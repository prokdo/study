package handlers

import (
	"encoding/json"
	"net/http"

	"dds_lab2-api/db"
)

//	@Summary		Check if a user exists by email
//	@Description	Check if a user with the given email exists
//	@Tags			auth
//	@Accept			json
//	@Produce		json
//	@Param			email	query		string				true	"User email"
//	@Success		200		{object}	map[string]string	"User exists"
//	@Failure		400		{object}	map[string]string	"Email is required"
//	@Failure		404		{object}	map[string]string	"User not found"
//	@Router			/api/check-email [get]
func CheckUserExistsByEmail(w http.ResponseWriter, r *http.Request) {
    w.Header().Set("Content-Type", "application/json")

    email := r.URL.Query().Get("email")
    if email == "" {
        http.Error(w, `{"error": "missing_email", "message": "Email parameter is required"}`, http.StatusBadRequest)
        return
    }

    var exists bool
    err := db.DB.QueryRow("SELECT EXISTS(SELECT 1 FROM users WHERE email = $1)", email).Scan(&exists)
    if err != nil {
        http.Error(w, `{"error": "database_error", "message": "Failed to check user existence"}`, http.StatusInternalServerError)
        return
    }

    if !exists {
        http.Error(w, `{"error": "user_not_found", "message": "User not found"}`, http.StatusNotFound)
        return
    }

    json.NewEncoder(w).Encode(map[string]string{"message": "User exists"})
}