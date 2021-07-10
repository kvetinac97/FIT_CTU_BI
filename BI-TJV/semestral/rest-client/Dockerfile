FROM azabost/android-sdk:base AS client-build

COPY . /client
WORKDIR /client
RUN bash ./gradlew assemble