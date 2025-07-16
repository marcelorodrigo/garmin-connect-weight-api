FROM ghcr.io/graalvm/graalvm-community:21.0.2 AS build
RUN mkdir -p /opt/app
WORKDIR /opt/app
COPY . /opt/app
RUN ./mvnw -e -B -Dmaven.test.skip=true --no-transfer-progress -Pnative native:compile

FROM gcr.io/distroless/static-debian12:nonroot
COPY --from=build /opt/app/target/garmin-connect-weight-api /opt/app/garmin-connect-weight-api
EXPOSE 8080
ENTRYPOINT ["/opt/app/garmin-connect-weight-api"]