FROM maven:3.8.4-openjdk-17-slim AS builder

COPY . /app

WORKDIR /app

RUN mvn clean package

FROM openjdk:17-jdk-alpine

COPY --from=builder app/target/dt24-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]