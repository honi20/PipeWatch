# Stage 1: 빌드
FROM node:20.15.0-alpine AS builder

## 이미지 안에 app 디렉토리 만들고 거기서 작업하기
WORKDIR /app

## package.json을 app 디렉토리 복사
COPY package.json package-lock.json ./

## package.json을 바탕으로 설치
RUN npm install

## 나머지 파일들 복사
COPY . .

## 빌드 실행
RUN npm run buildWithoutTsc

# Stage 2: Dist 배포
FROM node:20.15.0-alpine

## 서버 실행하기 위해 serve 설치
RUN yarn global add serve

## 워킹 디렉토리 설정
WORKDIR /app

## 빌더에서 dist 디렉토리 복사
COPY --from=builder /app/dist ./

## 빌더에서 env.sh 쉘 스크립트 복사
COPY --from=builder /app/env.sh ./env.sh

## 쉘 스크립트에 권한 주기
RUN chmod +x ./env.sh

## 3000번 포트 개방
EXPOSE 3000

## 런타임에 쉘 스크립트 실행하고 3000번 포트로 서버 띄우기
CMD ["sh", "-c", "./env.sh && serve -s . -l 3000"]