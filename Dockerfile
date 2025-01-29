FROM openjdk:17
WORKDIR /app
COPY "target/SalesConversionAPI-0.0.1-SNAPSHOT.jar" "/app/SalesConversionAPI-0.0.1-SNAPSHOT.jar"
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/SalesConversionAPI-0.0.1-SNAPSHOT.jar"]