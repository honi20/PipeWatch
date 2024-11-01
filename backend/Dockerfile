# 빌드용 이미지
FROM openjdk:21-ea-34-jdk-slim AS builder

WORKDIR /app

COPY . .

RUN chmod +x ./gradlew
RUN ./gradlew clean build -x test

# 실행용 이미지
FROM openjdk:21-ea-34-jdk-slim

COPY --from=builder /app/build/libs/*.jar /app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
