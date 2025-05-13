# Etapa 1: Construccion del JAR
FROM maven:3.8.5-openjdk-21 AS build

WORKDIR /app

COPY pom.xml /

RUN mvn dependency:go-offline

COPY src ./src

RUN mvn clean package -DskipTests

RUN ls -la /app/target

# Etapa2: Ejecución del JAR
FROM openjdk:21-jdk-bookworm

WORKDIR /app

COPY --from=build /app/target/front-0.0.1-SNAPSHOT.jar /app/front.jar

EXPOSE 8080

ENTRYPOINT [ "java", "-jar", "/app/mijar.jar" ]