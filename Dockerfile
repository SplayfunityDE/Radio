FROM amazoncorretto:25

RUN mkdir /bot
COPY build/libs/radio-1.0-SNAPSHOT.jar /bot

ENTRYPOINT ["java", "-jar", "/bot/radio-1.0-SNAPSHOT.jar"]
