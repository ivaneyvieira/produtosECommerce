#!/usr/bin/env bash

git pull
./gradlew clean vaadinBuildFrontend build -Pvaadin.productionMode

docker-compose down
docker-compose up -d
date
