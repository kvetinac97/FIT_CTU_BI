FROM maven:3.6.3-openjdk-11 AS restapi-build

COPY . /restapi
WORKDIR /restapi
RUN mvn package
RUN mvn dokka:dokka