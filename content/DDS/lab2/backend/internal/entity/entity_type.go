package entity

import "fmt"

type EntityType string

const (
	USER EntityType 			= "USER"
	REFRESH_TOKEN EntityType    = "REFRESH_TOKEN"
)

func (et EntityType) String() string {
    return string(et)
}

func (et EntityType) Validate() error {
	switch et {
    case USER, REFRESH_TOKEN:
        return nil
    default:
        return fmt.Errorf("invalid entity type: %s", et)
    }
}

func (et EntityType) IsValid() bool {
    return et.Validate() == nil
}

func ParseEntityType(s string) (EntityType, error) {
	et := EntityType(s)
    if err := et.Validate(); err != nil {
        return "", err
    }
    return et, nil
}