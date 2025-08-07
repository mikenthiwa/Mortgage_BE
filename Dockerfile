# Use a Java 17 base image (adjust based on your setup)
FROM eclipse-temurin:17-jdk

# Set working directory inside container
WORKDIR /app

# Copy and build the application
COPY target/mortgage-0.0.1-SNAPSHOT.jar app.jar

# Expose the port your Spring Boot app runs on
EXPOSE 8080

# Run the jar file
ENTRYPOINT ["java", "-jar", "app.jar"]