FROM eclipse-temurin:17
COPY target/spring-fastly-multipart-bug-0.0.1-SNAPSHOT.jar server.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/server.jar"]