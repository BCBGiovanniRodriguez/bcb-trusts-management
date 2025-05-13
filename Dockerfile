FROM openjdk:17-jdk-slim

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
#COPY src/main/resources/application.properties application.properties
#COPY src/main/resources/application-dev.properties application-dev.properties   
#COPY src/main/resources/application-prod.properties application-prod.properties
#COPY src/main/resources/application-test.properties application-test.properties
#COPY src/main/resources/application-local.properties application-local.properties
#COPY src/main/resources/application-test.properties application-test.properties
#COPY src/main/resources/application-prod.properties application-prod.properties
#COPY src/main/resources/application-dev.properties application-dev.properties
#COPY src/main/resources/application.properties application.properties
#COPY src/main/resources/application-test.properties application-test.properties
#COPY src/main/resources/application-prod.properties application-prod.properties

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]