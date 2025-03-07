package db

import (
	"database/sql"
	"fmt"
	"log"
	"os"

	_ "github.com/lib/pq"
)

var DB *sql.DB

func InitDB() {
    host := os.Getenv("DB_HOST")
    port := os.Getenv("DB_PORT")
    user := os.Getenv("DB_USER")
    password := os.Getenv("DB_PASSWORD")
    dbname := os.Getenv("DB_NAME")

    var connStr string

    if host == "" || port == "" || user == "" || password == "" || dbname == "" {
        connStr = "host=127.0.0.1 port=5432 user=prokdo password=1001 dbname=dds_lab2 sslmode=disable"
    } else {
        connStr = fmt.Sprintf(
            "host=%s port=%s user=%s password=%s dbname=%s sslmode=enable",
            host, port, user, password, dbname)
    }

    var err error
	DB, err = sql.Open("postgres", connStr)
    if err != nil {
        log.Fatal(err)
    }

    err = DB.Ping()
    if err != nil {
        log.Fatal(err)
    }

    fmt.Println("Connected to the database")
}