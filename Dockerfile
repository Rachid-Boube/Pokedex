FROM maven:3.9.9-amazoncorretto

WORKDIR /app

COPY . .

RUN mvn clean package

FROM openjdk:17-jdk-slim

WORKDIR /app

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} /app/pokemon.jar

ENTRYPOINT ["java","-jar","/app/pokemon.jar"]