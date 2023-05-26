# Build stage
FROM maven:3.9.2-eclipse-temurin-20-alpine AS build
LABEL authors="hos6"
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Run stage
FROM openjdk:20-jdk-slim AS run
WORKDIR /app
COPY --from=build /app/target/urlShortener-0.0.1-SNAPSHOT.jar .

# Security configuration
RUN groupadd -r appuser && useradd -r -g appuser appuser
USER appuser

# Expose application port
EXPOSE 8080

# Command to start the application
CMD ["java", "-jar", "urlShortener-0.0.1-SNAPSHOT.jar"]