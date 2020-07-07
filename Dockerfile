FROM openjdk:14-alpine

RUN mkdir /usr/myapp

COPY target/java-kubernetes.jar /usr/myapp/app.jar
WORKDIR /usr/myapp

EXPOSE 8080

ENTRYPOINT [ "sh", "-c", "java --enable-preview $JAVA_OPTS -jar app.jar" ]
