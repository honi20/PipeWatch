import os
from dotenv import load_dotenv

# 환경 변수 주입
load_dotenv()

# 개발 환경에 따른 변수 주입
if os.getenv("ENVIRONMENT") == "dev":
    host=os.getenv("LOCAL_HOST")
    port=os.getenv("LOCAL_PORT")
else:
    os.environ['PYOPENGL_PLATFORM'] = 'osmesa'
    host=os.getenv("SERVER_HOST")
    port=os.getenv("SERVER_PORT")

import math
import shutil
import subprocess
from typing import List

import boto3
import cadquery as cq
import numpy as np
import pyrender
import requests
import trimesh
import uvicorn
from fastapi import FastAPI
from PIL import Image
from pydantic import BaseModel

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

app = FastAPI()

# 경로 설정
NODE_PATH = os.getenv("NODE_PATH")
GLTF_PIPELINE_PATH = os.getenv("GLTF_PIPELINE_PATH")
BASE_WORK_DIR = os.getenv("BASE_WORK_DIR")

# 파이프 모델 생성 요청
class CreateModelRequest(BaseModel):
    modelUuid: str
    coords: List[List[List[float]]]
    radius: float = 1

# 파이프라인 모델 생성 api
@app.post("/pipelineModel")
def create_model(data: CreateModelRequest):
    work_dir = os.path.join(BASE_WORK_DIR, data.modelUuid)
    os.makedirs(work_dir, exist_ok=True)

    # 파이프 분류
    pipelines = segment_pipelines(data.coords)

    # 파일 생성
    stl_paths = create_stl_files(pipelines, data.radius, work_dir)
    gltf_path = os.path.join(work_dir, f"origin_Pipeline_{data.modelUuid}.gltf")
    compressed_gltf_path = create_gltf(data.modelUuid, stl_paths, gltf_path, work_dir)

    # gltf S3 업로드
    model_key = f"models/PipeLine_{data.modelUuid}.gltf"
    pipeModel_URL = upload_S3(compressed_gltf_path, model_key)

    # 썸네일 생성
    thumbnail_path = os.path.join(work_dir, f"Thumbnail_{data.modelUuid}.png")
    create_thumbnail(gltf_path, thumbnail_path)

    # 썸네일 S3 업로드
    thumbnail_key = f"thumbnails/Thumbnail_{data.modelUuid}.png"
    thumbnail_URL = upload_S3(thumbnail_path, thumbnail_key)

    # 작업 폴더 삭제
    shutil.rmtree(work_dir)

    # 결과 전송
    BE_response = send_data(data.modelUuid, pipeModel_URL, thumbnail_URL)

    return {"BE_response": BE_response, "pipeModel_URL": pipeModel_URL, "thumbnail_URL": thumbnail_URL}

# 파이프 분류 함수
def segment_pipelines(request_coords: List[List[List[float]]]):
    pipelines = []

    for coord_pair in request_coords:
        pipe_coord1, pipe_coord2 = coord_pair[0], coord_pair[1]
        pipe_coord1_connected_pipeline = None
        pipe_coord2_connected_pipeline = None
        
        # 파이프 연결 가능 여부 조회
        for pipeline in pipelines:
            if pipe_coord1 == pipeline[0] or pipe_coord1 == pipeline[-1]:
                pipe_coord1_connected_pipeline = pipeline

            if pipe_coord2 == pipeline[0] or pipe_coord2 == pipeline[-1]:
                pipe_coord2_connected_pipeline = pipeline

            if pipe_coord1_connected_pipeline and pipe_coord2_connected_pipeline:
                break

        # 새 파이프가 두 파이프라인 연결 시
        if pipe_coord1_connected_pipeline and pipe_coord2_connected_pipeline:
            if pipe_coord1_connected_pipeline[-1] == pipe_coord1:
                pipe_coord1_connected_pipeline.extend(
                pipe_coord2_connected_pipeline if pipe_coord2_connected_pipeline[0] == pipe_coord2 else pipe_coord2_connected_pipeline[::-1]
            )
            elif pipe_coord1_connected_pipeline[0] == pipe_coord1:
                pipe_coord1_connected_pipeline[:0] = (
                pipe_coord2_connected_pipeline if pipe_coord2_connected_pipeline[-1] == pipe_coord2 else pipe_coord2_connected_pipeline[::-1]
            )
                
            pipelines.remove(pipe_coord2_connected_pipeline)

        # 새 파이프가 한 파이프라인에 속할 시
        elif pipe_coord1_connected_pipeline:
            if pipe_coord1_connected_pipeline[-1] == pipe_coord1:
                pipe_coord1_connected_pipeline.append(pipe_coord2)
            elif pipe_coord1_connected_pipeline[0] == pipe_coord1:
                pipe_coord1_connected_pipeline.insert(0, pipe_coord2)

        elif pipe_coord2_connected_pipeline:
            if pipe_coord2_connected_pipeline[-1] == pipe_coord2:
                pipe_coord2_connected_pipeline.append(pipe_coord1)
            elif pipe_coord2_connected_pipeline[0] == pipe_coord2:
                pipe_coord2_connected_pipeline.insert(0, pipe_coord1)
                
        # 새 파이프가 새로운 파이프라인 형성 시
        else:
            pipelines.append([pipe_coord1, pipe_coord2])

    return pipelines

