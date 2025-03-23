package handlers

import (
	"errors"
	"fmt"
	"net/http"
	"strconv"

	"github.com/gin-gonic/gin"

	"dds_lab2-backend/internal/jwt"
	"dds_lab2-backend/internal/service"
	"dds_lab2-backend/internal/storage"
)

type UserHandler struct {
	userService *service.UserService
}

func NewUserHandler(userService *service.UserService) *UserHandler {
	return &UserHandler{userService: userService}
}

// GetCurrentUser godoc
// @Summary Get current user info
// @Description Returns information about authenticated user
// @Tags user
// @Security BearerAuth
// @Produce json
// @Success 200 {object} UserInfoResponse
// @Failure 401 {object} map[string]string
// @Failure 404 {object} map[string]string
// @Router /users/me [get]
func (h *UserHandler) GetCurrentUser(c *gin.Context) {
	ctx := c.Request.Context()

	claims, exists := c.Get("userClaims")
	if !exists {
		c.JSON(http.StatusUnauthorized, gin.H{"error": "authorization required"})
		return
	}

	userID := claims.(*jwt.AccessClaims).UserID

	userInfo, err := h.userService.GetById(ctx, userID)
	if err != nil {
		handleServiceError(c, err)
		return
	}

	c.JSON(http.StatusOK, userInfo)
}

// GetUserById godoc
// @Summary Get user by ID
// @Tags user
// @Security BearerAuth
// @Produce json
// @Param id path int true "User ID"
// @Success 200 {object} UserInfoResponse
// @Failure 400 {object} map[string]string
// @Failure 404 {object} map[string]string
// @Router /users/id/{id} [get]
func (h *UserHandler) GetUserById(c *gin.Context) {
	ctx := c.Request.Context()

	idParam := c.Param("id")
	userID, err := strconv.Atoi(idParam)
	if err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": "invalid user ID"})
		return
	}

	userInfo, err := h.userService.GetById(ctx, userID)
	if err != nil {
		handleServiceError(c, err)
		return
	}

	c.JSON(http.StatusOK, userInfo)
}

// GetUserByUsername godoc
// @Summary Get user by username
// @Tags user
// @Security BearerAuth
// @Produce json
// @Param username path string true "Username"
// @Success 200 {object} UserInfoResponse
// @Failure 400 {object} map[string]string
// @Failure 404 {object} map[string]string
// @Router /users/username/{username} [get]
func (h *UserHandler) GetUserByUsername(c *gin.Context) {
	ctx := c.Request.Context()

	username := c.Param("username")
	if username == "" {
		c.JSON(http.StatusBadRequest, gin.H{"error": "username is required"})
		return
	}

	userInfo, err := h.userService.GetByUsername(ctx, username)
	if err != nil {
		handleServiceError(c, err)
		return
	}

	c.JSON(http.StatusOK, userInfo)
}

func handleServiceError(c *gin.Context, err error) {
	switch {
	case errors.As(err, &storage.EntityNotFoundError{}):
		c.JSON(http.StatusNotFound, gin.H{"error": err.Error()})
	default:
		c.JSON(http.StatusInternalServerError, gin.H{"error": fmt.Sprintf("internal server error: %v", err.Error())})
	}
}
