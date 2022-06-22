FROM maven:3.5.2-jdk-8-alpine AS MAVEN_BUILD
MAINTAINER Daniel Correa
COPY pom.xml /build/
COPY src /build/src/
WORKDIR /build/
RUN mvn package

FROM openjdk:11-jre-alpine
WORKDIR /app
COPY --from=MAVEN_BUILD /build/target/VariaMosServices.jar /app/
ENTRYPOINT ["java", "-jar", "VariaMosServices.jar"]