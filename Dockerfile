FROM eclipse-temurin:21.0.7_6-jdk-alpine AS build
RUN mkdir -p /opt/app
WORKDIR /opt/app

# Copy Maven wrapper and pom.xml first
COPY mvnw mvnw.cmd ./
COPY .mvn .mvn
COPY pom.xml ./

# Download dependencies first (this layer will be cached)
RUN ./mvnw -e -B dependency:go-offline --no-transfer-progress

# Now copy the source code (this layer changes frequently)
COPY src ./src


RUN ./mvnw -e -B package -Dmaven.test.skip=true --no-transfer-progress

FROM gcr.io/distroless/java21-debian12:nonroot
COPY --from=build /opt/app/target/garmin-connect-weight-api.jar /opt/app/garmin-connect-weight-api.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/opt/app/garmin-connect-weight-api.jar"]
