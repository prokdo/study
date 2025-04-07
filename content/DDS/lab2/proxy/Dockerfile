FROM nginx:alpine3.21-slim

COPY nginx.conf /etc/nginx/conf.d/default.conf

RUN apk add --no-cache tzdata

ENV TZ=Europe/Moscow

EXPOSE 80