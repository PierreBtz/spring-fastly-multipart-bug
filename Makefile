.PHONY: build-java
build-java:
	@mvn verify

build-docker: build-java
	@docker build -t spring-fastly-multipart-bug:latest .

deploy-app: build-java
	SPRING_PROFILES_ACTIVE=default docker-compose up --build

deploy-app-no-rasp: build-java
	SPRING_PROFILES_ACTIVE=norasp docker-compose up --build

send-request:
	@curl -v -X POST http://127.0.0.1:8080/api/test -F report=report -F attachmentId=1234