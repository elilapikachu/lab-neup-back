FROM amazoncorretto:21 as build
LABEL authors="Crazzys"

WORKDIR /app

COPY . .

# Usar el gradle wrapper que ya existe en el proyecto
RUN chmod +x ./gradlew && ./gradlew clean build -x test

# Etapa final
FROM amazoncorretto:21
LABEL authors="Crazzys"

WORKDIR /app

RUN yum install -y shadow-utils && yum clean all

RUN useradd -m -u 1000 appuser

# Copiar el JAR compilado
COPY --from=build /app/build/libs/*.jar app.jar

RUN chown -R appuser:appuser /app
USER appuser

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]