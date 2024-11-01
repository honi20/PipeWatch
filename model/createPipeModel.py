import cadquery as cq
import trimesh
import os
import math
import subprocess

# 좌표 리스트
coordinates = [
    (0, 0), (5, 0), (10, 0), (15, 0), (15, 10),
    (15, 15), (15, 20), (10, 20), (5, 20), (5, 25),
    (5, 30), (5, 35), (10, 35), (15, 35), (20, 35)
]

# 파일 경로 설정
output_directory = "C:/Users/SSAFY/Downloads/3d"
os.makedirs(output_directory, exist_ok=True)
stl_paths = []
output_gltf_path = os.path.join(output_directory, "pipeline.gltf")
compressed_gltf_path = os.path.join(output_directory, "pipeline_compressed.gltf")

# 파이프라인 생성 함수
def create_pipeline(coords, radius=0.5):
    start_point = coords[0]
    end_point = coords[1]
    
    # 세그먼트 생성
    for index in range(2, len(coords) - 1):
        if check_collinear(start_point, end_point, coords[index]):
            end_point = coords[index]
        else: 
            # 원통 생성
            create_cylinder(start_point, end_point, radius, index - 1)

            # 커넥터 생성
            create_connector(end_point, radius, index - 1)
            
            start_point = end_point
            end_point = coords[index]

    # 마지막 세그먼트 생성
    create_cylinder(start_point, end_point, radius, "last")

# 직선 판단 함수
def check_collinear(p1, p2, p3):
    return (p2[1] - p1[1]) * (p3[0] - p2[0]) == (p3[1] - p1[1]) * (p2[0] - p1[0])

# 파이프 생성 함수
def create_cylinder(p1, p2, radius, index):
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

    # STL 파일로 저장
    cylinder_path = os.path.join(output_directory, f"cylinder_{index}.stl")
    cylinder.val().exportStl(cylinder_path)
    stl_paths.append(cylinder_path)

# 커넥터 생성 함수
def create_connector(center, radius, index):
    connector = cq.Workplane("XY").sphere(radius).translate((center[0], center[1], 0))
    
    # STL 파일로 저장
    connector_path = os.path.join(output_directory, f"connector_{index}.stl")
    connector.val().exportStl(connector_path)
    stl_paths.append(connector_path)

# 파이프라인 모델 생성
create_pipeline(coordinates)

# GLTF Scene 생성
scene = trimesh.Scene()

# Scene에 추가
for index, stl_path in enumerate(stl_paths):
    mesh = trimesh.load(stl_path)
    mesh.metadata['name'] = f"object_{index}"
    scene.add_geometry(mesh)

# GLTF 파일로 저장
scene.export(output_gltf_path, file_type='gltf')

# Draco 압축
def draco_compression(input_path, output_path):
    node_path = r"C:\Program Files\nodejs\node.exe"
    gltf_pipeline_path = r"C:\Users\SSAFY\AppData\Roaming\npm\node_modules\gltf-pipeline\bin\gltf-pipeline.js"
    
    subprocess.run(
        [node_path, gltf_pipeline_path, "-i", input_path, "-o", output_path, "-d", "--draco.compressMesh"],
        check=True,
        capture_output=True, text=True
    )

# 압축
draco_compression(output_gltf_path, compressed_gltf_path)

# 임시 STL 파일 삭제
for stl_path in stl_paths:
    os.remove(stl_path)

# 원본 GLTF 파일 삭제
os.remove(output_gltf_path)

# .bin 파일 삭제
for file in os.listdir(output_directory):
    if file.endswith('.bin'):
        os.remove(os.path.join(output_directory, file))