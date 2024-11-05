from fastapi import FastAPI
from dotenv import load_dotenv
from pydantic import BaseModel
from typing import List, Tuple
import os
import subprocess
import cadquery as cq
import trimesh
import math
import boto3
import uvicorn
import shutil

# 환경 변수 주입
load_dotenv()

# S3 환경 변수
AWS_REGION = os.getenv("S3_REGION_NAME")
AWS_ACCESS_KEY_ID = os.getenv("S3_PUBLIC_ACCESS_KEY")
AWS_SECRET_ACCESS_KEY = os.getenv("S3_PRIVATE_ACCESS_KEY")
S3_BUCKET_NAME = os.getenv("S3_BUCKET_NAME")

app = FastAPI()

class PipelineModels(BaseModel):
    id: str
    pipelines: List[List[Tuple[float, float]]]
    radius: float = 1

# FastAPI 엔드포인트 설정
@app.post("/pipelineModel")
def create_pipeline_model(data: PipelineModels):
    # HACK: 경로 수정
    work_dir = os.path.join("C:/Users/SSAFY/Downloads/3d", data.id)
    os.makedirs(work_dir, exist_ok=True)

    stl_paths = []

    for i, pipeline_coords in enumerate(data.pipelines):
        pipeline_name = f"PipeObj_{i + 1}"
        stl_paths.extend(create_pipeline(pipeline_coords, data.radius, pipeline_name, work_dir))

    compressed_gltf_path = create_gltf(stl_paths, data.id, work_dir)

    # S3에 업로드
    pipeModel_url = upload_s3(compressed_gltf_path, f"PipeLine_{data.id}.gltf")
    
    # 작업 폴더 삭제
    # shutil.rmtree(work_dir)

    return {"url": pipeModel_url}

# 파이프라인 생성 함수
def create_pipeline(coords, radius, pipeline_name, work_dir):
    stl_paths = []
    start_point = coords[0]
    end_point = coords[1]    
    segment_index = 1
    connector_index = 1

    for index in range(2, len(coords)):
        if check_collinear(start_point, end_point, coords[index]):
            end_point = coords[index]
        else:
            create_cylinder(start_point, end_point, radius, f"{pipeline_name}_Segment_{segment_index}", work_dir, stl_paths)
            create_connector(end_point, radius, f"{pipeline_name}_Connector_{connector_index}", work_dir, stl_paths)
            start_point = end_point
            end_point = coords[index]
            segment_index += 1
            connector_index += 1

    create_cylinder(start_point, end_point, radius, f"{pipeline_name}_Segment_{segment_index}", work_dir, stl_paths)

    return stl_paths

# 직선 판단 함수
def check_collinear(p1, p2, p3):
    ANGLE_TOLERANCE = 10

    vec1 = (p2[0] - p1[0], p2[1] - p1[1])
    vec2 = (p3[0] - p2[0], p3[1] - p2[1])
    angle = abs(math.degrees(math.atan2(vec2[1], vec2[0]) - math.atan2(vec1[1], vec1[0])))

    return angle <= ANGLE_TOLERANCE

# 파이프 생성 함수
def create_cylinder(p1, p2, radius, name, work_dir, stl_paths):
    distance = math.sqrt((p2[0] - p1[0]) ** 2 + (p2[1] - p1[1]) ** 2)
    angle = math.degrees(math.atan2(p2[1] - p1[1], p2[0] - p1[0]))

    cylinder = (
        cq.Workplane("XY")
        .center(0, 0)
        .circle(radius)
        .extrude(distance)
        .rotate((0, 0, 0), (0, 1, 0), 90)
        .rotate((0, 0, 0), (0, 0, 1), angle)
        .translate((p1[0], p1[1], 0))
    )

    cylinder_path = os.path.join(work_dir, f"{name}.stl")
    cylinder.val().exportStl(cylinder_path)
    stl_paths.append(cylinder_path)

# 커넥터 생성 함수
def create_connector(center, radius, name, work_dir, stl_paths):
    connector = cq.Workplane("XY").sphere(radius).translate((center[0], center[1], 0))

    connector_path = os.path.join(work_dir, f"{name}.stl")
    connector.val().exportStl(connector_path)
    stl_paths.append(connector_path)

# GLTF 및 압축 함수
def create_gltf(stl_paths, id, work_dir):
    scene = trimesh.Scene()
    output_gltf_path = os.path.join(work_dir, "pipeline.gltf")
    compressed_gltf_path = os.path.join(work_dir, f"PipeLine_{id}.gltf")

    for stl_path in stl_paths:
        mesh = trimesh.load(stl_path)
        mesh.metadata['name'] = os.path.splitext(os.path.basename(stl_path))[0]
        scene.add_geometry(mesh)

    scene.export(output_gltf_path, file_type='gltf')

    # HACK: 경로 수정
    node_path = r"C:\Program Files\nodejs\node.exe"
    gltf_pipeline_path = r"C:\Users\SSAFY\AppData\Roaming\npm\node_modules\gltf-pipeline\bin\gltf-pipeline.js"

    # 압축
    subprocess.run(
        [node_path, gltf_pipeline_path, "-i", output_gltf_path, "-o", compressed_gltf_path, "-d", "--draco.compressMesh"],
        check=True,
        capture_output=True, text=True
    )

    return compressed_gltf_path

def upload_s3(file_path, s3_key):
    s3_client = boto3.client(
        "s3",
        aws_access_key_id=AWS_ACCESS_KEY_ID,
        aws_secret_access_key=AWS_SECRET_ACCESS_KEY,
        region_name=AWS_REGION
    )

    s3_client.upload_file(file_path, S3_BUCKET_NAME, s3_key)
    s3_url = f"https://{S3_BUCKET_NAME}.s3.{AWS_REGION}.amazonaws.com/{s3_key}"
    
    return s3_url

if __name__ == "__main__":
    # HACK: 호스트 및 포트 수정
    uvicorn.run(app, host="127.0.0.1", port=8000)
