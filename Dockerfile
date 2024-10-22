FROM gradle:7.6-jdk21 AS build
COPY --chown=gradle:gradle . /app
WORKDIR /app
RUN gradle build -x test

FROM openjdk:21-slim
COPY --from=build /app/build/libs/*.jar /turamyzba.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/turamyzba.jar"]
