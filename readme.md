# URL Shortener Application Deployment Guide

This guide outlines the steps to deploy the URL Shortener application using Docker and Docker Compose.

## Prerequisites

- Docker and Docker Compose installed on your host machine.
- The URL Shortener application codebase.

## Environment Configuration

Create an `.env` file in the root directory of the project to set up your environment variables. The `.env` file should look like this:

```sh
# MongoDB Configuration
#MongoDB Configuration
MONGO_INITDB_ROOT_USERNAME=admin
MONGO_INITDB_ROOT_PASSWORD=your_mongodb_password

# Redis Configuration
REDIS_PASSWORD=your_redis_password

# Application Configuration
SPRING_DATA_MONGODB_URI=mongodb://admin:your_mongodb_password@mongodb:27017/urlShortener?authSource=admin
SPRING_DATA_REDIS_HOST=redis
SPRING_DATA_REDIS_PASSWORD=your_redis_password

# Docker build arguments
JAR_FILE=urlShortener-0.0.1-SNAPSHOT.jar

mongo_container_name=mongodb-urlshortener
redis_container_name=redis-urlshortener
redis_container_name=backend-urlshortener
```

Please replace `your_mongodb_password` and `your_redis_password` with your chosen MongoDB and Redis passwords. Update the `JAR_FILE` variable if the JAR filename changes (e.g., if the application version is updated).

## Build and Run

With the `.env` file set up, you can build and run the application. Run the following commands in the root directory of the project:

```sh
docker-compose build
docker-compose up
```

The `build` command will build the Docker images for the URL Shortener application, MongoDB, and Redis. The `up` command will start all three services.

## Accessing the Application

Once all services are running, the URL Shortener application will be accessible at `http://localhost:8080`.

## Shutting Down the Application

To stop running services:

```sh
docker-compose down
```

This command stops all running services defined in `docker-compose.yml`.

## Important Note

For security reasons, never commit the `.env` file to your version control system. Add `.env` to your `.gitignore` file to prevent accidental commits.
