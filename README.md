# Java and Kubernetes

Show how you can move your spring boot application to docker and kubernetes.
This project is a demo for the series of posts on dev.to
https://dev.to/sandrogiacom/kubernetes-for-java-developers-setup-41nk

## Part one - base app:

### Requirements:

**Docker and Make (Optional)**

**Java 14**

### Build and run application:

Spring boot and mysql database running on docker

**Clone from repository**
```bash
git clone https://github.com/sandrogiacom/java-kubernetes.git
```

**Build application**
```bash
cd java-kubernetes
mvn clean install
```

**Start the database**
```bash
make run-db
```

**Run application**
```bash
java --enable-preview -jar target/java-kubernetes.jar
```

**Check**
http://localhost:8080/app/users

## Part two - app on Docker:

Create a Dockerfile:

```yaml
FROM openjdk:14-alpine
RUN mkdir /usr/myapp
COPY target/java-kubernetes.jar /usr/myapp/app.jar
WORKDIR /usr/myapp
EXPOSE 8080
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -jar app.jar" ]
```

**Build application and docker image**

```bash
make build
```

Create and run the database
```bash
make run-db
```

Create and run the application
```bash
make run-app
```

**Check**
http://localhost:8080/app/users
http://localhost:8080/app/hello

## Part three - app on Kubernetes:

We have an application and image running in docker
Now, we deploy application in a kubernetes cluster running in our machine

Prepare

### Start minikube
`make k-setup` start minikube, enable ingress and create namespace dev-to

### Deploy database

`make k-deploy-db` create mysql deployment and service

`kubectl get pods -n dev-to`

`kubectl port-forward -n dev-to <pod_name> 3306:3306`

## Build application and deploy

`make k-build-app` build app

`make k-build-image` create docker image inside minikube machine

`make k-deploy-app` create app deployment and service

**Check**

`
kubectl get services -n dev-to
`

`
minikube -p dev.to service -n dev-to myapp --url
`

http://172.17.0.5:32594/app/users
http://172.17.0.5:32594/app/hello

## Check pods

`
kubectl get pods -n dev-to
`

`
kubectl -n dev-to logs myapp-6ccb69fcbc-rqkpx
`

## Map to dev.local

get minikube IP
`
minikube -p dev.to ip
` 

Edit `hosts` 

Replicas
`
kubectl get rs -n dev-to
`

Get and Delete pod
`
kubectl get pods -n dev-to
`

`
kubectl delete pod -n dev-to myapp-f6774f497-82w4r
`

Scale
`
kubectl -n dev-to scale deployment/myapp --replicas=2
`

Test replicas
`
while true
do curl "http://dev.local/app/hello"
echo
sleep 2
done
`
Test replicas with wait

`
while true
do curl "http://dev.local/app/wait"
echo
done
`

## Check app url
`minikube -p dev.to service -n dev-to myapp --url`

Change your IP and PORT as you need it

`
curl -X GET http://dev.local/app/users
`

Add new User
`
curl --location --request POST 'http://dev.local/app/user' \
--header 'Content-Type: application/json' \
--data-raw '{
    "name": "new user",
    "birthDate": "2010-10-01"
}'
`

## Minikube dashboard

`
minikube -p dev.to dashboard
`

## Part four - debug app:

add   JAVA_OPTS: "-agentlib:jdwp=transport=dt_socket,address=*:5005,server=y,suspend=n -Xms256m -Xmx512m -XX:MaxMetaspaceSize=128m"
change CMD to ENTRYPOINT on Dockerfile

`kubectl get pods -n=dev-to`

`kubectl port-forward -n=dev-to <pod_name> 5005:5005`

## Start all

`make k:all`

## Install Istio

curl -L https://istio.io/downloadIstio | sh - 
export PATH="$PATH:/home/sandro/istio-1.4.2/bin" 
istioctl manifest apply --set profile=demo

kubectl label namespace default istio-injection=enabled
kubectl label dev-to default istio-injection=enabled

https://www.digitalocean.com/community/tutorials/how-to-install-and-use-istio-with-kubernetes

https://grafana.com/grafana/dashboards/4701
https://grafana.com/grafana/dashboards/6756

kubectl -n istio-system port-forward $(kubectl -n istio-system get pod -l app=grafana -o jsonpath='{.items[0].metadata.name}') 3000:3000 &

kubectl -n istio-system port-forward $(kubectl -n istio-system get pod -l app=prometheus -o jsonpath='{.items[0].metadata.name}') 9090:9090 &

add prometheus job
    - job_name: 'myapp'
      metrics_path: '/actuator/prometheus'
      kubernetes_sd_configs:
      - role: endpoints
        namespaces:
          names:
          - dev-to
