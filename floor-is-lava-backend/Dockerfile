FROM openjdk:17-ea-21-jdk

WORKDIR /app

COPY build/libs/floor-is-lava-backend-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

CMD [ "java", "-jar", "app.jar" ]