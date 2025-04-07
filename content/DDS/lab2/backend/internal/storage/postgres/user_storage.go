package postgres

import (
	"context"
	"database/sql"
	"errors"
	"fmt"

	"github.com/jmoiron/sqlx"
	"github.com/lib/pq"

	"dds_lab2-backend/internal/entity"
	"dds_lab2-backend/internal/storage"
)

type UserStorage struct {
	db *sqlx.DB
}

func NewUserStorage(db *sqlx.DB) *UserStorage {
	return &UserStorage{db: db}
}

func (s *UserStorage) Create(ctx context.Context, user *entity.User) error {
	query := `
		INSERT INTO users (username, password_hash, role)
		VALUES (:username, :password_hash, :role)
		RETURNING id, created_at`

	stmt, err := s.db.PrepareNamedContext(ctx, query)
	if err != nil {
		return fmt.Errorf("failed to prepare query: %w", err)
	}
	defer stmt.Close()

	err = stmt.GetContext(ctx, user, user)
	if err != nil {
		if pqErr, ok := err.(*pq.Error); ok && pqErr.Code == "23505" {
			return &storage.EntityDuplicateError{
				EntityType: entity.USER.String(),
				Field:      "username",
				Value:      user.Username,
			}
		}
		return fmt.Errorf("failed to create user: %w", err)
	}
	return nil
}

func (s *UserStorage) GetById(ctx context.Context, id int) (*entity.User, error) {
	query := `
		SELECT id, username, password_hash, role, created_at
		FROM users
		WHERE id = $1`

	var user entity.User
	err := s.db.GetContext(ctx, &user, query, id)
	if err != nil {
		if errors.Is(err, sql.ErrNoRows) {
			return nil, &storage.EntityNotFoundError{
				EntityType: entity.USER.String(),
				ID:         id,
				Err:        err,
			}
		}
		return nil, fmt.Errorf("failed to get user by id: %w", err)
	}
	return &user, nil
}

func (s *UserStorage) GetByUsername(ctx context.Context, username string) (*entity.User, error) {
	query := `
		SELECT id, username, password_hash, role, created_at
		FROM users
		WHERE username = $1`

	var user entity.User
	err := s.db.GetContext(ctx, &user, query, username)
	if err != nil {
		if errors.Is(err, sql.ErrNoRows) {
			return nil, &storage.EntityNotFoundError{
				EntityType: entity.USER.String(),
				ID:         0,
				Err:        fmt.Errorf("user with username %s not found", username),
			}
		}
		return nil, fmt.Errorf("failed to get user by username: %w", err)
	}
	return &user, nil
}

func (s *UserStorage) Update(ctx context.Context, user *entity.User) error {
	existing, err := s.GetById(ctx, user.ID)
	if err != nil {
		return fmt.Errorf("failed to get existing user: %w", err)
	}

	updates := make(map[string]any)
	if user.Username != "" && user.Username != existing.Username {
		updates["username"] = user.Username
	}
	if user.PasswordHash != "" && user.PasswordHash != existing.PasswordHash {
		updates["password_hash"] = user.PasswordHash
	}
	if user.Role != "" && user.Role != existing.Role {
		updates["role"] = user.Role
	}

	if len(updates) == 0 {
		return &storage.EntityUpdateError{
			EntityType: entity.USER.String(),
			ID:         user.ID,
			Err:        errors.New("no fields to update"),
		}
	}

	updates["id"] = user.ID

	query, args, err := sqlx.Named(`
		UPDATE users SET
		username = COALESCE(:username, username),
		password_hash = COALESCE(:password_hash, password_hash),
		role = COALESCE(:role, role)
		WHERE id = :id`, updates)
	if err != nil {
		return fmt.Errorf("failed to build query: %w", err)
	}

	query = s.db.Rebind(query)
	result, err := s.db.ExecContext(ctx, query, args...)
	if err != nil {
		if pqErr, ok := err.(*pq.Error); ok && pqErr.Code == "23505" {
			return &storage.EntityDuplicateError{
				EntityType: entity.USER.String(),
				Field:      "username",
				Value:      user.Username,
			}
		}
		return &storage.EntityUpdateError{
			EntityType: entity.USER.String(),
			ID:         user.ID,
			Err:        err,
		}
	}

	rowsAffected, _ := result.RowsAffected()
	if rowsAffected == 0 {
		return &storage.EntityUpdateError{
			EntityType: entity.USER.String(),
			ID:         user.ID,
			Err:        errors.New("user not found during update"),
		}
	}

	return nil
}

func (s *UserStorage) Delete(ctx context.Context, id int) error {
	query := `DELETE FROM users WHERE id = $1`
	result, err := s.db.ExecContext(ctx, query, id)
	if err != nil {
		return fmt.Errorf("failed to delete user: %w", err)
	}

	rowsAffected, _ := result.RowsAffected()
	if rowsAffected == 0 {
		return &storage.EntityNotFoundError{
			EntityType: entity.USER.String(),
			ID:         id,
			Err:        errors.New("user not found for deletion"),
		}
	}

	return nil
}
