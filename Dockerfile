FROM openjdk:21
EXPOSE 8080
ADD target/demo-store-app-0.0.1-SNAPSHOT.jar demo-store-app.jar
ENTRYPOINT ["java", "-jar", "/demo-store-app.jar"]
