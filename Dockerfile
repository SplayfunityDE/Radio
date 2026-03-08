FROM ubuntu:latest

RUN apt-get update && apt-get install -y java-26-amazon-corretto-jdk

RUN mkdir /bot
COPY build/libs/radio-1.0-SNAPSHOT.jar /bot

ENTRYPOINT ["java", "-jar", "/bot/radio-1.0-SNAPSHOT.jar"]
