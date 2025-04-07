package service

import (
	"context"

	"dds_lab2-backend/internal/entity"
	"dds_lab2-backend/internal/storage"
)

type UserService struct {
	userStorage storage.UserStorage
}

func NewUserService(userStorage storage.UserStorage) *UserService {
	return &UserService{userStorage: userStorage}
}

func (s *UserService) GetById(ctx context.Context, id int) (*entity.User, error) {
	return s.userStorage.GetById(ctx, id)
}

func (s *UserService) GetByUsername(ctx context.Context, username string) (*entity.User, error) {
	return s.userStorage.GetByUsername(ctx, username)
}
