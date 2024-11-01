FROM openjdk:21-ea-oraclelinux8
LABEL maintainer="Churnobyl"

VOLUME /tmp

CMD ["./gradlew", "bootJar"]
ARG JAR_FILE_PATH=build/libs/*.jar
COPY ${JAR_FILE_PATH} app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]

