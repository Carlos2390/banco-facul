FROM eclipse-temurin:17-jdk-jammy AS build

WORKDIR /app
COPY pom.xml mvnw ./
COPY .mvn .mvn

RUN chmod +x ./mvnw

RUN ./mvnw dependency:resolve

COPY src src

RUN ./mvnw package -DskipTests

FROM eclipse-temurin:17-jre-jammy

EXPOSE 8080
WORKDIR /demo

COPY --from=build /app/target/*.jar demo.jar

ENTRYPOINT ["java", "-jar", "demo.jar"]
