package types

import "fmt"

type GinMode string

const (
	DEBUG   GinMode = "debug"
	RELEASE GinMode = "release"
)

func (m GinMode) String() string {
	return string(m)
}

func (m GinMode) Validate() error {
	switch m {
	case DEBUG, RELEASE:
		return nil
	default:
		return fmt.Errorf("invalid Gin mode: %s", m)
	}
}

func (m GinMode) IsValid() bool {
	return m.Validate() == nil
}

func ParseGinMode(s string) (GinMode, error) {
	mode := GinMode(s)
	if err := mode.Validate(); err != nil {
		return "", err
	}
	return mode, nil
}
