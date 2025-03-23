package service

import (
	"context"
	"errors"
	"fmt"
	"time"

	"github.com/golang-jwt/jwt/v5"

	"dds_lab2-backend/internal/config"
	"dds_lab2-backend/internal/dto/auth"
	"dds_lab2-backend/internal/entity"
	myJwt "dds_lab2-backend/internal/jwt"
	"dds_lab2-backend/internal/storage"
	"dds_lab2-backend/internal/types"
	"dds_lab2-backend/internal/utils"
)

type AuthService struct {
	userStorage         storage.UserStorage
	refreshTokenStorage storage.RefreshTokenStorage
	jwtConfig           config.JWTConfig
	bcryptConfig        config.BcryptConfig
}

func NewAuthService(
	userStorage storage.UserStorage,
	refreshTokenStorage storage.RefreshTokenStorage,
	jwtCfg config.JWTConfig,
	bcryptCfg config.BcryptConfig,
) *AuthService {
	return &AuthService{
		userStorage:         userStorage,
		refreshTokenStorage: refreshTokenStorage,
		jwtConfig:           jwtCfg,
		bcryptConfig:        bcryptCfg,
	}
}

func (s *AuthService) Register(ctx context.Context, req auth.AuthRequest, role types.Role) (*auth.AuthResponse, error) {
	_, err := s.userStorage.GetByUsername(ctx, req.Username)
	var entityNotFoundError *storage.EntityNotFoundError
	switch {
	case err == nil:
		return nil, storage.NewEntityDuplicateError(entity.USER, "username", req.Username)
	case !errors.As(err, &entityNotFoundError):
		return nil, fmt.Errorf("failed to check for duplicate username: %w", err)
	}

	passwordHash, err := utils.HashPassword(req.Password, s.bcryptConfig.SaltRounds)
	if err != nil {
		return nil, fmt.Errorf("failed to hash password: %w", err)
	}

	user := &entity.User{
		Username:     req.Username,
		PasswordHash: passwordHash,
		Role:         role,
	}
	if err := s.userStorage.Create(ctx, user); err != nil {
		return nil, fmt.Errorf("failed to create user: %w", err)
	}

	return s.generateTokens(user.ID, user.Role.String())
}

func (s *AuthService) Login(ctx context.Context, req auth.AuthRequest) (*auth.AuthResponse, error) {
	user, err := s.userStorage.GetByUsername(ctx, req.Username)
	if err != nil {
		if errors.As(err, &storage.EntityNotFoundError{}) {
			return nil, err
		}
		return nil, fmt.Errorf("failed to get user: %w", err)
	}

	if !utils.CheckPasswordHash(req.Password, user.PasswordHash) {
		return nil, NewInvalidCredentialsError(req.Username)
	}

	return s.generateTokens(user.ID, user.Role.String())
}

func (s *AuthService) Revoke(ctx context.Context, rtokenStr string) error {
	hash := utils.HashToken(rtokenStr)

	if _, err := s.refreshTokenStorage.CheckRevokedByHash(ctx, hash); err != nil {
		if errors.As(err, &storage.EntityNotFoundError{}) {
			return nil
		}
		return fmt.Errorf("failed to check if token is revoked: %w", err)
	}

	if err := s.refreshTokenStorage.RevokeByHash(ctx, hash); err != nil {
		return fmt.Errorf("failed to revoke token: %w", err)
	}

	return nil
}

func (s *AuthService) Refresh(ctx context.Context, rtokenStr string) (*auth.AuthResponse, error) {
	token, err := jwt.ParseWithClaims(
		rtokenStr,
		&myJwt.RefreshClaims{},
		func(token *jwt.Token) (any, error) {
			return []byte(s.jwtConfig.RefreshSecret), nil
		},
	)

	claims, ok := token.Claims.(*myJwt.RefreshClaims)
	if !ok || !token.Valid {
		return nil, fmt.Errorf("invalid refresh token: %w", err)
	}

	hash := utils.HashToken(rtokenStr)
	if _, err := s.refreshTokenStorage.GetByHash(ctx, hash); err != nil {
		return nil, fmt.Errorf("token not found: %w", err)
	}

	if err := s.refreshTokenStorage.RevokeByHash(ctx, hash); err != nil {
		return nil, fmt.Errorf("token revocation failed: %w", err)
	}

	user, err := s.userStorage.GetById(ctx, claims.UserID)
	if err != nil {
		return nil, fmt.Errorf("user not found: %w", err)
	}

	return s.generateTokens(user.ID, user.Role.String())
}

func (s *AuthService) ValidateAccessToken(atokenStr string) (*myJwt.AccessClaims, error) {
	token, err := jwt.ParseWithClaims(
		atokenStr,
		&myJwt.AccessClaims{},
		func(token *jwt.Token) (any, error) {
			return []byte(s.jwtConfig.AccessSecret), nil
		},
	)

	if err != nil || !token.Valid {
		return nil, fmt.Errorf("invalid token")
	}

	claims, ok := token.Claims.(*myJwt.AccessClaims)
	if !ok {
		return nil, fmt.Errorf("invalid token claims")
	}

	return claims, nil
}

func (s *AuthService) Logout(ctx context.Context, rtokenStr string) error {
	hash := utils.HashToken(rtokenStr)
	return s.refreshTokenStorage.RevokeByHash(ctx, hash)
}

func (s *AuthService) generateTokens(userID int, role string) (*auth.AuthResponse, error) {
	accessClaims := myJwt.NewAccessClaims(userID, role, s.jwtConfig.AccessTTL)
	accessToken := jwt.NewWithClaims(jwt.SigningMethodHS256, accessClaims)
	accessTokenString, err := accessToken.SignedString([]byte(s.jwtConfig.AccessSecret))
	if err != nil {
		return nil, fmt.Errorf("failed to sign access token: %w", err)
	}

	refreshClaims := myJwt.NewRefreshClaims(userID, s.jwtConfig.RefreshTTL)
	refreshToken := jwt.NewWithClaims(jwt.SigningMethodHS256, refreshClaims)
	refreshTokenString, err := refreshToken.SignedString([]byte(s.jwtConfig.RefreshSecret))
	if err != nil {
		return nil, fmt.Errorf("failed to sign refresh token: %w", err)
	}

	hash := utils.HashToken(refreshTokenString)
	refreshTokenEntity := &entity.RefreshToken{
		UserID:    userID,
		TokenHash: hash,
		ExpiresAt: time.Now().Add(s.jwtConfig.RefreshTTL),
	}

	if err := s.refreshTokenStorage.Create(context.Background(), refreshTokenEntity); err != nil {
		return nil, fmt.Errorf("failed to save refresh token: %w", err)
	}

	return &auth.AuthResponse{
		AccessToken:  accessTokenString,
		RefreshToken: refreshTokenString,
	}, nil
}
