# Stage 1
FROM maven:3.5.2-jdk-8-alpine AS MAVEN_BUILD
ARG SPRING_ACTIVE_PROFILE=prod
COPY ./pom.xml /build/
COPY ./src /build/src/
WORKDIR /build/
RUN mvn clean install -Dspring.profiles.active=$SPRING_ACTIVE_PROFILE \
    && mvn package -B -e -Dspring.profiles.active=$SPRING_ACTIVE_PROFILE

# Final stage
FROM openjdk:8-alpine
WORKDIR /app
COPY --from=MAVEN_BUILD /build/target/bookland-*.jar /app/bookland.jar
ENTRYPOINT ["java", "-Dserver.port=$PORT", "$JAVA_OPTS", "-jar", "bookland.jar"]
