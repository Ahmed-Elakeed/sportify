# Step 1: Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim

# Step 2: Set the working directory in the container
WORKDIR /app

# Step 3: Copy the JAR file from the target directory to the container
COPY target/sportify-0.0.1-SNAPSHOT.jar /app/app.jar

# Step 4: Expose the port on which the application will run
EXPOSE 8080

# Step 6: Run the JAR file using Java
ENTRYPOINT ["java", "-jar", "app.jar"]
