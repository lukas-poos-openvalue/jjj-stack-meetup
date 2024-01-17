# BUILD STAGE
FROM maven:3-eclipse-temurin-21-alpine as build

WORKDIR /workdir
COPY pom.xml /workdir/
COPY src /workdir/src/

RUN mvn clean package

# PACKAGE STAGE
FROM azul/zulu-openjdk-alpine:21-jre-latest
EXPOSE 8080
COPY --from=build /workdir/target/todo-app-plus.jar /todo-app-plus.jar

ENTRYPOINT ["java","-jar","/todo-app-plus.jar"]