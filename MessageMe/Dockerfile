FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY target/*.jar app.jar

EXPOSE 8081
EXPOSE 9090

ENTRYPOINT ["java", "-jar", "app.jar"]
