from fastapi import FastAPI
from pydantic import BaseModel
from typing import List, Tuple
import os
import subprocess
import cadquery as cq
import trimesh
import math

app = FastAPI()

# 경로 설정 (서버 임의 폴더로 대체 예정)
output_directory = "C:/Users/SSAFY/Downloads/3d"
os.makedirs(output_directory, exist_ok=True)

class CoordinateModel(BaseModel):
    coordinates: List[Tuple[float, float]]
    radius: float = 1
    id: int

# FastAPI 엔드포인트 설정
@app.post("/pipelineModel")
def create_pipeline_model(data: CoordinateModel):
    stl_paths = create_pipeline(data.coordinates, data.radius)
    compressed_gltf_path = create_and_compress_gltf(stl_paths)
    
    # 임시 파일 삭제
    for stl_path in stl_paths:
        os.remove(stl_path)
    
    for file in os.listdir(output_directory):
        if file.endswith('.bin') or file == "pipeline.gltf":
            os.remove(os.path.join(output_directory, file))

    return {"compressed_gltf_path": compressed_gltf_path}

# 파이프라인 생성 함수
def create_pipeline(coords, radius):
    stl_paths = []
    start_point = coords[0]
    end_point = coords[1]
    
    for index in range(2, len(coords) - 1):
        if check_collinear(start_point, end_point, coords[index]):
            end_point = coords[index]
        else:
            create_cylinder(start_point, end_point, radius, index - 1, stl_paths)
            create_connector(end_point, radius, index - 1, stl_paths)
            start_point = end_point
            end_point = coords[index]

    create_cylinder(start_point, end_point, radius, "last", stl_paths)

    return stl_paths

# 직선 판단 함수
def check_collinear(p1, p2, p3):
    return (p2[1] - p1[1]) * (p3[0] - p2[0]) == (p3[1] - p1[1]) * (p2[0] - p1[0])

# 파이프 생성 함수
def create_cylinder(p1, p2, radius, index, stl_paths):
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
    cylinder_path = os.path.join(output_directory, f"cylinder_{index}.stl")
    cylinder.val().exportStl(cylinder_path)
    stl_paths.append(cylinder_path)

# 커넥터 생성 함수
def create_connector(center, radius, index, stl_paths):
    connector = cq.Workplane("XY").sphere(radius).translate((center[0], center[1], 0))
    connector_path = os.path.join(output_directory, f"connector_{index}.stl")
    connector.val().exportStl(connector_path)
    stl_paths.append(connector_path)

# GLTF 및 압축 함수
def create_and_compress_gltf(stl_paths):
    scene = trimesh.Scene()
    output_gltf_path = os.path.join(output_directory, "pipeline.gltf")
    compressed_gltf_path = os.path.join(output_directory, "pipeline_compressed.gltf")

    for index, stl_path in enumerate(stl_paths):
        mesh = trimesh.load(stl_path)
        mesh.metadata['name'] = f"object_{index}"
        scene.add_geometry(mesh)

    scene.export(output_gltf_path, file_type='gltf')    
    node_path = r"C:\Program Files\nodejs\node.exe"
    gltf_pipeline_path = r"C:\Users\SSAFY\AppData\Roaming\npm\node_modules\gltf-pipeline\bin\gltf-pipeline.js"
    subprocess.run(
        [node_path, gltf_pipeline_path, "-i", output_gltf_path, "-o", compressed_gltf_path, "-d", "--draco.compressMesh"],
        check=True,
        capture_output=True, text=True
    )

    return compressed_gltf_path

# TODO:
# - 서버 임시 경로 설정하기
# - s3 업로드 로직 구현
# - api 호출해서 보내주기 (id long)
# - gltf 전체 삭제
# - 좌표 보정하기
# - 파일 업로드 후 url 반환하기
# - 썸네일 만들고 url 반환하기
# - requirements 업데이트 하기