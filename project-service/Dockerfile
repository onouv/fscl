FROM maven:latest AS service
WORKDIR /src/fscl/project-service
COPY . .
RUN mvn -B -f pom.xml dependency:resolve
RUN mvn -B package -DskipTests

FROM openjdk:8-jdk-alpine AS runtime
RUN apk update && apk add bash
RUN mkdir -p /app
WORKDIR /app
COPY --from=service /src/fscl/project-service/target/project-service-0.2.0.jar .
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/project-service-0.2.0.jar"]
