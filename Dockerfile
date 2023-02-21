# Use an existing image as a base
FROM openjdk:8-jdk-alpine

# Set the working directory
WORKDIR /app

jar cf TestNGFramework.jar *.class

# Copy the Java program to the container
COPY TestNGFramework.jar /app

# Set the VM arguments as an environment variable
ENV JAVA_OPTS="-Dbrowser=chrome -Dtest.env=qa -DincludedGroups=20158"

# Set the default command
CMD ["java", "-jar", "TestNGFramework.jar"]
