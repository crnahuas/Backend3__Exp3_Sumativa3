# syntax=docker/dockerfile:1.7
FROM maven:3.9.11-eclipse-temurin-17 AS build

ARG SERVICE
WORKDIR /workspace
COPY ${SERVICE}/pom.xml pom.xml
RUN --mount=type=cache,id=maven-cache,target=/root/.m2,sharing=locked \
    mvn -B -DskipTests dependency:go-offline
COPY ${SERVICE}/src src
RUN --mount=type=cache,id=maven-cache,target=/root/.m2,sharing=locked \
    mvn -B -DskipTests package

FROM eclipse-temurin:17-jre-jammy
RUN groupadd --system spring \
    && useradd --system --gid spring spring \
    && apt-get update \
    && apt-get install -y --no-install-recommends curl \
    && rm -rf /var/lib/apt/lists/*
WORKDIR /app
COPY --from=build /workspace/target/*.jar app.jar
USER spring:spring
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
