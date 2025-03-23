// @title           DDS Lab2 Backend Documentation
// @description     This is a sample RESTful API for managing users for DDS lab2.
// @BasePath        /
// @securityDefinitions.apikey BearerAuth
// @in header
// @name Authorization
package main

import (
	"fmt"
	"log"

	"github.com/gin-contrib/cors"
	"github.com/gin-gonic/gin"

	"dds_lab2-backend/internal/api"
	"dds_lab2-backend/internal/config"
	"dds_lab2-backend/internal/handlers"
	"dds_lab2-backend/internal/service"
	"dds_lab2-backend/internal/storage/postgres"

	_ "dds_lab2-backend/docs"
)

func main() {
	configPath := "../../.env"
	cfg, err := config.Load(configPath)
	if err != nil {
		log.Fatalf("Configuration load failed: %v", err)
	}

	db, err := postgres.New(cfg.DB)
	if err != nil {
		log.Fatalf("Database connection failed: %v", err)
	}
	defer db.Close()

	userStorage := postgres.NewUserStorage(db)
	refreshTokenStorage := postgres.NewRefreshTokenStorage(db)

	authService := service.NewAuthService(
		userStorage,
		refreshTokenStorage,
		cfg.JWT,
		cfg.Bcrypt,
	)

	userService := service.NewUserService(userStorage)

	authHandler := handlers.NewAuthHandler(authService)
	userHandler := handlers.NewUserHandler(userService)

	router := gin.Default()
	router.Use(cors.New(cors.Config{
		AllowOrigins:     cfg.CORS.AllowedOrigins,
		AllowMethods:     cfg.CORS.AllowedMethods,
		AllowHeaders:     cfg.CORS.AllowedHeaders,
		ExposeHeaders:    cfg.CORS.ExposedHeaders,
		AllowCredentials: cfg.CORS.AllowCredentials,
		MaxAge:           cfg.CORS.MaxAge,
	}))

	router = api.SetupRouter(authHandler, userHandler, authService)

	addr := fmt.Sprintf(":%s", cfg.Server.Port)
	log.Printf("Starting server on %s", addr)
	if err := router.Run(addr); err != nil {
		log.Fatalf("Server start failed: %v", err)
	}
}
