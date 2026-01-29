# Java Authentication Server

Production-ready Authentication Server using Java 21, Spring Boot 3.3.x, and Docker.

## Tech Stack
- **Language**: Java 21
- **Framework**: Spring Boot 3.3.x
- **Security**: Spring Security 6 (JWT)
- **Database**: MySQL 8.0
- **Cache**: Redis (Refresh Token storage)
- **Build Tool**: Gradle (Kotlin DSL)

## Local Development Setup

### 1. Prerequisites
- Docker & Docker Compose
- JDK 21 (optional if using Docker for app)

### 2. Infrastructure Setup (MySQL, Redis)
Run the infrastructure containers using the provided compose file:
```bash
docker-compose -f docker-compose.infra.yml up -d
```
This will start:
- **MySQL**: localhost:3306 (User: `user`, Password: `password`, DB: `authdb`)
- **Redis**: localhost:6379

### 3. Run Application

#### Using Gradle (Local)
Ensure you have the environment variables or the correct `application.yml` setup.
```bash
./gradlew bootRun
```

#### Using Docker Compose (Full Stack)
To run the entire stack including the application:
```bash
docker-compose up --build -d
```
The app will be available at `http://localhost:8080`.

## API Endpoints

### Authentication
- `POST /auth/signup`: User registration (Default role: `ROLE_USER`)
- `POST /auth/login`: User login, returns Access & Refresh tokens.
- `POST /auth/reissue`: Rotate tokens using a valid Refresh token.
- `POST /auth/logout`: Invalidate Refresh token in Redis.

### Testing Roles
- `GET /`: Redirects to Swagger UI (`/swagger-ui.html`)
- `GET /api/user`: Requires authentication (`ROLE_USER` or `ROLE_ADMIN`)
- `GET /api/admin`: Requires `ROLE_ADMIN` authority.

## Documentation
- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **Changelog**: See `CHANGELOG.md` for version history.
