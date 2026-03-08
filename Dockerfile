FROM ubuntu:latest

RUN apt-get update && apt-get install -y openjdk-25-jdk-headless

WORKDIR /bot
COPY build/libs/radio-1.0-SNAPSHOT.jar /bot/app.jar

ENTRYPOINT ["java", "-jar", "/bot/app.jar"]
