# Let's compile the application
FROM eclipse-temurin:21-jdk-alpine AS build
RUN mkdir -p /opt/app
WORKDIR /opt/app
COPY . /opt/app
RUN ./mvnw -e -B package -Dmaven.test.skip=true --no-transfer-progress

# Discover the modules that our application uses
FROM eclipse-temurin:21-jdk-alpine AS dependencies
RUN mkdir -p /opt/app
WORKDIR /opt/app
COPY --from=build /opt/app/target/garmin-connect-weight-api.jar /opt/app
RUN unzip garmin-connect-weight-api.jar -d ./app && \
    jdeps \
      --print-module-deps \
      --ignore-missing-deps \
      --recursive \
      --multi-release 21 \
      --class-path="./app/BOOT-INF/lib/*" \
      --module-path="./app/BOOT-INF/lib/*" \
      /opt/app/garmin-connect-weight-api.jar > app-modules.txt

# Create a custom JDK image with the required modules
FROM eclipse-temurin:21-jdk-alpine AS custom-jdk
COPY --from=dependencies /opt/app/app-modules.txt app-modules.txt
RUN apk add --no-cache binutils
RUN jlink \
        --add-modules "$(cat app-modules.txt),jdk.naming.dns,jdk.crypto.ec" \
        --strip-debug \
        --no-man-pages \
        --no-header-files \
        --compress=2 \
        --output /custom-jdk

# Build the image with the custom JDK and our application
FROM alpine:latest as final
ENV JAVA_HOME=/jdk
ENV PATH="${JAVA_HOME}/bin:${PATH}"
COPY --from=custom-jdk /custom-jdk $JAVA_HOME
RUN mkdir -p /opt/app
COPY --from=build /opt/app/target/garmin-connect-weight-api.jar /opt/app
ENV PORT 8080
EXPOSE $PORT
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/opt/app/garmin-connect-weight-api.jar"]