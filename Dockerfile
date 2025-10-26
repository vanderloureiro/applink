FROM gradle:8.10-jdk21 AS builder

RUN apt-get update && apt-get install -y curl && \
    curl -fsSL https://deb.nodesource.com/setup_20.x | bash - && \
    apt-get install -y nodejs

WORKDIR /app

COPY build.gradle.kts settings.gradle.kts gradlew ./
COPY gradle ./gradle
COPY src ./src
COPY front ./front

WORKDIR /app/front
RUN npm ci
RUN npm run generate

RUN mkdir -p /app/src/main/resources/static && \
    cp -r .output/public/* /app/src/main/resources/static/

WORKDIR /app
RUN ./gradlew bootJar

FROM eclipse-temurin:21-jdk-jammy

WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]