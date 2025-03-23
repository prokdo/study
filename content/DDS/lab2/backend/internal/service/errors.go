package service

import "fmt"

type InvalidTokenError struct {
	TokenHash string
}

func (e InvalidTokenError) Error() string {
	return fmt.Sprintf("token with hash %s is invalid", e.TokenHash)
}

func NewInvalidTokenError(hash string) *InvalidTokenError {
	return &InvalidTokenError{TokenHash: hash}
}

type InvalidCredentialsError struct {
	Username string
}

func (e InvalidCredentialsError) Error() string {
	return fmt.Sprintf("invalid credentials for user '%s'", e.Username)
}

func NewInvalidCredentialsError(username string) *InvalidCredentialsError {
	return &InvalidCredentialsError{Username: username}
}
