# Build stage
FROM gradle:7.6-jdk17 AS build
WORKDIR /app
COPY .. .
RUN chmod +x ./gradlew
RUN ./gradlew :backend:build -x test --no-daemon

# Run stage
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/backend/build/libs/*.jar app.jar
COPY --from=build /app/backend/src/main/resources/application.yml /app/

ENV SPRING_PROFILES_ACTIVE=${SPRING_PROFILES_ACTIVE:-prod}

CMD java -jar -Dspring.profiles.active=$SPRING_PROFILES_ACTIVE app.jar

CMD ["java", "-jar", "-Dserver.address=0.0.0.0", "-Dserver.port=8080", "/app/app.jar"]