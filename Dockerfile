FROM maven:3.8.6-openjdk-11-slim AS build
WORKDIR /app
COPY . .
RUN mvn clean package

FROM openjdk:11-jre-slim
WORKDIR /app
COPY --from=build /app/target/dependency/webapp-runner.jar .
COPY --from=build /app/target/*.war .
CMD ["sh", "-c", "java $JAVA_OPTS -jar webapp-runner.jar --port $PORT *.war"]

