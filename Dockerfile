FROM debian:12-slim

RUN apt update && apt install -y openjdk-17-jdk

COPY target/FoxBot-1.0-SNAPSHOT.jar /FoxBot.jar
COPY frontend/out /frontend
WORKDIR /srv
ENTRYPOINT [ "java", "-jar", "/FoxBot.jar" ]
