FROM maven:3.9-amazoncorretto-21-al2023 as firststep

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

FROM amazoncorretto:21

WORKDIR /app

COPY --from=firststep /app/target/user-auth-service-0.0.1-SNAPSHOT.jar authentication.jar

ENV TZ=America/Sao_Paulo

EXPOSE 5003

CMD ["java", "-jar", "authentication.jar"]