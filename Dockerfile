# Etapa 1: Construccion del JAR
FROM maven:3.9.9-eclipse-temurin-21 AS build

# Crear directorio de la aplicación
RUN mkdir /app

WORKDIR /app

COPY pom.xml /app
COPY jars /app/jars

RUN mvn clean 
RUN mvn install:install-file -Dfile=./jars/nunito-extension.jar -DgroupId=com.example -DartifactId=example -Dversion=0.0.1-SNAPSHOT -Dpackaging=pom
RUN mvn dependency:go-offline

COPY src ./src

RUN mvn install package -DskipTests 
#RUN mvn package -DskipTests

RUN ls -la /app/target

# Etapa2: Ejecución del JAR
FROM eclipse-temurin:21.0.7_6-jdk

# Parámetros configurables
ARG APP_NAME

# Configura encoding, localtime, idioma y país
ENV LANG='es_MX.UTF-8'
ENV LC_ALL='es_MX.UTF-8'
ENV LANGUAGE='es_MX:es'

# Configura la zona horaria
ENV TZ=America/Mexico_City
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# Copiar el archivo JAR
COPY target/*.jar "/app/front-0.0.1-SNAPSHOT.jar"

#COPY config/*.yml /apps/config # Deploy

WORKDIR /app

COPY --from=build /app/target/front-0.0.1-SNAPSHOT.jar /app/front.jar

EXPOSE 8080

ENTRYPOINT [ "java", "-jar", "/app/front.jar" ]