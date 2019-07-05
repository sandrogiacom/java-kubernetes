# defaul shell
SHELL = /bin/bash

# Rule "help"
.PHONY: help
.SILENT: help
help:
	echo "Use make [rule]"
	echo "Rules:"
	echo ""
	echo "build 		- build application and generate docker image"
	echo "run:db 		- run mysql database on docker"
	echo "run:app		- run application on docker"
	echo "stop:app	- stop application"
	echo "stop:db		- stop database"
	echo "rm:app		- stop and delete application"
	echo "rm:db		- stop and delete database"
	echo "k:setup		- init minikube machine"
	echo "k:deploy-db	- deploy mysql on cluster"
	echo "k:build-app	- build app and create docker image inside minikube"
	echo "k:deploy-app	- deploy app on cluster"
	echo "k:delete	- stop and delete minikube machine"
	echo "check		- check tools versions"
	echo "help		- show this message"

# Rule "build"
.PHONY: build
.SILENT: build
build:
	mvn clean install; \
	docker build --force-rm -t java-k8s .

# Rule "run:db"
.PHONY: run\:db
.SILENT: run\:db
run\:db:
	make stop:db; \
	make rm:db; \
	docker run --name mysql57 -p 3306:3306 -e MYSQL_ROOT_PASSWORD=123456 -e MYSQL_USER=java -e MYSQL_PASSWORD=1234 -e MYSQL_DATABASE=k8s_java -d mysql/mysql-server:5.7

# Rule "run:app"
.PHONY: run\:app
.SILENT: run\:app
run\:app:
	make stop:app; \
	make rm:app; \
	docker run --name myapp -p 8080:8080 -d -e DATABASE_SERVER_NAME=mysql57 --link mysql57:mysql57 java-k8s:latest

# Rule "stop:app"
.PHONY: stop\:app
.SILENT: stop\:app
stop\:app:
	docker stop myapp; \

# Rule "stop:db"
.PHONY: stop\:db
.SILENT: stop\:db
stop\:db:
	docker stop mysql57

# Rule "rm:app"
.PHONY: rm\:app
.SILENT: rm\:app
rm\:app:
	make stop:app; \
	docker rm myapp; \

# Rule "rm:db"
.PHONY: rm\:db
.SILENT: rm\:db
rm\:db:
	make stop:db; \
	docker rm mysql57

# Rule "k:setup"
.PHONY: k\:setup
.SILENT: k\:setup
k\:setup:
	minikube -p dev.to start --cpus 2 --memory=4098; \
	minikube -p dev.to addons enable ingress; \
	kubectl create namespace dev-to

# Rule "k:deploy-db"
.SILENT: k\:deploy-db
k\:deploy-db:
	kubectl create -f kubernetes/mysql/;

# Rule "k:build-app"
.SILENT: k\:build-app
k\:build-app:
	mvn clean install; \
	eval $$(minikube -p dev.to docker-env) && docker build --force-rm -t java-k8s .;

# Rule "k:deploy-app"
.SILENT: k\:deploy-app
k\:deploy-app:
	kubectl create -f kubernetes/app/;

# Rule "k:delete"
.SILENT: k\:delete
k\:delete:
	minikube -p dev.to stop && minikube -p dev.to delete

# Rule "check"
.SILENT: check
check:
	echo "make version " && make --version && echo
	minikube version && echo
	echo "kubectl version" && kubectl version --short --client && echo
	echo "virtualbox version" && vboxmanage --version  && echo

