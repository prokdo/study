package entity

import (
	"time"

	"github.com/go-playground/validator/v10"
)

type RefreshToken struct {
    ID          int         `json:"id" db:"id" validate:"min=1"`
    UserID      int         `json:"user_id" db:"user_id" validate:"required,min=1"`
    TokenHash   string      `json:"token_hash" db:"token_hash" validate:"required"`
    IsRevoked   bool        `json:"is_revoked" db:"is_revoked" validate:"required"`
    ExpiresAt   time.Time   `json:"exp" db:"expires_at" validate:"required"`
    IssuedAt    time.Time   `json:"iat" db:"issued_at"`
}

func (rt RefreshToken) Validate() error {
    validate := validator.New()

    validate.RegisterValidation("expiry_check", func(fl validator.FieldLevel) bool {
        return fl.Field().Interface().(time.Time).After(time.Now())
    })

    return validate.Struct(rt)
}