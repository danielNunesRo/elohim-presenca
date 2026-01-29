# =====================
# STAGE 1 - BUILD
# =====================
FROM maven:3.9.9-eclipse-temurin-21 AS build

WORKDIR /app

COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline

COPY src src
RUN ./mvnw package -DskipTests

# =====================
# STAGE 2 - RUNTIME
# =====================
FROM registry.access.redhat.com/ubi9/openjdk-21:1.23

WORKDIR /deployments

COPY --from=build /app/target/quarkus-app/lib/ lib/
COPY --from=build /app/target/quarkus-app/app/ app/
COPY --from=build /app/target/quarkus-app/quarkus/ quarkus/
COPY --from=build /app/target/quarkus-app/*.jar ./

EXPOSE 8080

CMD ["java", "-jar", "quarkus-run.jar"]
