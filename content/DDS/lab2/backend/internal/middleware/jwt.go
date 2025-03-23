package middleware

import (
	"net/http"
	"strings"

	"github.com/gin-gonic/gin"

	"dds_lab2-backend/internal/service"
	"dds_lab2-backend/internal/types"
)

func AuthMiddleware(authService *service.AuthService, allowedRoles []types.Role) gin.HandlerFunc {
	return func(c *gin.Context) {
		token := strings.TrimPrefix(c.GetHeader("Authorization"), "Bearer ")
		if token == "" {
			c.AbortWithStatusJSON(http.StatusUnauthorized, gin.H{"error": "authorization header required"})
			return
		}

		claims, err := authService.ValidateAccessToken(token)
		if err != nil {
			c.AbortWithStatusJSON(http.StatusUnauthorized, gin.H{"error": "invalid token"})
			return
		}

		if len(allowedRoles) > 0 {
			if !containsRole(allowedRoles, claims.Role) {
				c.AbortWithStatusJSON(http.StatusForbidden, gin.H{"error": "insufficient permissions"})
				return
			}
		}

		c.Set("userClaims", claims)
		c.Next()
	}
}

func containsRole(allowedRoles []types.Role, role string) bool {
	for _, r := range allowedRoles {
		if r.String() == role {
			return true
		}
	}
	return false
}
