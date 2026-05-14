FROM eclipse-temurin:17

WORKDIR /app

COPY target/login-service-1.0.jar login-service.jar

EXPOSE 8081

ENTRYPOINT ["java","-jar","login-service.jar"]
