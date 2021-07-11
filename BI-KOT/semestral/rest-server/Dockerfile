FROM maven:3.6.3-openjdk-11 AS server-build

COPY . /server
WORKDIR /server
RUN mvn install:install-file -Dfile=libs/model.jar -DgroupId=cz.wrzecond -DartifactId=app-model -Dversion=1.0.0 -Dpackaging=jar
RUN mvn package