package types

import "fmt"

type Role string

const (
	ADMIN   Role    = "ADMIN"
	USER    Role    = "USER"
)

func (r Role) String() string {
    return string(r)
}

func (r Role) Validate() error {
	switch r {
    case ADMIN, USER:
        return nil
    default:
        return fmt.Errorf("invalid role: %s", r)
    }
}

func (r Role) IsValid() bool {
    return r.Validate() == nil
}

func ParseRole(s string) (Role, error) {
	role := Role(s)
	if err := role.Validate(); err != nil {
        return "", err
    }
	return role, nil
}