# Build stage
FROM maven:3.9.2-eclipse-temurin-20-alpine AS build
LABEL authors="hos6"
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests


ARG JAR_FILE
RUN JAR_FILE=$(ls /app/target/*.jar) && echo "JAR_FILE=${JAR_FILE}" > .env
RUN echo "LOG_PATH=./logs" >> .env

# Run stage
FROM openjdk:20-jdk-slim AS run
WORKDIR /app

# Copy jar from build stage
COPY --from=build /app/target/${JAR_FILE} .

# Create a directory for logs and mount as a volume
VOLUME [ "/app/logs" ]

# Security configuration
RUN groupadd -r appuser && useradd -r -g appuser appuser
USER appuser

# Expose application port
EXPOSE 8080

# Command to start the application
CMD ["java", "-jar", "${JAR_FILE}"]