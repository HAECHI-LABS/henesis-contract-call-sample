FROM openjdk:8-jdk-alpine
LABEL maintainer="larry@haechi.io"
VOLUME /tmp
EXPOSE 8080
ADD /contract-call-example.jar contract-call-example.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-Dspring.profiles.active=dev","-jar","/contract-call-example.jar"]