FROM postgres:16.8-alpine3.20

COPY init.sh /docker-entrypoint-initdb.d/
RUN chmod +x /docker-entrypoint-initdb.d/init.sh

ENV TZ=Europe/Moscow
ENV PGTZ=Europe/Moscow

ENV PGPORT=5432

# Значение должно совпадать с значением PGPORT
EXPOSE 5432