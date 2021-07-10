FROM maven:3.6.3-openjdk-11 AS model-build

COPY . /model
WORKDIR /model
RUN mvn package