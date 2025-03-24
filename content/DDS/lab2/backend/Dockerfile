FROM alpine:3.21.3

ENV PORT=8080
ENV GIN_MODE=release

ENV TZ=Europe/Moscow

RUN apk add --no-cache libc6-compat
RUN apk add --no-cache tzdata

WORKDIR /server

COPY build/dds_lab2-backend /server

CMD ["./dds_lab2-backend"]