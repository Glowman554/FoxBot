FROM debian:12-slim

RUN apt update && apt install -y openjdk-17-jdk
RUN apt install -y  autoconf bison flex gcc g++ git libprotobuf-dev libnl-route-3-dev libtool make pkg-config protobuf-compiler

COPY target/FoxBot-1.0-SNAPSHOT.jar /FoxBot.jar
COPY frontend/out /frontend
WORKDIR /srv
ENTRYPOINT [ "java", "-jar", "/FoxBot.jar" ]