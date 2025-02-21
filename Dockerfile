FROM openjdk:21
WORKDIR /app
COPY target/support-0.0.1-SNAPSHOT.jar .
CMD ["java", "-jar", "support-0.0.1-SNAPSHOT.jar"]
