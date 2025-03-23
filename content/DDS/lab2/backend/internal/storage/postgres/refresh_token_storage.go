package postgres

import (
	"context"
	"database/sql"
	"errors"
	"fmt"
	"time"

	"github.com/jmoiron/sqlx"
	"github.com/lib/pq"

	"dds_lab2-backend/internal/entity"
	"dds_lab2-backend/internal/storage"
)

type RefreshTokenStorage struct {
	db *sqlx.DB
}

func NewRefreshTokenStorage(db *sqlx.DB) *RefreshTokenStorage {
	return &RefreshTokenStorage{db: db}
}

func (s *RefreshTokenStorage) Create(ctx context.Context, token *entity.RefreshToken) error {
	query := `
		INSERT INTO refresh_tokens (user_id, token_hash, expires_at, is_revoked)
		VALUES (:user_id, :token_hash, :expires_at, :is_revoked)
		RETURNING issued_at`

	stmt, err := s.db.PrepareNamedContext(ctx, query)
	if err != nil {
		return fmt.Errorf("failed to prepare query: %w", err)
	}
	defer stmt.Close()

	err = stmt.GetContext(ctx, token, token)
	if err != nil {
		if pqErr, ok := err.(*pq.Error); ok && pqErr.Code == "23505" {
			return storage.NewEntityDuplicateError(
				entity.REFRESH_TOKEN,
				"token_hash",
				token.TokenHash,
			)
		}
		return fmt.Errorf("failed to create token: %w", err)
	}
	return nil
}

func (s *RefreshTokenStorage) GetByHash(ctx context.Context, hash string) (*entity.RefreshToken, error) {
	query := `
		SELECT user_id, token_hash, expires_at, is_revoked, issued_at
		FROM refresh_tokens
		WHERE token_hash = $1`

	var token entity.RefreshToken
	err := s.db.GetContext(ctx, &token, query, hash)
	if err != nil {
		if errors.Is(err, sql.ErrNoRows) {
			return nil, storage.NewEntityNotFoundError(
				entity.REFRESH_TOKEN,
				0,
				fmt.Errorf("token with hash %s not found", hash),
			)
		}
		return nil, fmt.Errorf("failed to get token by hash: %w", err)
	}
	return &token, nil
}

func (s *RefreshTokenStorage) CheckRevokedByHash(ctx context.Context, hash string) (bool, error) {
	token, err := s.GetByHash(ctx, hash)
	if err != nil {
		if errors.As(err, &storage.EntityNotFoundError{}) {
			return true, nil
		}
		return false, err
	}

	if time.Now().After(token.ExpiresAt) {
		return false, storage.NewTokenExpiredError(hash, token.ExpiresAt)
	}

	return token.IsRevoked, nil
}

func (s *RefreshTokenStorage) RevokeByHash(ctx context.Context, hash string) error {
	query := `
		UPDATE refresh_tokens
		SET is_revoked = TRUE
		WHERE token_hash = $1
		AND is_revoked = FALSE`

	result, err := s.db.ExecContext(ctx, query, hash)
	if err != nil {
		return fmt.Errorf("failed to revoke token: %w", err)
	}

	rowsAffected, _ := result.RowsAffected()
	if rowsAffected == 0 {
		token, err := s.GetByHash(ctx, hash)
		switch {
		case errors.As(err, &storage.EntityNotFoundError{}):
			return storage.NewEntityNotFoundError(
				entity.REFRESH_TOKEN,
				0,
				fmt.Errorf("token with hash %s not found", hash),
			)
		case token != nil && token.IsRevoked:
			return storage.NewTokenRevokedError(hash, time.Now())
		}
	}

	return nil
}

func (s *RefreshTokenStorage) Delete(ctx context.Context, hash string) error {
	query := `DELETE FROM refresh_tokens WHERE token_hash = $1`
	result, err := s.db.ExecContext(ctx, query, hash)
	if err != nil {
		return fmt.Errorf("failed to delete token: %w", err)
	}

	rowsAffected, _ := result.RowsAffected()
	if rowsAffected == 0 {
		return storage.NewEntityNotFoundError(
			entity.REFRESH_TOKEN,
			0,
			fmt.Errorf("token with hash %s not found", hash),
		)
	}

	return nil
}
