package storage

import (
	"fmt"
	"time"
)

type EntityNotFoundError struct {
	EntityType string
	ID         int
	Err        error
}

func (e EntityNotFoundError) Error() string {
	return fmt.Sprintf("%s with ID %d not found: %v", e.EntityType, e.ID, e.Err)
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

type EntityUpdateError struct {
	EntityType string
	ID         int
	Err        error
}

func (e EntityUpdateError) Error() string {
	return fmt.Sprintf("%s with ID %d update failed: %v", e.EntityType, e.ID, e.Err)
}

type TokenExpiredError struct {
	TokenHash string
	ExpiresAt time.Time
}

func (e TokenExpiredError) Error() string {
	return fmt.Sprintf("token with hash %s expired at %s", e.TokenHash, e.ExpiresAt.Format(time.RFC3339))
}

type TokenRevokedError struct {
	TokenHash string
}

func (e TokenRevokedError) Error() string {
	return fmt.Sprintf("token with hash %s is revoked", e.TokenHash)
}
