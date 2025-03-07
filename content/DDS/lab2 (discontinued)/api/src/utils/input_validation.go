package utils

import "strings"

func IsValidEmail(email string) bool {
    return len(email) > 0 && strings.Contains(email, "@") && strings.Contains(email, ".")
}