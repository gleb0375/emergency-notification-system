FROM eclipse-temurin:17-jre-alpine

ARG JAR_FILE=target/api-auth-service-*.jar

COPY ${JAR_FILE} app.jar

EXPOSE 8082

ENTRYPOINT ["java","-jar","/app.jar"]
