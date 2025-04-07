package postgres

import (
	"context"
	"fmt"
	"log"
	"time"

	"github.com/jmoiron/sqlx"

	"dds_lab2-backend/internal/config"

	_ "github.com/lib/pq"
)

func New(cfg config.DBConfig) (*sqlx.DB, error) {
	connStr := fmt.Sprintf(
		"host=%s port=%s user=%s password=%s dbname=%s sslmode=%s",
		cfg.Host, cfg.Port, cfg.User,
		cfg.Password, cfg.Name, cfg.SSLMode,
	)

	db, err := sqlx.Open("postgres", connStr)
	if err != nil {
		return nil, err
	}

	db.SetMaxOpenConns(cfg.MaxOpenConnections)
	db.SetMaxIdleConns(cfg.MaxIdleConnections)
	db.SetConnMaxLifetime(cfg.MaxConnectionLifetime)

	ctx, cancel := context.WithTimeout(context.Background(), cfg.ConnectionTimeout)
	defer cancel()

	for i := range cfg.ConnectionAttempts {
		if err := db.PingContext(ctx); err == nil {
			log.Printf("connected to DB at %s:%s", cfg.Host, cfg.Port)
			return db, nil
		}

		log.Printf("connection attempt %d to %s:%s failed: %v", i+1, cfg.Host, cfg.Port, err)
		time.Sleep(cfg.ConnectionTimeout)
	}

	return nil, fmt.Errorf("connection to %s:%s failed after %d attempts", cfg.Host, cfg.Port, cfg.ConnectionAttempts)
}
