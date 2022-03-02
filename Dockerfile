FROM openjdk:17-jdk-alpine
COPY target/*.jar /app.jar
EXPOSE 8080
CMD ["/usr/bin/java", "-jar", "-Dspring.profiles.active=default", "/app.jar"]
