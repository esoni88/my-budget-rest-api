FROM openjdk:11
COPY ./target/mybudget-rest-0.0.2-SNAPSHOT.jar /usr/src/mybudget/
WORKDIR /usr/src/mybudget
EXPOSE 8080
CMD ["java", "-jar", "mybudget-rest-0.0.2-SNAPSHOT.jar", "--spring.config.location=file:///usr/config/application.properties"]
