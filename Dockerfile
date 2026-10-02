# Lyfia as a native executable, built entirely inside Docker.
#   docker build -t lyfia .
# Configuration comes only from environment variables at run time (see README): the image holds no secret.

## Build: Mandrel compiles the app and the frontend (Quinoa downloads its own Node) to a native executable
FROM quay.io/quarkus/ubi9-quarkus-mandrel-builder-image:jdk-21 AS build
USER quarkus
WORKDIR /code

# Dependencies first, in their own layer, so a source change does not download them again
COPY --chown=quarkus:quarkus --chmod=0755 mvnw /code/mvnw
COPY --chown=quarkus:quarkus .mvn /code/.mvn
COPY --chown=quarkus:quarkus pom.xml /code/
RUN ./mvnw -B -q dependency:go-offline quarkus:go-offline

COPY --chown=quarkus:quarkus src /code/src
RUN ./mvnw -B package -Dnative -DskipTests

## Run: a minimal base holding only what the native executable links against
FROM quay.io/quarkus/ubi9-quarkus-micro-image:2.0
WORKDIR /work/
RUN chown 1001 /work \
    && chmod "g+rwX" /work \
    && chown 1001:root /work
COPY --from=build --chown=1001:root --chmod=0755 /code/target/*-runner /work/application
EXPOSE 8080
USER 1001
ENTRYPOINT ["./application", "-Dquarkus.http.host=0.0.0.0"]
