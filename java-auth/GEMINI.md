# Authentication Server – Production Setup Guide (gemini.md)

Role: Senior DevOps & Backend Developer  
Task: Create a production-ready Authentication Server using Java 21, Spring Boot 3.3.x, and Docker.

## Requirements

### 1. Tech Stack
- Java 21
- Spring Boot 3.3.x
- Spring Security 6 (Strictly use `SecurityFilterChain`, NO `WebSecurityConfigurerAdapter`)
- Spring Data JPA
- Redis (for Refresh Token storage)
- MySQL 8.0
- JJWT (io.jsonwebtoken)
- Gradle (Kotlin DSL preferred)

### 2. Application Features
- **JWT Auth**: Access Token (Short-lived) + Refresh Token (Long-lived, stored in Redis).
- **Endpoints**: `/auth/signup`, `/auth/login`, `/auth/reissue` (rotate tokens), `/auth/logout`.
- **Security**: Stateless session management, BCrypt password encoding.

### 3. DevOps & Infrastructure
- **Dockerfile**: Create a `Dockerfile` using a **multi-stage build** (Gradle wrapper build -> JRE runtime) to minimize image size.
- **Docker Compose**:
  - Orchestrate `mysql-db`, `redis-cache`, and `auth-app`.
  - Use proper `depends_on` and network aliases.
  - ✅ **추가 요구사항**: `mysql`과 `redis`는 `docker-compose.infra.yml`로 작성한다.
- **Configuration**: Ensure `application.yml` uses environment variables or docker service names (e.g., `host: mysql-db`) for connectivity.

### 4. Versioning & Documentation (Mandatory)
- **CHANGELOG.md**: Create a `CHANGELOG.md` file following the "Keep a Changelog" format.
- **Version Control**: If I request a version update (or if this is the initial setup), you MUST:
  1. Explicitly state the version number in `build.gradle.kts` (e.g., `version = "1.0.0"`).
  2. Add a new entry in `CHANGELOG.md` under that version with the date and list of changes (Added, Changed, Fixed).

### 5. Deliverables
- `build.gradle.kts` (with explicit version).
- `CHANGELOG.md` (Initial release notes).
- Core Java Code: `SecurityConfig.java`, `JwtProvider.java`, `AuthController.java`, `MemberService.java`.
- Infrastructure: `Dockerfile`, `docker-compose.yml`.
- Configuration: `application.yml`.
