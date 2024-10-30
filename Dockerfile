# Step 1: Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim

# Install MySQL
RUN apt-get update && \
    apt-get install -y mysql-server && \
    rm -rf /var/lib/apt/lists/*

# Configure MySQL
RUN service mysql start && \
    mysql -e "CREATE DATABASE sportify;" && \
    mysql -e "ALTER USER 'root'@'localhost' IDENTIFIED BY '';"

# Step 2: Set the working directory in the container
WORKDIR /app

# Step 3: Copy the JAR file from the target directory to the container
COPY target/sportify-0.0.1-SNAPSHOT.jar /app/app.jar

# Step 4: Expose the port on which the application will run
EXPOSE 8080 3306

# Step 6: Run the JAR file using Java
ENTRYPOINT ["java", "-jar", "app.jar"]


# Start MySQL and Spring Boot app
CMD service mysql start && java -jar app.jar
