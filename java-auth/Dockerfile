# Build stage
FROM gradle:8.8.0-jdk21-jammy AS build
WORKDIR /home/gradle/project
COPY --chown=gradle:gradle . .
RUN gradle build --no-daemon

# Package stage
FROM openjdk:21-jre-slim
WORKDIR /app
COPY --from=build /home/gradle/project/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]