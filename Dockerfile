#FROM --platform=linux/amd64 openjdk:17-jdk
FROM openjdk:17-jdk

LABEL maintainer="ytjdud01@kookmin.ac.kr"

VOLUME /bank

EXPOSE 8000

ARG JAR_FILE=./build/libs/bank-0.0.1-SNAPSHOT.jar

COPY ${JAR_FILE} bank.jar

ENTRYPOINT ["java","-jar","/bank.jar"]