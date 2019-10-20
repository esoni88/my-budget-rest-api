FROM openjdk:11
COPY ./target/mybudget-rest.jar /usr/src/mybudget/
WORKDIR /usr/src/mybudget
EXPOSE 8080
CMD ["java", "-jar", "mybudget-rest.jar", "--spring.config.location=file:///usr/config/application.properties"]
