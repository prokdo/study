FROM postgres:17.2

COPY init.sql /docker-entrypoint-initdb.d/init.sql

ENV POSTGRES_USER=prokdo
ENV POSTGRES_PASSWORD=1001
ENV POSTGRES_DB=dds_lab2

EXPOSE 5432
