FROM openjdk:8-jdk-alpine

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java","-jar", "-Dspring.profiles.active=develop", "/app.jar"]

# If the above Entripoint array grow bigger then replace it with a shell script