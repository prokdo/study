package middleware

import (
	"fmt"
	"net/http"
	"os"

	"github.com/golang-jwt/jwt/v5"
)

var jwtKey []byte

func init() {
    jwtKey = []byte(os.Getenv("JWT_KEY"))
    if len(jwtKey) == 0 {
        jwtKey = []byte("DEV")
        // panic("JWT_KEY is not set")
    }
}

func GetJwtKey() []byte {
    return jwtKey
}

func AuthMiddleware(next http.Handler) http.Handler {
    return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
        authHeader := r.Header.Get("Authorization")
        if authHeader == "" {
            http.Error(w, `{"error": "missing_token", "message": "Authorization header is missing"}`, http.StatusUnauthorized)
            return
        }

        tokenString := authHeader[len("Bearer "):]
        if tokenString == "" {
            http.Error(w, `{"error": "invalid_token_format", "message": "Token must be in the format 'Bearer <token>'"}`, http.StatusUnauthorized)
            return
        }

        token, err := jwt.ParseWithClaims(tokenString, &jwt.RegisteredClaims{}, func(token *jwt.Token) (any, error) {
            if _, ok := token.Method.(*jwt.SigningMethodHMAC); !ok {
                return nil, fmt.Errorf("unexpected signing method: %v", token.Header["alg"])
            }
            return jwtKey, nil
        })

        if err != nil || !token.Valid {
            http.Error(w, `{"error": "invalid_token", "message": "Invalid or expired token"}`, http.StatusUnauthorized)
            return
        }

        next.ServeHTTP(w, r)
    })
}