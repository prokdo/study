package handlers

import (
	"database/sql"
	"encoding/json"
	"net/http"
	"strconv"
	"time"

	"github.com/golang-jwt/jwt/v5"
	"golang.org/x/crypto/bcrypt"

	"dds_lab2-api/db"
	"dds_lab2-api/middleware"
	"dds_lab2-api/models"
	"dds_lab2-api/utils"
)

//	@Summary		Register a new user
//	@Description	Register a new user with name, email, and password
//	@Tags			users
//	@Accept			json
//	@Produce		json
//	@Param			user	body		models.User	true	"User object to register"
//	@Success		201		{object}	map[string]string
//	@Failure		400		{object}	map[string]string	"Invalid input"
//	@Failure		409		{object}	map[string]string	"User with this email already exists"
//	@Failure		500		{object}	map[string]string	"Internal server error"
//	@Router			/api/register [post]
func RegisterUser(w http.ResponseWriter, r *http.Request) {
    w.Header().Set("Content-Type", "application/json")

    var user models.User
    err := json.NewDecoder(r.Body).Decode(&user)
    if err != nil || user.Email == "" || user.PasswordHash == "" {
        http.Error(w, `{"error": "invalid_input", "message": "Name, email, and password are required"}`, http.StatusBadRequest)
        return
    }

    if !utils.IsValidEmail(user.Email) {
        http.Error(w, `{"error": "invalid_email", "message": "Invalid email format"}`, http.StatusBadRequest)
        return
    }

    if len(user.PasswordHash) < 6 {
        http.Error(w, `{"error": "weak_password", "message": "Password must be at least 6 characters long"}`, http.StatusBadRequest)
        return
    }

    var exists bool
    err = db.DB.QueryRow("SELECT EXISTS(SELECT 1 FROM users WHERE email = $1)", user.Email).Scan(&exists)
    if err != nil {
        http.Error(w, `{"error": "database_error", "message": "Failed to check email uniqueness"}`, http.StatusInternalServerError)
        return
    }
    if exists {
        http.Error(w, `{"error": "email_exists", "message": "User with this email already exists"}`, http.StatusConflict)
        return
    }

    hashedPassword, err := bcrypt.GenerateFromPassword([]byte(user.PasswordHash), bcrypt.DefaultCost)
    if err != nil {
        http.Error(w, `{"error": "hashing_error", "message": "Failed to hash password"}`, http.StatusInternalServerError)
        return
    }

    _, err = db.DB.Exec(
        "INSERT INTO users (name, email, password_hash) VALUES ($1, $2, $3)",
        user.Name, user.Email, string(hashedPassword),
    )
    if err != nil {
        http.Error(w, `{"error": "database_error", "message": "Failed to create user"}`, http.StatusInternalServerError)
        return
    }

    w.WriteHeader(http.StatusCreated)
    json.NewEncoder(w).Encode(map[string]string{"message": "User registered successfully"})
}

