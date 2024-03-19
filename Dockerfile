FROM openjdk:17

COPY target/FoxBot-1.0-SNAPSHOT.jar /FoxBot.jar
WORKDIR /srv
ENTRYPOINT [ "java", "-jar", "/FoxBot.jar" ]