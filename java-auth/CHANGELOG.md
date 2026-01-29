# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## 1.0.2 - 2026-01-29
### Fixed
- Fixed 403 Forbidden error when accessing Swagger UI through `HomeController` by adding `/swagger-ui.html` to permitted request matchers in `SecurityConfig`.

## 1.0.1 - 2026-01-29
### Fixed
- Fixed `bootRun` failure by updating `application.yml` default datasource URL to `localhost`.
- Updated `docker-compose.yml` to explicitly set `SPRING_DATASOURCE_URL` and `SPRING_DATA_REDIS_HOST` for Docker networking.

## 1.0.0 - 2026-01-28
### Added
- Initial project setup with Java 21, Spring Boot 3.3.x.
- Implemented basic `HomeController` to redirect to Swagger UI.
### Changed
- Updated JJWT to version 0.13.0.

