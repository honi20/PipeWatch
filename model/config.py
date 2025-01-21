import os 
from dotenv import load_dotenv

# 환경 변수 주입
load_dotenv()

import boto3

# S3 환경 변수
AWS_REGION = os.getenv("S3_REGION_NAME")
AWS_ACCESS_KEY_ID = os.getenv("S3_PUBLIC_ACCESS_KEY")
AWS_SECRET_ACCESS_KEY = os.getenv("S3_PRIVATE_ACCESS_KEY")
S3_BUCKET_NAME = os.getenv("S3_BUCKET_NAME")

# S3 클라이언트 생성
S3_client = boto3.client(
    "s3",
    aws_access_key_id=AWS_ACCESS_KEY_ID,
    aws_secret_access_key=AWS_SECRET_ACCESS_KEY,
    region_name=AWS_REGION
 )

# 경로 설정
NODE_PATH = os.getenv("NODE_PATH")
GLTF_PIPELINE_PATH = os.getenv("GLTF_PIPELINE_PATH")
BASE_WORK_DIR = os.getenv("BASE_WORK_DIR")
