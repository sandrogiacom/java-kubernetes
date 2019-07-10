# Java and Kubernetes

Show how you can move your spring boot application to docker and kubernetes.
This project is a demo for the series of posts on dev.to
https://dev.to/sandrogiacom/kubernetes-for-java-developers-setup-41nk

## Part one - base app:

### Requirements:

**Docker and Make**

### Build and run application:

Spring boot and mysql database running on docker

**Clone from repository**
```bash
git clone https://github.com/sandrogiacom/java-kubernetes.git
```

**Build application**
```bash
make build
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

We have an application and image running in docker
Now, we deploy application in a kunernetes cluster running in our machine

Prepare

### Start minikube
`make k:setup` start minikube, enable ingress and create namespace dev-to

### Deploy database

`make k:deploy-db` create mysql deployment and service

`k get pods -n=dev-to`

`k port-forward -n=dev-to <pod_name> 3306:3306`

## Build application and deploy

`make build-app` build app and create docker image inside minikube machine

`make deploy-app` create app deployment and service

## Check pods

`k get pods -n=dev-to`

Delete pod
`k delete pod -n dev-to myapp-f6774f497-82w4r`

Replicas
`k get rs -n dev-to`

Scale
`kubectl -n dev-to scale deployment/myapp --replicas=2`

`
while true
do curl "http://192.168.99.141:30358/hello"
echo
sleep 2
done
`
Hello myapp-f6774f497-sbzzh/172.17.0.9
curl: (7) Failed to connect to 192.168.99.141 port 30358: Connection refused

Ocorre erro quando escala porque não temos o /health


## Check app url
`minikube -p=dev.to service -n dev-to myapp --url`

Change your IP and PORT as you need it

`curl -X GET http://192.168.99.132:31838/persons`

Add new Person
`curl -X POST http://192.168.99.132:31838/persons -H "Content-Type: application/json" -d '{"name": "New Person", "birthDate": "2000-10-01"}'`

## Minikube dashboard

`minikube -p dev.to dashboard`

## Part four - debug app:

add   JAVA_OPTS: "-agentlib:jdwp=transport=dt_socket,address=*:5005,server=y,suspend=n -Xms256m -Xmx512m -XX:MaxMetaspaceSize=128m"
change CMD to ENTRYPOINT on Dockerfile

`k get pods -n=dev-to`

`k port-forward -n=dev-to <pod_name> 5005:5005`

## Start all

`make k:all`

## Restart virtualbox ip

`rm  ~/.config/VirtualBox/HostInterfaceNetworking-vboxnet0-Dhcpd.leases`
`rm  ~/.config/VirtualBox/HostInterfaceNetworking-vboxnet0-Dhcpd.leases-prev`
