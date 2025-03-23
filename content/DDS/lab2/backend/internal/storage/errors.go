package storage

import (
	"fmt"
	"time"

	"dds_lab2-backend/internal/entity"
)

type EntityNotFoundError struct {
	EntityType string
	ID         int
	Err        error
}

func (e EntityNotFoundError) Error() string {
	return fmt.Sprintf("%s with ID %d not found: %v", e.EntityType, e.ID, e.Err)
}

func NewEntityNotFoundError(et entity.EntityType, id int, err error) *EntityNotFoundError {
	return &EntityNotFoundError{
		EntityType: et.String(),
		ID:         id,
		Err:        err,
	}
}

type EntityDuplicateError struct {
	EntityType string
	Field      string
	Value      string
}

func (e EntityDuplicateError) Error() string {
	return fmt.Sprintf("%s with %s '%s' already exists",
		e.EntityType, e.Field, e.Value)
}

func NewEntityDuplicateError(et entity.EntityType, field, value string) *EntityDuplicateError {
	return &EntityDuplicateError{
		EntityType: et.String(),
		Field:      field,
		Value:      value,
	}
}

type EntityUpdateError struct {
	EntityType string
	ID         int
	Err        error
}

func (e EntityUpdateError) Error() string {
	return fmt.Sprintf("%s with ID %d update failed: %v", e.EntityType, e.ID, e.Err)
}

func NewEntityUpdateError(et entity.EntityType, id int, err error) *EntityUpdateError {
	return &EntityUpdateError{
		EntityType: et.String(),
		ID:         id,
		Err:        err,
	}
}

type TokenExpiredError struct {
	TokenHash string
	ExpiresAt time.Time
}

func (e TokenExpiredError) Error() string {
	return fmt.Sprintf("token with hash %s expired at %s", e.TokenHash, e.ExpiresAt.Format(time.RFC3339))
}

func NewTokenExpiredError(hash string, expiresAt time.Time) *TokenExpiredError {
	return &TokenExpiredError{
		TokenHash: hash,
		ExpiresAt: expiresAt,
	}
}

type TokenRevokedError struct {
	TokenHash string
}

func (e TokenRevokedError) Error() string {
	return fmt.Sprintf("token with hash %s is revoked", e.TokenHash)
}

func NewTokenRevokedError(hash string, revokedAt time.Time) *TokenRevokedError {
	return &TokenRevokedError{TokenHash: hash}
}
