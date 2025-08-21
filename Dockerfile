# Stage 1: Build the application
#FROM maven:3.8.4-openjdk-17 AS build
# Stage 1: Build the application (pakai Alpine biar ringan)
FROM maven:3.8.4-eclipse-temurin-17-alpine AS build
# Set the working directory
WORKDIR /app

# Copy the pom.xml file
COPY pom.xml .

# Copy the source code
COPY src ./src

# Build the application
# RUN mvn clean package -DskipTests
# Build (batasi memory Maven biar gak makan RAM VPS)
RUN mvn clean package -DskipTests -T 1C -Dmaven.compiler.fork=false

# Stage 2: Run the application
#FROM openjdk:17-jdk-slim
FROM eclipse-temurin:17-jre-alpine

# Set the working directory
WORKDIR /app

# Set timezone
#ENV TZ=Asia/Jakarta
#RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
ENV TZ=Asia/Jakarta
RUN apk --no-cache add tzdata && \
    cp /usr/share/zoneinfo/$TZ /etc/localtime && \
    echo $TZ > /etc/timezone && \
    apk del tzdata

# App JAR
COPY --from=build /app/target/*.jar app.jar

# Environment variables (can be overridden by Docker Compose or GitHub Secrets)
ENV SPRING_PROFILES_ACTIVE=prod

#ENTRYPOINT ["java", "-jar", "app.jar"]
ENTRYPOINT ["java", "-Xmx512m", "-Xms256m", "-jar", "app.jar"]
