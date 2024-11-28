## Build stage
#FROM gradle:7.6-jdk17 AS build
#WORKDIR /app
#COPY backend/ .
#RUN chmod +x ./gradlew
#RUN sed -i 's/\r$//' ./gradlew
#RUN ./gradlew :backend:build -x test --no-daemon
# Build stage
FROM gradle:7.6-jdk17 AS build
WORKDIR /app
COPY / .
RUN chmod +x ./gradlew
RUN sed -i 's/\r$//' ./gradlew
RUN ./gradlew build -x test --no-daemon

## Run stage
#FROM openjdk:17-jdk-slim
#WORKDIR /app
#COPY --from=build /app/backend/build/libs/*.jar app.jar
#COPY --from=build /app/backend/src/main/resources/application.yml /app/
#
#ENV SPRING_PROFILES_ACTIVE=${MODE:-prod}
#
#CMD java -jar -Dspring.profiles.active={MODE:-prod} app.jar
#
#CMD ["java", "-jar", "-Dserver.address=0.0.0.0", "-Dserver.port=8080", "/app/app.jar"]
# Run stage
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
COPY --from=build /app/src/main/resources/application.yml /app/

ENV SPRING_PROFILES_ACTIVE=${MODE:-prod}

CMD java -jar -Dspring.profiles.active={MODE:-prod} app.jar

CMD ["java", "-jar", "-Dserver.address=0.0.0.0", "-Dserver.port=8080", "/app/app.jar"]