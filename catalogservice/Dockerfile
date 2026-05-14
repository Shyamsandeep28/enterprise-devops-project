FROM eclipse-temurin:17

WORKDIR /app

COPY target/catalogservice-0.0.1-SNAPSHOT.jar catalogservice.jar

EXPOSE 8082

ENTRYPOINT ["java","-jar","catalogservice.jar"]
