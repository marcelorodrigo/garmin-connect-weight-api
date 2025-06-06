FROM eclipse-temurin:21.0.6_7-jdk-alpine AS build
RUN mkdir -p /opt/app
WORKDIR /opt/app
COPY . /opt/app
RUN ./mvnw -e -B package -Dmaven.test.skip=true --no-transfer-progress

FROM eclipse-temurin:21.0.6_7-jre-alpine
RUN mkdir -p /opt/app
COPY --from=build /opt/app/target/garmin-connect-weight-api.jar /opt/app

ENV PORT 8080
EXPOSE $PORT

ENTRYPOINT ["java","-jar","/opt/app/garmin-connect-weight-api.jar"]
