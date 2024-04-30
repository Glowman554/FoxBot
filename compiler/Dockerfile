FROM golang:1.22-bookworm

RUN apt update; apt install -y autoconf bison flex gcc g++ git libprotobuf-dev libnl-route-3-dev libtool make pkg-config protobuf-compiler

WORKDIR /usr/src/app

COPY . .
RUN go mod tidy; go build

ENTRYPOINT [ "./compiler" ]
