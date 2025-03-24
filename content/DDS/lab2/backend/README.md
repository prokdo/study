## Описание
Докер-образ для развёртывания backend-сервиса авторизации на Go:
- REST API с JWT-аутентификацией
- Взаимодействие с PostgreSQL

## Требования
- Docker
- Запущенный контейнер с PostgreSQL (см. [README](../db/README.md))
- Файл `.env` с переменными окружения

## Настройка переменных окружения
1. Создать `.env` файл со следующими переменными:
```env
DB_HOST=localhost                                           # Адрес хоста СУБД
DB_PORT=5432                                                # Порт хоста СУБД
DB_NAME=dds_lab2-db                                         # Имя рабочей БД
DB_USER=prokdo                                              # Пользователь БД
DB_PASSWORD=1001                                            # Пароль пользователя БД

DB_SSL_MODE=disable                                         # Режим работы SSL-шифрования соединения к БД
                                                            # (если сервер БД настроен на работу с SSL, безопаснее использовать enable)
DB_CONNECTION_TIMEOUT=10s                                   # Время ожидания подключения к БД
DB_MAX_OPEN_CONNECTIONS=25                                  # Максимум активных соединений с БД
DB_MAX_IDLE_CONNECTIONS=25                                  # Максимум соединений с БД в простое
DB_MAX_CONNECTION_LIFETIME=5m                               # Максимальное время жизни соединения с БД
DB_CONNECTIONS_ATTEMPTS=5                                   # Количество попыток соединения с БД
JWT_ACCESS_SECRET=DEV_ACCESS                                # Секрет для JWT Access токена
JWT_REFRESH_SECRET=DEV_REFRESH                              # Секрет для JWT Refresh токена
JWT_ACCESS_TTL=15m                                          # Время жизни JWT Access токена
JWT_REFRESH_TTL=720h                                        # Время жизни JWT Refresh токена
BCRYPT_SALT_ROUNDS=12                                       # Количество раундов Bcrypt-хеширования
CORS_ALLOWED_ORIGINS="http://localhost:8080"                # Разрешенные источники CORS, указать домен frontend
CORS_ALLOWED_METHODS="GET,POST,PUT,DELETE"                  # Разрешенные методы CORS
CORS_ALLOWED_HEADERS="Origin,Authorization,Content-Type"    # Разрешенные заголовки запросов CORS
CORS_EXPOSED_HEADERS="Content-Length"                       # Открытые для CORS заголовки
CORS_ALLOW_CREDENTIALS=true                                 # Разрешение на использование реквизитов в CORS, обязательно указать true
CORS_MAX_AGE=12h                                            # Время жизни preflight-запросов CORS
```
2. В файле `Doсkerfile` изменить следующие переменные (опционально):
```Dockerfile
PORT=8080                                                   # Рабочий порт сервиса
GIN_MODE=release                                            # Изменить на debug, если планируется разработка
TZ=Europe/Moscow                                            # Часовой пояс системы-контейнера
```

## Первичный запуск
1. Собрать Go-проект:
```bash
go build -o ./build/dds_lab2-backend ./cmd/server/main.go
```

2. Собрать образ:
```bash
docker build -t <имя_образа> .
```
3. Запустить полученный образ:
```bash
docker run -p <порт_хоста>:<порт_приложения>  --name <имя_контейнера> --env-file <путь_до_файла_.env> <имя_образа>
```

## Последующие запуски
Для последующих запусков достаточно использовать стандартную команду Docker `docker start`