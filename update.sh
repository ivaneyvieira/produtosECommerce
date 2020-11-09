#!/usr/bin/env bash

git pull
./gradlew clean vaadinPrepareFrontend build

docker-compose down
docker-compose up -d
