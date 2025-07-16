FROM ghcr.io/graalvm/graalvm-community:21.0.2 AS build
RUN mkdir -p /opt/app
WORKDIR /opt/app
COPY . /opt/app
RUN ./mvnw -e -B package -Dmaven.test.skip=true --no-transfer-progress

FROM alpine:3.22.1
RUN mkdir -p /opt/app
COPY --from=build /opt/app/target/garmin-connect-weight-api /opt/app
EXPOSE 8080
ENTRYPOINT ["/opt/app/garmin-connect-weight-api"]