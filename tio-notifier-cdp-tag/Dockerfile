### Step 1 - Build
FROM maven:3.9.6-eclipse-temurin-21 AS BUILD
ARG ACCESS_TOKEN

WORKDIR /src
COPY . /src
RUN mvn -DskipTests install -Dmaven.repo.local=.m2/repository

#### Step 2 - Entrypoint
FROM openjdk:21-rc-jdk-oracle
WORKDIR /app
COPY --from=BUILD --chown=1001:root /src/target/*.jar /app/app.jar
COPY --from=BUILD --chown=1001:root /src/open-telemetry/opentelemetry-javaagent.jar /app
RUN chown 1001 /app
EXPOSE 8080
ENV TZ="America/Sao_Paulo"
# Rootless
USER 1001
ENTRYPOINT ["java", "-jar", "/app/app.jar"]