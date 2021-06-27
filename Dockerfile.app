# Alpine Linux with OpenJDK JRE
FROM openjdk:8-jre-alpine

WORKDIR /app

# Copy jar file
COPY ./target/Async-Rest-Jersey-1.0-SNAPSHOT.jar /app

EXPOSE 8080

# run the app
#CMD ["/usr/bin/java", "-jar", "/Async-Rest-Jersey-1.0-SNAPSHOT.jar"]
ENTRYPOINT ["/usr/bin/java", "-jar", "Async-Rest-Jersey-1.0-SNAPSHOT.jar"]
