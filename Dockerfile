# Use an official Java runtime as a parent image
FROM openjdk:11-jre-slim

# Set the working directory to /app
WORKDIR /app

# Copy the current directory contents into the container at /app
COPY . /app

# Set the environment variable for the VM argument
ENV MY_VM_ARG_BROWSER=chrome
ENV MY_VM_ARG_ENV=qa
ENV MY_VM_ARG_GROUPS=20158

# Run the command to start the Java application with the VM argument
CMD ["java", "-jar", "-Dbrowser=${MY_VM_ARG_BROWSER},-Dtest.env=${MY_VM_ARG_ENV},-DincludedGroups=${MY_VM_ARG_GROUPS}", "myapp.jar"

