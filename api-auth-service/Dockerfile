FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY target/api-auth-service-0.0.1-SNAPSHOT.jar api-auth-service.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "api-auth-service.jar"]