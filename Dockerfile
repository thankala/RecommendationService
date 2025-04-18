# ---- Stage 1: Build JAR with Maven ----
FROM maven:3.9.9-eclipse-temurin-17-alpine AS builder

WORKDIR /build
COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim

WORKDIR /app
COPY --from=builder /build/target/RecommendationService-*.jar app.jar
COPY --from=builder /build/target/classes/prices prices/

ENV CRYPTO_PRICES_FOLDER_PATH=/app

ENTRYPOINT ["java", "-jar", "app.jar"]
    