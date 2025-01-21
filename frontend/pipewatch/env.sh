#!/bin/sh

## .env이 존재하면 파일 로드
if [ -f .env ]; then
  export $(grep -v '^#' .env | xargs)
fi

## env-config.js 파일 생성하기
## 추가적인 .env 변수들 여기다 추가하기
echo "window._env_ = {" > ./env-config.js
echo "  VITE_URL: '$VITE_URL'," >> ./env-config.js
echo "  VITE_LOGIN_URL: '$VITE_LOGIN_URL'," >> ./env-config.js
echo "  VITE_LOCAL_LOGIN_URL: '$VITE_LOCAL_LOGIN_URL'," >> ./env-config.js
echo "  VITE_NODE_ENV: '$VITE_NODE_ENV'" >> ./env-config.js
echo "};" >> ./env-config.js
