FROM java:8-jdk-alpine

# Copy jar file
COPY ./target/async-rest-jersey-sample-1.0-SNAPSHOT.jar /usr/app/

WORKDIR /usr/app

EXPOSE 8080

# run the app
CMD ["/usr/bin/java", "-jar", "async-rest-jersey-sample-1.0-SNAPSHOT.jar"]
#ENTRYPOINT ["java", "-jar", "async-rest-jersey-sample-1.0-SNAPSHOT.jar"]
