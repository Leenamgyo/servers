Role: Senior DevOps & Backend Developer
Task: Create a production-ready Authentication Server using Java 17+, Spring Boot 3.x, and Docker.

Requirements:

1. **Tech Stack**:
   - Java 21
   - Spring Boot 3.3.x
   - Spring Security 6 (Strictly use `SecurityFilterChain`, NO `WebSecurityConfigurerAdapter`)
   - Spring Data JPA
   - Redis (for Refresh Token storage)
   - MySQL 8.0
   - JJWT (io.jsonwebtoken)
   - Gradle (Kotlin DSL preferred)

2. **Application Features**:
   - **JWT Auth**: Access Token (Short-lived) + Refresh Token (Long-lived, stored in Redis).
   - **Endpoints**: `/auth/signup`, `/auth/login`, `/auth/reissue` (rotate tokens), `/auth/logout`.
   - **Security**: Stateless session management, BCrypt password encoding.

3. **DevOps & Infrastructure (Crucial)**:
   - **Dockerfile**: Create a `Dockerfile` for the Spring Boot application. Use a **multi-stage build** approach (Grade wrapper build -> JRE runtime) to minimize image size.
   - **Docker Compose**: Create a `docker-compose.yml` file that orchestrates:
     - `mysql-db`: MySQL 8.0 container (include environment variables for root password and database creation).
     - `redis-cache`: Redis alpine container.
     - `auth-app`: The Spring Boot application container (depends on mysql and redis).
   - **Configuration**: Show the `application.yml` settings configured to connect to the docker service names (e.g., use `host: mysql-db` instead of `localhost`).

4. **Deliverables**:
   - `build.gradle.kts` dependencies.
   - Core Java Code: `SecurityConfig.java`, `JwtProvider.java`, `AuthController.java`, `MemberService.java`.
   - Infrastructure Code: `Dockerfile`, `docker-compose.yml`.
   - Configuration: `application.yml`.