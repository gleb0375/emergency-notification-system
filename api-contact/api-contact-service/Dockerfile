FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY target/api-contact-service-0.0.1-SNAPSHOT.jar api-contact-service.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "api-contact-service.jar"]