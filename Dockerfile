# Etapa 1: Construcción del proyecto
FROM maven:3.9.4-eclipse-temurin-21 AS build

# Establece el directorio de trabajo
WORKDIR /app

# Copia los archivos del proyecto al contenedor
COPY . .

# Ejecuta la construcción del proyecto con Maven
RUN mvn clean package -DskipTests

# Etapa 2: Ejecución
FROM openjdk:21-jdk-slim

# Establece el directorio de trabajo
WORKDIR /app

# Copia el archivo JAR generado desde la etapa de construcción
COPY --from=build /app/target/viajesCompartidos-0.0.1-SNAPSHOT.jar app.jar

# Expone el puerto de tu aplicación
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
