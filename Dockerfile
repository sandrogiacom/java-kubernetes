FROM openjdk:11.0.3-jdk-slim

RUN mkdir /usr/myapp

COPY target/java-kubernetes-0.0.1-SNAPSHOT.jar /usr/myapp/app.jar
WORKDIR /usr/myapp

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
