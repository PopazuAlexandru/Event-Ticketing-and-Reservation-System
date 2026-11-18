FROM maven:3.9.16-eclipse-temurin-25 AS build
WORKDIR /workspace

COPY pom.xml .
COPY ticketing-contracts/pom.xml ticketing-contracts/pom.xml
COPY event-reservation-service/pom.xml event-reservation-service/pom.xml
COPY notification-service/pom.xml notification-service/pom.xml
COPY ticketing-contracts/src ticketing-contracts/src
COPY event-reservation-service/src event-reservation-service/src
COPY notification-service/src notification-service/src

RUN mvn -B -pl event-reservation-service -am package -DskipTests

FROM eclipse-temurin:25-jre-alpine
WORKDIR /app
RUN apk add --no-cache wget
COPY --from=build /workspace/event-reservation-service/target/event-reservation-service-*.jar /app/app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
