FROM ubuntu:latest

RUN apt-get update && apt-get install -y \
    wget \
    gnupg \
    && rm -rf /var/lib/apt/lists/*

RUN wget -O - https://apt.corretto.aws/corretto.key | gpg --dearmor -o /usr/share/keyrings/corretto-keyring.gpg && \
    echo "deb [signed-by=/usr/share/keyrings/corretto-keyring.gpg] https://apt.corretto.aws stable main" | tee /etc/apt/sources.list.d/corretto.list

RUN apt-get install -y java-26-amazon-corretto-jdk

WORKDIR /bot
COPY build/libs/radio-1.0-SNAPSHOT.jar /bot/app.jar

ENTRYPOINT ["java", "-jar", "/bot/app.jar"]