//	@Summary		Authenticate a user
//	@Description	Authenticate a user by email and password
//	@Tags			users
//	@Accept			json
//	@Produce		json
//	@Param			credentials	body		models.Credentials	true	"User credentials"
//	@Success		200			{object}	map[string]string
//	@Failure		400			{object}	map[string]string	"Invalid input"
//	@Failure		401			{object}	map[string]string	"Invalid credentials"
//	@Failure		500			{object}	map[string]string	"Internal server error"
//	@Router			/api/login [post]
func LoginUser(w http.ResponseWriter, r *http.Request) {
    w.Header().Set("Content-Type", "application/json")

    var credentials models.Credentials
    err := json.NewDecoder(r.Body).Decode(&credentials)
    if err != nil || credentials.Email == "" || credentials.Password == "" {
        http.Error(w, `{"error": "invalid_input", "message": "Email and password are required"}`, http.StatusBadRequest)
        return
    }

    var storedHash string
    err = db.DB.QueryRow("SELECT password_hash FROM users WHERE email = $1", credentials.Email).Scan(&storedHash)
    if err == sql.ErrNoRows {
        http.Error(w, `{"error": "user_not_found", "message": "User not found"}`, http.StatusUnauthorized)
        return
    } else if err != nil {
        http.Error(w, `{"error": "database_error", "message": "Failed to fetch user data"}`, http.StatusInternalServerError)
        return
    }

    err = bcrypt.CompareHashAndPassword([]byte(storedHash), []byte(credentials.Password))
    if err != nil {
        http.Error(w, `{"error": "invalid_credentials", "message": "Invalid email or password"}`, http.StatusUnauthorized)
        return
    }

    expirationTime := time.Now().Add(24 * time.Hour)
    claims := &jwt.RegisteredClaims{
        Subject:   credentials.Email,
        ExpiresAt: jwt.NewNumericDate(expirationTime),
    }

    token := jwt.NewWithClaims(jwt.SigningMethodHS256, claims)
    tokenString, err := token.SignedString(middleware.GetJwtKey())
    if err != nil {
        http.Error(w, `{"error": "token_generation_error", "message": "Failed to generate token"}`, http.StatusInternalServerError)
        return
    }

    json.NewEncoder(w).Encode(map[string]string{
        "message": "Login successful",
        "token":   tokenString,
    })
}

//	@Summary		Get a user by email
//	@Description	Get a single user by their email
//	@Tags			users
//	@Accept			json
//	@Produce		json
//	@Param			email	query		string	true	"User email"
//	@Success		200		{object}	models.User
//	@Failure		400		{object}	map[string]string	"Email is required"
//	@Failure		404		{object}	map[string]string	"User not found"
//	@Router			/api/users/email [get]
func GetUserByEmail(w http.ResponseWriter, r *http.Request) {
    w.Header().Set("Content-Type", "application/json")

    email := r.URL.Query().Get("email")
    if email == "" {
        http.Error(w, `{"error": "missing_email", "message": "Email parameter is required"}`, http.StatusBadRequest)
        return
    }

    var user models.User
    err := db.DB.QueryRow("SELECT id, name, email FROM users WHERE email = $1", email).Scan(&user.ID, &user.Name, &user.Email)
    if err == sql.ErrNoRows {
        http.Error(w, `{"error": "user_not_found", "message": "User not found"}`, http.StatusNotFound)
        return
    } else if err != nil {
        http.Error(w, `{"error": "database_error", "message": "Failed to fetch user data"}`, http.StatusInternalServerError)
        return
    }

    json.NewEncoder(w).Encode(user)
}

//	@Summary		Get a user by ID
//	@Description	Get a single user by its ID
//	@Tags			users
//	@Accept			json
//	@Produce		json
//	@Param			id	query		int	true	"User ID"
//	@Success		200	{object}	models.User
//	@Failure		400	{object}	map[string]string	"Invalid user ID"
//	@Failure		404	{object}	map[string]string	"User not found"
//	@Router			/api/users/id [get]
func GetUserById(w http.ResponseWriter, r *http.Request) {
    w.Header().Set("Content-Type", "application/json")

    idStr := r.URL.Query().Get("id")
    id, err := strconv.Atoi(idStr)
    if err != nil {
        http.Error(w, `{"error": "invalid_id", "message": "ID must be a valid integer"}`, http.StatusBadRequest)
        return
    }

    var user models.User
    err = db.DB.QueryRow("SELECT id, name, email FROM users WHERE id = $1", id).Scan(&user.ID, &user.Name, &user.Email)
    if err == sql.ErrNoRows {
        http.Error(w, `{"error": "user_not_found", "message": "User not found"}`, http.StatusNotFound)
        return
    } else if err != nil {
        http.Error(w, `{"error": "database_error", "message": "Failed to fetch user data"}`, http.StatusInternalServerError)
        return
    }

    json.NewEncoder(w).Encode(user)
}