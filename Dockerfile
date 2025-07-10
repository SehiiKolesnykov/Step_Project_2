FROM maven:3.9.9-eclipse-temurin-23 AS build
WORKDIR /app
COPY. .
RUN mvn clean package -DskipTests

From eclipse-temurin:23-jre
WORKDIR /app
COPY --from=build /app/target/Step_Project_2-1.0-SNAPSHOT.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "appjar"]