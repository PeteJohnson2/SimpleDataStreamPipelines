#!/bin/sh
docker pull postgres:latest
docker run --name datastream-postgres -e POSTGRES_PASSWORD=sven1 -e POSTGRES_USER=sven1 -e POSTGRES_DB=datastream -p 5432:5432 -d postgres -c wal_level=logical

# docker start datastream-postgres
# docker stop datastream-postgres
