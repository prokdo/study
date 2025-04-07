FROM golang:1.24.2-alpine3.21 as builder

WORKDIR /app

COPY . .

RUN go mod download
RUN go build -o build/dds_lab2-backend cmd/server/main.go

FROM alpine:3.21.3

ENV PORT=8080
ENV GIN_MODE=release

ENV TZ=Europe/Moscow

RUN apk add --no-cache libc6-compat
RUN apk add --no-cache tzdata

WORKDIR /app

COPY --from=builder /app/build/dds_lab2-backend .

# Значение должно совпадать с PORT
EXPOSE 8080

CMD ["./dds_lab2-backend"]