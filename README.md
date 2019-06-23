# Java and Kubernetes

Show how you can move your spring boot application to docker and kubernetes

## Part one:

### Requirements:

**Docker**

### Build and run application:

Spring boot and mysql database running on docker

**Start the database**
```bash
docker run --name mysql57 -p 3306:3306 \
-e MYSQL_ROOT_PASSWORD=123456 \
-e MYSQL_USER=java \
-e MYSQL_PASSWORD=1234 \
-e MYSQL_DATABASE=k8s_java \
-d mysql/mysql-server:5.7
```

**Build application**
```bash
mvn clean install
```

**Run application**
```bash
java -jar target/java-kubernetes-0.0.1-SNAPSHOT.jar
```

**Check**
http://localhost:8080/persons



## Part two.

Pack application in a docker container


