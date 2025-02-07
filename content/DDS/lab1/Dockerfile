FROM node:current-alpine3.20

RUN npm install -g serve

WORKDIR /app
COPY build /app

CMD ["serve", "-s"]

