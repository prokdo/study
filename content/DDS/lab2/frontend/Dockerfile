FROM node:iron-alpine3.21

RUN npm install -g serve

WORKDIR /frontend
COPY /dist /frontend

CMD ["serve", "-s"]