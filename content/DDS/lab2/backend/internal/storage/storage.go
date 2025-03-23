package storage

import (
	"context"

	"dds_lab2-backend/internal/entity"
)

type UserStorage interface {
    Create(ctx context.Context, user *entity.User) error
    GetById(ctx context.Context, id int) (*entity.User, error)
    GetByUsername(ctx context.Context, username string) (*entity.User, error)
    Update(ctx context.Context, user *entity.User) error
    Delete(ctx context.Context, id int) error
}

type RefreshTokenStorage interface {
    Create(ctx context.Context, token *entity.RefreshToken) error
    GetByHash(ctx context.Context, hash string) (*entity.RefreshToken, error)
    CheckRevokedByHash(ctx context.Context, hash string) (bool, error)
    RevokeByHash(ctx context.Context, hash string) error
    Delete(ctx context.Context, hash string) error
}