# STL 파일 생성
def create_stl_files(pipelines_coords, radius, work_dir):
    stl_paths = []
    segment_index = 1

    for pipeline_coords in pipelines_coords:
        pipeline_name = "PipeObj_1"
        new_stl_paths, segment_index = create_pipeline(pipeline_coords, radius, pipeline_name, work_dir, segment_index)
        stl_paths.extend(new_stl_paths)

    return stl_paths

# 파이프라인 생성 함수
def create_pipeline(pipeline_coords, radius, pipeline_name, work_dir, segment_index):
    stl_paths = []
    start_coord = pipeline_coords[0]
    end_coord = pipeline_coords[1]

    for index in range(2, len(pipeline_coords)):
        if check_collinear(start_coord, end_coord, pipeline_coords[index]):
            end_coord = pipeline_coords[index]
        else:
            create_cylinder(start_coord, end_coord, radius, f"{pipeline_name}_Segment_{segment_index}", work_dir, stl_paths)
            create_connector(end_coord, radius, f"{pipeline_name}_Connector_{segment_index}", work_dir, stl_paths)
            
            start_coord = end_coord
            end_coord = pipeline_coords[index]
            segment_index += 1

    create_cylinder(start_coord, end_coord, radius, f"{pipeline_name}_Segment_{segment_index}", work_dir, stl_paths)
    segment_index += 1

    return stl_paths, segment_index

# 직선 판단 함수
def check_collinear(p1, p2, p3):
    ANGLE_TOLERANCE = 10

    vec1 = (p2[0] - p1[0], p2[1] - p1[1])
    vec2 = (p3[0] - p2[0], p3[1] - p2[1])

    # 벡터 내적
    dot_product = vec1[0] * vec2[0] + vec1[1] * vec2[1]
    mag1 = math.sqrt(vec1[0] ** 2 + vec1[1] ** 2)
    mag2 = math.sqrt(vec2[0] ** 2 + vec2[1] ** 2)
    angle = math.degrees(math.acos(dot_product / (mag1 * mag2)))

    return angle <= ANGLE_TOLERANCE

# 파이프 생성 함수
def create_cylinder(start_coord, end_coord, radius, name, work_dir, stl_paths):
    distance = math.sqrt((end_coord[0] - start_coord[0]) ** 2 + (end_coord[1] - start_coord[1]) ** 2)
    angle = math.degrees(math.atan2(end_coord[1] - start_coord[1], end_coord[0] - start_coord[0]))

    cylinder = (
        cq.Workplane("XY")
        .center(0, 0)
        .circle(radius)
        .extrude(distance)
        .rotate((0, 0, 0), (0, 1, 0), 90)
        .rotate((0, 0, 0), (0, 0, 1), angle)
        .translate((start_coord[0], start_coord[1], 0))
    )

    cylinder_path = os.path.join(work_dir, f"{name}.stl")
    cylinder.val().exportStl(cylinder_path)
    stl_paths.append(cylinder_path)

    # 플렌지 좌표 계산
    p1_flange_x = start_coord[0] + (radius * 2) * math.cos(math.radians(angle))
    p1_flange_y = start_coord[1] + (radius * 2) * math.sin(math.radians(angle))

    p2_flange_x = end_coord[0] - (radius * 2) * math.cos(math.radians(angle))
    p2_flange_y = end_coord[1] - (radius * 2) * math.sin(math.radians(angle))

    # 플랜지 생성
    create_flange((p1_flange_x, p1_flange_y), radius, name + "_Flange_1", work_dir, stl_paths, angle)
    create_flange((p2_flange_x, p2_flange_y), radius, name + "_Flange_2", work_dir, stl_paths, angle)

# 플렌지 생성 함수
def create_flange(center, radius, name, work_dir, stl_paths, angle):
    distance = 0.2
    flange_radius = radius * 1.5

    flange = (
        cq.Workplane("XY")
        .center(0, 0)
        .circle(flange_radius)
        .extrude(distance)
        .rotate((0, 0, 0), (0, 1, 0), 90)
        .rotate((0, 0, 0), (0, 0, 1), angle)
        .translate((center[0], center[1], 0))
    )

    flange_path = os.path.join(work_dir, f"{name}.stl")
    flange.val().exportStl(flange_path)
    stl_paths.append(flange_path)

