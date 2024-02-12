#!/bin/bash

# Microservicio: eureka-service
cd eureka-service
mvn clean package
docker build -t eureka:latest .
docker tag eureka:latest gjlobos/digital-money-back:eureka-latest
docker push gjlobos/digital-money-back:eureka-latest
cd ..

# Microservicio: gateway-service
cd gateway-service
mvn clean package
docker build -t gateway:latest .
docker tag gateway:latest gjlobos/digital-money-back:gateway-latest
docker push gjlobos/digital-money-back:gateway-latest
cd ..

# Microservicio: user-service
cd user-service
mvn clean package
docker build -t user:latest .
docker tag user:latest gjlobos/digital-money-back:user-latest
docker push gjlobos/digital-money-back:user-latest
cd ..

# Microservicio: account-service
cd account-service
mvn clean package
docker build -t account:latest .
docker tag account:latest gjlobos/digital-money-back:account-latest
docker push gjlobos/digital-money-back:account-latest
cd ..

# Microservicio: card-service
cd card-service
mvn clean package
docker build -t card:latest .
docker tag card:latest gjlobos/digital-money-back:card-latest
docker push gjlobos/digital-money-back:card-latest
cd ..

# Microservicio: transaction-service
cd transaction-service
mvn clean package
docker build -t transaction:latest .
docker tag transaction:latest gjlobos/digital-money-back:transaction-latest
docker push gjlobos/digital-money-back:transaction-latest
cd ..
