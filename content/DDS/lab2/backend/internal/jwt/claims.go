package jwt

import (
	"time"

	"github.com/golang-jwt/jwt/v5"
)

type AccessClaims struct {
	UserID    int       `json:"user_id"`
	Role      string    `json:"role"`
	ExpiresAt time.Time `json:"exp"`
	IssuedAt  time.Time `json:"iat"`
}

func (j *AccessClaims) GetAudience() (jwt.ClaimStrings, error) {
	return nil, nil
}

func (j *AccessClaims) GetIssuer() (string, error) {
	return "", nil
}

func (j *AccessClaims) GetNotBefore() (*jwt.NumericDate, error) {
	return nil, nil
}

func (j *AccessClaims) GetSubject() (string, error) {
	return "", nil
}

func (j AccessClaims) GetExpirationTime() (*jwt.NumericDate, error) {
	return jwt.NewNumericDate(j.ExpiresAt), nil
}

func (j AccessClaims) GetIssuedAt() (*jwt.NumericDate, error) {
	return jwt.NewNumericDate(j.IssuedAt), nil
}

func (j AccessClaims) Valid() error {
	now := time.Now()
	if now.After(j.ExpiresAt) {
		return jwt.ErrTokenExpired
	}
	return nil
}

func NewAccessClaims(userID int, role string, ttl time.Duration) *AccessClaims {
	return &AccessClaims{
		UserID:    userID,
		Role:      role,
		ExpiresAt: time.Now().Add(ttl),
		IssuedAt:  time.Now(),
	}
}

type RefreshClaims struct {
	UserID    int       `json:"user_id"`
	ExpiresAt time.Time `json:"exp"`
	IssuedAt  time.Time `json:"iat"`
}

func (j *RefreshClaims) GetAudience() (jwt.ClaimStrings, error) {
	return nil, nil
}

func (j *RefreshClaims) GetIssuer() (string, error) {
	return "", nil
}

func (j *RefreshClaims) GetNotBefore() (*jwt.NumericDate, error) {
	return nil, nil
}

func (j *RefreshClaims) GetSubject() (string, error) {
	return "", nil
}

func (j RefreshClaims) GetExpirationTime() (*jwt.NumericDate, error) {
	return jwt.NewNumericDate(j.ExpiresAt), nil
}

func (j RefreshClaims) GetIssuedAt() (*jwt.NumericDate, error) {
	return jwt.NewNumericDate(j.IssuedAt), nil
}

func (j RefreshClaims) Valid() error {
	now := time.Now()
	if now.After(j.ExpiresAt) {
		return jwt.ErrTokenExpired
	}
	return nil
}

func NewRefreshClaims(userID int, ttl time.Duration) *RefreshClaims {
	return &RefreshClaims{
		UserID:    userID,
		ExpiresAt: time.Now().Add(ttl),
		IssuedAt:  time.Now(),
	}
}
