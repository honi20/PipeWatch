# Python 3.9.20 이미지 기반으로 시작
FROM nikolaik/python-nodejs:python3.9-nodejs22-slim

# Node.js 설치를 위해 필요한 패키지 추가 및 Node.js 설치
RUN apt-get update && \
    apt-get install -y wget llvm-6.0 llvm-6.0-tools freeglut3 freeglut3-dev && \
    wget https://github.com/mmatl/travis_debs/raw/master/xenial/mesa_18.3.3-0.deb && \
    dpkg -i ./mesa_18.3.3-0.deb || true && \
    apt-get install -f -y && \
    apt-get install -y libglu1-mesa libgl1-mesa-glx libgl1-mesa-dev && \
    npm install -g gltf-pipeline && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# 작업 디렉터리 설정
WORKDIR /app/

# 필요한 파일 복사
COPY createPipeModel.py requirements.txt /app/

# Python 가상 환경 설정 및 종속성 설치
RUN python -m venv venv && \
    . venv/bin/activate && \
    pip install --upgrade pip && \
    pip install -r requirements.txt

# 가상 환경 활성화 후 앱 실행
EXPOSE 8000
CMD ["sh", "-c", ". venv/bin/activate && uvicorn --host=0.0.0.0 --port 8000 createPipeModel:app"]
