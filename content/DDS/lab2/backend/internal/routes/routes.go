package routes

import (
	"github.com/gin-gonic/gin"
	swaggerFiles "github.com/swaggo/files"
	ginSwagger "github.com/swaggo/gin-swagger"

	"dds_lab2-backend/internal/handlers"
	"dds_lab2-backend/internal/middleware"
	"dds_lab2-backend/internal/service"
	"dds_lab2-backend/internal/types"
)

func SetupRouter(
	authHandler *handlers.AuthHandler,
	userHandler *handlers.UserHandler,
	authService *service.AuthService,
) *gin.Engine {
	router := gin.Default()

	public := router.Group("/auth")
	{
		public.POST("/register", authHandler.Register)
		public.POST("/login", authHandler.Login)
	}

	protectedUser := router.Group("/")
	protectedUser.Use(middleware.AuthMiddleware(authService, []types.Role{types.USER, types.ADMIN}))
	{
		auth := protectedUser.Group("/auth")
		{
			auth.POST("/logout", authHandler.Logout)
			auth.POST("/refresh", authHandler.Refresh)
			auth.GET("/check", authHandler.CheckAuth)
		}

		user := protectedUser.Group("/users")
		{
			user.GET("/me", userHandler.GetCurrentUser)
			user.GET("/id/:id", userHandler.GetUserById)
			user.GET("/username/:username", userHandler.GetUserByUsername)
		}
	}

	swagger := router.Group("/")
	swagger.GET("/swagger/*any", ginSwagger.WrapHandler(swaggerFiles.Handler))

	return router
}
