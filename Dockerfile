# ====== Etapa 1: Construcción ======
FROM maven:3.9.9-eclipse-temurin-21 AS build

# Copiar pom.xml y resolver dependencias primero
COPY pom.xml .
RUN mvn dependency:go-offline -B --quiet

# Copiar código fuente y compilar
COPY src ./src
RUN mvn clean package -DskipTests -B --quiet

# ====== Etapa 2: Imagen final ======
FROM eclipse-temurin:21-jre-alpine

# Metadata
LABEL maintainer="BCB Casa de Bolsa" \
description="Sistema de Administración Fiduciaria" \
version="1.0.0"

WORKDIR /app

# Crear usuario no-root para seguridad
RUN addgroup -S spring && adduser -S spring -G spring \
    && apk add --no-cache curl wget openssl tzdata \
    && chown -R spring:spring /app

    # Configura encoding, localtime, idioma y país
ENV TZ=America/Mexico_City \
    LANG='es_MX.UTF-8' \
    LC_ALL='es_MX.UTF-8' \
    LANGUAGE='es_MX:es' \
    JAVA_OPTS="-Xms512m -Xmx1024m"

RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

USER spring

COPY --from=build /app/target/front-0.0.1-SNAPSHOT.jar /app/front.jar
EXPOSE 10101
ENTRYPOINT [ "java", "-jar", "/app/front.jar" ]
