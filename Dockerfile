FROM gradle:7.6.0-jdk17 AS build
COPY . /app
WORKDIR /app
RUN gradle build -x test

FROM openjdk:21-slim
COPY --from=build /app/build/libs/turamyzba-0.0.1-SNAPSHOT.jar /app/turamyzba.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/turamyzba.jar"]
