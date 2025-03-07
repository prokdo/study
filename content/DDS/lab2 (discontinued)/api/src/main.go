//	@title			DDS Lab2 API Documentation
//	@version		1.0
//	@description	This is a sample RESTful API for managing users for DDS lab2.
//	@host			localhost:8080
//	@BasePath		/api
package main

import (
	"log"
	"net/http"
	"os"

	"dds_lab2-api/db"
	_ "dds_lab2-api/docs"
	"dds_lab2-api/handlers"
	"dds_lab2-api/middleware"

	"github.com/gorilla/mux"
	httpSwagger "github.com/swaggo/http-swagger"
)

func main() {
    db.InitDB()
    defer db.DB.Close()

    r := mux.NewRouter()

    r.HandleFunc("/api/register", handlers.RegisterUser).Methods("POST")
    r.HandleFunc("/api/login", handlers.LoginUser).Methods("POST")
    r.HandleFunc("/api/check-email", handlers.CheckUserExistsByEmail).Methods("GET")

    protected := r.PathPrefix("/api").Subrouter()
    protected.Use(middleware.AuthMiddleware)
    protected.HandleFunc("/users/id", handlers.GetUserById).Methods("GET")
    protected.HandleFunc("/users/email", handlers.GetUserByEmail).Methods("GET")

    if os.Getenv("ENVIRONMENT") != "prod" {
        r.PathPrefix("/swagger/").Handler(httpSwagger.WrapHandler)
    }

    log.Println("Server started on :8080")
    log.Fatal(http.ListenAndServe(":8080", r))
}