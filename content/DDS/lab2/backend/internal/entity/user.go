package entity

import (
	"regexp"
	"time"

	"github.com/go-playground/validator/v10"

	"dds_lab2-backend/internal/types"
)

type User struct {
    ID           	int       	`json:"id" db:"id" validate:"min=1"`
    Username     	string    	`json:"username" db:"username" validate:"required,username,max=50"`
    PasswordHash	string    	`json:"-" db:"password_hash" validate:"required"`
    Role        	types.Role  `json:"role" db:"role" validate:"required,valid_role"`
    CreatedAt    	time.Time	`json:"created_at" db:"created_at"`
}

func (u User) Validate() error {
    validate := validator.New()

    validate.RegisterValidation("username", func(fl validator.FieldLevel) bool {
        return regexp.MustCompile(`^[a-zA-Z0-9_-]+$`).MatchString(fl.Field().String())
    })

    validate.RegisterValidation("valid_role", func(fl validator.FieldLevel) bool {
        role, ok := fl.Field().Interface().(types.Role)
        return ok && role.IsValid()
    })

    return validate.Struct(u)
}