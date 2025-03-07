FROM mongo:8.0.5-noble

RUN mkdir -p /docker-entrypoint-initdb.d

COPY ./init-scripts/* /docker-entrypoint-initdb.d/
COPY ./config/mongod.conf /etc/mongod.conf

RUN chmod +x /docker-entrypoint-initdb.d/*.js

ENV MONGO_INITDB_ROOT_USERNAME=root
ENV MONGO_INITDB_ROOT_PASSWORD=root
ENV MONGO_INITDB_DATABASE=dds_lab2-db
ENV MONGO_APP_USER=prokdo
ENV MONGO_APP_PASSWORD=1001
ENV MONGO_INIT_MODERATOR_EMAIL=moderator1@moderator.com
ENV MONGO_INIT_MODERATOR_PASSWORD_HASH='$2a$12$I5VTs0kbpzMZAVpFI3pXH.JUNBEvGSia6jcAvVVrvyk6CngAOczta'

EXPOSE 27017

CMD ["mongod", "--config", "/etc/mongod.conf"]