# 커넥터 생성 함수
def create_connector(center, radius, name, work_dir, stl_paths):
    connector = cq.Workplane("XY").sphere(radius).translate((center[0], center[1], 0))

    connector_path = os.path.join(work_dir, f"{name}.stl")
    connector.val().exportStl(connector_path)
    stl_paths.append(connector_path)

# GLTF 생성 함수
def create_gltf(modelUuid, stl_paths, gltf_path, work_dir):
    scene = trimesh.Scene()

    # 각 STL 파일을 GLTF에 추가
    for stl_path in stl_paths:
        mesh = trimesh.load(stl_path)
        mesh.metadata['name'] = os.path.splitext(os.path.basename(stl_path))[0]
        scene.add_geometry(mesh)

    # GLTF 파일 생성
    scene.export(gltf_path, file_type='gltf')

    # 압축 GLTF 파일 생성
    compressed_gltf_path = os.path.join(work_dir, f"PipeLine_{modelUuid}.gltf")
    compress_gltf(gltf_path, compressed_gltf_path)
    
    return compressed_gltf_path

# draco 압축 함수
def compress_gltf(input_path, output_path):
    subprocess.run(
        [NODE_PATH, GLTF_PIPELINE_PATH, "-i", input_path, "-o", output_path, "-d", "--draco.compressMesh"],
        check=True, capture_output=True, text=True
    )
    
# 썸네일 생성 함수
def create_thumbnail(gltf_path, thumbnail_path):
    # gltf 디렉토리 설정
    scene = trimesh.load(gltf_path, resolver=trimesh.resolvers.FilePathResolver(os.path.dirname(gltf_path)))
    
    # Trimesh 씬을 Pyrender 씬으로 변환
    pyrender_scene = pyrender.Scene()
    for geometry in scene.geometry.values():
        mesh = pyrender.Mesh.from_trimesh(geometry)
        pyrender_scene.add(mesh)
    
    # 범위 계산
    bounds = scene.bounds
    min_bound, max_bound = bounds[0], bounds[1]
    center = (min_bound + max_bound) / 2
    size = np.linalg.norm(max_bound - min_bound)

    # 카메라 위치 설정
    camera_distance = size
    camera_pose = np.array([
        [1.0, 0.0, 0.0, center[0]],
        [0.0, 1.0, 0.0, center[1]],
        [0.0, 0.0, 1.0, center[2] + camera_distance],
        [0.0, 0.0, 0.0, 1.0],
    ])
    
    # 카메라 설정
    camera = pyrender.PerspectiveCamera(yfov=np.pi / 3.0)
    pyrender_scene.add(camera, pose=camera_pose)
    
    # 조명 설정
    light = pyrender.DirectionalLight(color=np.ones(3), intensity=2.0)
    pyrender_scene.add(light, pose=camera_pose)

    # 씬 렌더링
    r = pyrender.OffscreenRenderer(640, 480)
    color, _ = r.render(pyrender_scene)
    
    # 이미지 저장
    image = Image.fromarray(color)
    image.save(thumbnail_path)

# S3 업로드 함수
def upload_S3(file_path, S3_key):
    S3_client.upload_file(file_path, S3_BUCKET_NAME, S3_key)
    S3_URL = f"https://{S3_BUCKET_NAME}.s3.{AWS_REGION}.amazonaws.com/{S3_key}"
    
    return S3_URL

# 결과 전송 함수
def send_data(model_uuid: str, model_url: str, preview_img_url: str) -> dict:
    target_url = "https://api.pipewatch.co.kr/api/models/modeling"
    
    # NOTE:
    # BE 변수명 변경 시 동일하게 변경 필요
    payload = {
        "modelUuid": model_uuid,
        "modelUrl": model_url,
        "previewImgUrl": preview_img_url
    }

    BE_response = requests.post(target_url, json=payload)

    # NOTE:
    # BE 데이터 전송 성공 여부 판단
    # 불필요 할 경우 주석 처리
    if BE_response.status_code == 201:
        return {
            "status": "성공",
            "message": "결과 전송 완료",
            "pipeModel_URL": model_url,
            "thumbnail_URL": preview_img_url
        }
    else:
        return {
            "status": "실패",
            "message": "결과 전송 실패",
            "error": BE_response.text
        }

if __name__ == "__main__":
    uvicorn.run(app, host=host, port=int(port))

# NOTE:
# 포트 오류 -> Stop-Process -Name python -Force