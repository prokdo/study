FROM node:alpine3.21 as builder

WORKDIR /app

COPY . .

RUN npm ci
RUN npm run build

FROM nginx:alpine3.21-slim

ENV PORT=3000

ENV TZ=Europe/Moscow

RUN apk add --no-cache tzdata

WORKDIR /app

COPY --from=builder /app/build/ /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Значение должно совпадать с PORT
EXPOSE 3000