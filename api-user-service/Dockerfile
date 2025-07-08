FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY target/api-user-service-0.0.1-SNAPSHOT.jar api-user-service.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "api-user-service.jar"]