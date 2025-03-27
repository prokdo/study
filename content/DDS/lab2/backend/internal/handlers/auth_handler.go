package handlers

import (
	"errors"
	"fmt"
	"net/http"

	"github.com/gin-gonic/gin"

	"dds_lab2-backend/internal/dto/auth"
	"dds_lab2-backend/internal/service"
	"dds_lab2-backend/internal/storage"
	"dds_lab2-backend/internal/types"
)

type AuthHandler struct {
	authService *service.AuthService
}

func NewAuthHandler(authService *service.AuthService) *AuthHandler {
	return &AuthHandler{authService: authService}
}

// Register godoc
//
//	@Summary	Register new user
//	@Tags		auth
//	@Accept		json
//	@Produce	json
//	@Param		request	body		auth.AuthRequest	true	"Registration data"	example({"username":"user123","password":"qwerty"})
//	@Success	201		{object}	auth.AuthResponse
//	@Failure	400		{object}	map[string]string
//	@Failure	409		{object}	map[string]string
//	@Failure	500		{object}	map[string]string
//	@Router		/auth/register [post]
func (h *AuthHandler) Register(c *gin.Context) {
	ctx := c.Request.Context()
	var req auth.AuthRequest

	if err := c.ShouldBindJSON(&req); err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": "invalid request body"})
		return
	}

	resp, err := h.authService.Register(ctx, req, types.USER)
	if err != nil {
		handleAuthError(c, err)
		return
	}

	c.JSON(http.StatusCreated, resp)
}

// Login godoc
//
//	@Summary	User login
//	@Tags		auth
//	@Accept		json
//	@Produce	json
//	@Param		request	body		auth.AuthRequest	true	"Login credentials"	example({"username":"user123","password":"qwerty"})
//	@Success	200		{object}	auth.AuthResponse
//	@Failure	400		{object}	map[string]string
//	@Failure	401		{object}	map[string]string
//	@Failure	500		{object}	map[string]string
//	@Router		/auth/login [post]
func (h *AuthHandler) Login(c *gin.Context) {
	ctx := c.Request.Context()
	var req auth.AuthRequest

	if err := c.ShouldBindJSON(&req); err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": "invalid request body"})
		return
	}

	resp, err := h.authService.Login(ctx, req)
	if err != nil {
		handleAuthError(c, err)
		return
	}

	c.JSON(http.StatusOK, resp)
}

// Logout godoc
//
//	@Summary		Logout user
//	@Description	Invalidates refresh token
//	@Tags			auth
//	@Security		BearerAuth
//	@Accept			json
//	@Param			request	body	auth.LogoutRequest	true	"Refresh token"	example({"refresh_token":"eyJhbGciOi..."})
//	@Success		204
//	@Failure		400	{object}	map[string]string
//	@Failure		401	{object}	map[string]string
//	@Failure		500	{object}	map[string]string
//	@Router			/auth/logout [post]
func (h *AuthHandler) Logout(c *gin.Context) {
	ctx := c.Request.Context()
	var req auth.LogoutRequest

	if err := c.ShouldBindJSON(&req); err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": "invalid request body"})
		return
	}

	if err := h.authService.Logout(ctx, req.RefreshToken); err != nil {
		handleAuthError(c, err)
		return
	}

	c.Status(http.StatusNoContent)
}

// Refresh godoc
//
//	@Summary	Refresh tokens
//	@Tags		auth
//	@Accept		json
//	@Produce	json
//	@Param		request	body		auth.RefreshRequest	true	"Refresh token"	example({"refresh_token":"eyJhbGciOi..."})
//	@Success	200		{object}	auth.AuthResponse
//	@Failure	400		{object}	map[string]string
//	@Failure	401		{object}	map[string]string
//	@Failure	500		{object}	map[string]string
//	@Router		/auth/refresh [post]
func (h *AuthHandler) Refresh(c *gin.Context) {
	ctx := c.Request.Context()
	var req auth.RefreshRequest

	if err := c.ShouldBindJSON(&req); err != nil {
		handleAuthError(c, err)
		return
	}

	resp, err := h.authService.Refresh(ctx, req.RefreshToken)
	if err != nil {
		handleAuthError(c, err)
		return
	}

	c.JSON(http.StatusOK, resp)
}

// CheckAuth godoc
//
//	@Summary		Check authentication status
//	@Description	Verifies JWT access token validity
//	@Tags			auth
//	@Security		BearerAuth
//	@Produce		json
//	@Success		204
//	@Failure		401	{object}	map[string]string
//	@Router			/auth/check [get]
func (h *AuthHandler) CheckAuth(c *gin.Context) {
	c.Status(http.StatusNoContent)
}

func handleAuthError(c *gin.Context, err error) {
	switch {
	case errors.As(err, new(*service.InvalidCredentialsError)):
		c.JSON(http.StatusUnauthorized, gin.H{"error": err.Error()})
	case errors.As(err, new(*storage.EntityDuplicateError)):
		c.JSON(http.StatusConflict, gin.H{"error": err.Error()})
	case errors.As(err, new(*storage.TokenExpiredError)):
		c.JSON(http.StatusUnauthorized, gin.H{"error": err.Error()})
	case errors.As(err, new(*service.InvalidTokenError)):
		c.JSON(http.StatusUnauthorized, gin.H{"error": err.Error()})
	default:
		c.JSON(http.StatusInternalServerError, gin.H{"error": fmt.Sprintf("internal server error: %v", err.Error())})
	}
}
