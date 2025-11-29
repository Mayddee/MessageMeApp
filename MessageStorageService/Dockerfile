FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY target/*.jar app.jar

EXPOSE 8082
EXPOSE 9292

ENTRYPOINT ["java", "-jar", "app.jar"]
