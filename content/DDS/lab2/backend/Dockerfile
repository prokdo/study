FROM alpine:3.21.3

ENV PORT=8080
ENV GIN_MODE=release

RUN apk add libc6-compat

WORKDIR /server

COPY build/dds_lab2-backend /server

CMD ["./dds_lab2-backend"]