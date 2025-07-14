FROM maven:3.8.5-openjdk-17 AS build

WORKDIR /app

COPY pom.xml ./
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn clean package -DskipTests

RUN ls -la /app/target

# Second step: create the final image

FROM openjdk:17-jdk-alpine

WORKDIR /app

COPY --from=build /app/target/uploadFile-0.0.1-SNAPSHOT.jar /app/uploadFile-0.0.1-SNAPSHOT.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/uploadFile-0.0.1-SNAPSHOT.jar"]
