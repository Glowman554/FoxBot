FROM golang:1.22-bookworm

RUN apt update; apt install -y clang autoconf bison flex gcc g++ git libprotobuf-dev libnl-route-3-dev libtool make pkg-config protobuf-compiler

WORKDIR /
RUN git clone https://github.com/Glowman554/FireStorm

WORKDIR /FireStorm/fire
RUN go mod tidy; go build; cp fire /bin/fire


WORKDIR /usr/src/app

COPY . .
RUN go mod tidy; go build

ENTRYPOINT [ "./compiler" ]
