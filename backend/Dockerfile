# Gradle 빌드 단계를 추가한 Dockerfile
FROM openjdk:21-ea-oraclelinux8 AS builder
WORKDIR /app

# 필요한 패키지 설치
RUN microdnf install -y findutils

# 프로젝트 파일 복사 후 실행 권한 추가 및 빌드 수행
COPY . .
RUN chmod +x ./gradlew
RUN ./gradlew bootJar

# 빌드된 JAR 파일을 가져와 최종 이미지를 만듭니다.
FROM openjdk:21-ea-oraclelinux8
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar

# 컨테이너 설정
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
