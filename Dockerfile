FROM openjdk:21-jdk-slim
LABEL authors="Crazzys"

#Este copy copia todo el proyecto desde su ruta principal
COPY . .

# Construir la aplicacion
RUN ./mbnw clean package -DskipTests

ENTRYPOINT ["java", "-jar", "app.jar"]