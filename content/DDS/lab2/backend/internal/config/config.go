package config

import (
	"fmt"
	"log"
	"os"
	"strconv"
	"strings"
	"time"

	"github.com/joho/godotenv"

	"dds_lab2-backend/internal/types"
)

type DBConfig struct {
	Host                  string
	Port                  string
	Name                  string
	User                  string
	Password              string
	SSLMode               string
	ConnectionTimeout     time.Duration
	MaxOpenConnections    int
	MaxIdleConnections    int
	MaxConnectionLifetime time.Duration
	ConnectionAttempts    int
}

type JWTConfig struct {
	AccessSecret  string
	RefreshSecret string
	AccessTTL     time.Duration
	RefreshTTL    time.Duration
}

type BcryptConfig struct {
	SaltRounds int
}

type ServerConfig struct {
	Port    string
	GinMode string
}

type CORSConfig struct {
	AllowedOrigins   []string
	AllowedMethods   []string
	AllowedHeaders   []string
	ExposedHeaders   []string
	AllowCredentials bool
	MaxAge           time.Duration
}

type Config struct {
	DB     DBConfig
	JWT    JWTConfig
	Bcrypt BcryptConfig
	Server ServerConfig
	CORS   CORSConfig
}

func Load(filenames ...string) (*Config, error) {
	var err error
	for _, filename := range filenames {
		err = godotenv.Load(filename)
	}

	if err != nil {
		log.Println("couldn't load .env file(s), relying on system environment")
	}

	var cfg Config
	var envValue string

	if cfg.DB.Host, err = getEnv("DB_HOST"); err != nil {
		return nil, err
	}

	if cfg.DB.Port, err = getEnv("DB_PORT"); err != nil {
		return nil, err
	}

	if _, err = strconv.Atoi(cfg.DB.Port); err != nil {
		return nil, err
	}

	if cfg.DB.Name, err = getEnv("DB_NAME"); err != nil {
		return nil, err
	}

	if cfg.DB.User, err = getEnv("DB_USER"); err != nil {
		return nil, err
	}

	if cfg.DB.Password, err = getEnv("DB_PASSWORD"); err != nil {
		return nil, err
	}

	if cfg.DB.SSLMode, err = getEnv("DB_SSL_MODE"); err != nil {
		return nil, err
	}

	if envValue, err = getEnv("DB_CONNECTION_TIMEOUT"); err != nil {
		return nil, err
	}

	if cfg.DB.ConnectionTimeout, err = parseDuration(envValue); err != nil {
		return nil, err
	}

	if envValue, err = getEnv("DB_MAX_OPEN_CONNECTIONS"); err == nil {
		cfg.DB.MaxOpenConnections, err = strconv.Atoi(envValue)
		if err != nil {
			return nil, err
		}
	} else {
		return nil, err
	}

	if envValue, err = getEnv("DB_MAX_IDLE_CONNECTIONS"); err == nil {
		cfg.DB.MaxIdleConnections, err = strconv.Atoi(envValue)
		if err != nil {
			return nil, err
		}
	} else {
		return nil, err
	}

	if envValue, err = getEnv("DB_MAX_CONNECTION_LIFETIME"); err != nil {
		return nil, err
	}

	if cfg.DB.MaxConnectionLifetime, err = parseDuration(envValue); err != nil {
		return nil, err
	}

	if envValue, err = getEnv("DB_CONNECTION_ATTEMPTS"); err != nil {
		return nil, err
	}

	if cfg.DB.ConnectionAttempts, err = strconv.Atoi(envValue); err != nil {
		return nil, err
	}

	if cfg.JWT.AccessSecret, err = getEnv("JWT_ACCESS_SECRET"); err != nil {
		return nil, err
	}

	if cfg.JWT.RefreshSecret, err = getEnv("JWT_REFRESH_SECRET"); err != nil {
		return nil, err
	}

	if envValue, err = getEnv("JWT_ACCESS_TTL"); err != nil {
		return nil, err
	}

	if cfg.JWT.AccessTTL, err = parseDuration(envValue); err != nil {
		return nil, err
	}

	if envValue, err = getEnv("JWT_REFRESH_TTL"); err != nil {
		return nil, err
	}

	if cfg.JWT.RefreshTTL, err = parseDuration(envValue); err != nil {
		return nil, err
	}

	saltRounds, err := getEnv("BCRYPT_SALT_ROUNDS")
	if err != nil {
		return nil, err
	}

	if cfg.Bcrypt.SaltRounds, err = strconv.Atoi(saltRounds); err != nil {
		return nil, err
	}

	if envValue, err = getEnv("CORS_ALLOWED_ORIGINS"); err == nil {
		cfg.CORS.AllowedOrigins = strings.Split(envValue, ",")
	} else {
		return nil, err
	}

	if envValue, err = getEnv("CORS_ALLOWED_METHODS"); err == nil {
		cfg.CORS.AllowedMethods = strings.Split(envValue, ",")
	} else {
		return nil, err
	}

	if envValue, err = getEnv("CORS_ALLOWED_HEADERS"); err == nil {
		cfg.CORS.AllowedHeaders = strings.Split(envValue, ",")
	} else {
		return nil, err
	}

	if envValue, err = getEnv("CORS_EXPOSED_HEADERS"); err == nil {
		cfg.CORS.ExposedHeaders = strings.Split(envValue, ",")
	} else {
		return nil, err
	}

	if envValue, err = getEnv("CORS_ALLOW_CREDENTIALS"); err == nil {
		cfg.CORS.AllowCredentials, err = strconv.ParseBool(envValue)
		if err != nil {
			return nil, err
		}
	} else {
		return nil, err
	}

	if envValue, err = getEnv("CORS_MAX_AGE"); err != nil {
		return nil, err
	}

	if cfg.CORS.MaxAge, err = parseDuration(envValue); err != nil {
		return nil, err
	}

	if cfg.Server.Port, err = getEnv("PORT"); err != nil {
		return nil, err
	}

	if _, err = strconv.Atoi(cfg.Server.Port); err != nil {
		return nil, err
	}

	if envValue, err = getEnv("GIN_MODE"); err != nil {
		return nil, err
	}

	var ginMode types.GinMode
	if ginMode, err = types.ParseGinMode(envValue); err != nil {
		return nil, err
	}

	cfg.Server.GinMode = ginMode.String()

	return &cfg, nil
}

func getEnv(key string) (string, error) {
	value := os.Getenv(key)
	if value == "" {
		return "", fmt.Errorf("environment variable %s is required", key)
	}
	return value, nil
}

func parseDuration(value string) (time.Duration, error) {
	duration, err := time.ParseDuration(value)
	if err != nil {
		return 0, fmt.Errorf("invalid duration format for %s", value)
	}
	return duration, nil
}
