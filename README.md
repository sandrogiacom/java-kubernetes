# Java and Kubernetes

Show how you can move your spring boot application to docker and kubernetes.
This project is a demo for the series of posts on dev.to
https://dev.to/sandrogiacom/kubernetes-for-java-developers-setup-41nk

## Part one - base app:

### Requirements:

**Docker and Make**

### Build and run application:

Spring boot and mysql database running on docker

**Build application**
```bash
mvn clean install
```

**Start the database**
```bash
make run:db
```

**Run application**
```bash
java -jar target/java-kubernetes-0.0.1-SNAPSHOT.jar
```

**Check**
http://localhost:8080/persons


## Part two - app on Docker:

Create a Dockerfile:

```yaml
FROM openjdk:11.0.3-jdk-slim
RUN mkdir /usr/myapp
COPY target/java-kubernetes-0.0.1-SNAPSHOT.jar /usr/myapp/app.jar
WORKDIR /usr/myapp
EXPOSE 8080
CMD ["java", "-Xms128m", "-Xmx256m", "-jar", "app.jar"]
```

**Build application and docker image**

```bash
make build
```

Create and run the database
```bash
make run:db
```

Create and run the application
```bash
make run:app
```

**Check**
http://localhost:8080/persons


## Part three - app on Kubernetes:

**Minikube and VirtualBox**

We have an application and image running in docker
Now, we deploy application in a kunernetes cluster running in our machine

Prepare

### Start minikube
`make k:setup` start minikube, enable ingress and create namespace dev-to


### Deploy database

`make k:db` create mysql deployment and service

`k get pods -n=dev-to`

`k port-forward -n=dev-to <pod_name> 3306:3306`

## Create application

`make k:build` build app and create docker image inside minikube machine

`make k:app` create app deployment and service

## Check pods

`k get pods -n=dev-to`

## Check app url
`minikube -p=dev.to service -n dev-to myapp --url`

`curl -X GET http://192.168.99.132:31838/persons`

## Minikube dashboard

`minikube -p dev.to dashboard`

minikube -p=dev.to service -n dev-to myapp --url



## Mapping local Docker deamon to Minikube
`eval $(minikube -p dev.to docker-env)`


configure application database url

build image 
`make k:build`

`docker images`


Create deployment file
Create service file





