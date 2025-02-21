#!/bin/bash

# Создаем сеть, если она еще не существует
docker network create support-network || true

# Собираем проект
echo "Сборка проекта Maven..."
./mvnw clean package -DskipTests

# Запускаем базу данных
echo "Запуск PostgreSQL..."
docker compose --env-file .env \
  up -d support_db

# Ждем готовности PostgreSQL
./scripts/wait-for-it.sh localhost:${POSTGRESDB_LOCAL_PORT} --timeout=60 --strict -- echo "PostgreSQL готов"

# Ждем готовности PgAdmin
./scripts/wait-for-it.sh localhost:5050 --timeout=60 --strict -- echo "PgAdmin готов"

# Запускаем основное приложение
echo "Запуск Spring Boot приложения..."
docker compose --env-file .env \
  up --build -d bot-service

echo "Все сервисы успешно запущены!"