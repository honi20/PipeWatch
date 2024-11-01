# 빌드용 이미지
FROM openjdk:21-ea-oraclelinux8 AS builder
WORKDIR /app
COPY . .
RUN chmod +x ./gradlew
CMD ["./gradlew", "clean", "build", "-x", "test"]

# 실행용 이미지
FROM openjdk:21-ea-oraclelinux8
WORKDIR /app
COPY --from=builder ./build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
