FROM eclipse-temurin:21.0.4_7-jdk-alpine AS build
RUN mkdir -p /opt/app
WORKDIR /opt/app
COPY .mvn ./.mvn
COPY src ./src
RUN ./mvnw -e -B package --DskipTests --no-transfer-progress

FROM eclipse-temurin:21.0.4_7-jre-alpine
RUN mkdir -p /opt/app
COPY --from=build /opt/app/target/garmin-connect-weight-api.jar /opt/app

ENTRYPOINT ["java","-jar","/opt/app/garmin-connect-weight-api.jar"]
EXPOSE 8080