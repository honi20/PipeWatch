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

from ultralytics import YOLO
import cv2
from IPython.display import display
from scipy.spatial import distance
from util import find_intersections, convert_numpy_types, create_model
from sklearn.decomposition import PCA

import logging
import numpy as np
import uvicorn
from fastapi import FastAPI, File, UploadFile, Form
from PIL import Image

app = FastAPI()
trained_model=YOLO("./DLModel/best.pt")
result ={}

logging.basicConfig(level=logging.DEBUG)

@app.post("/inference")
async def inference(modelUuid: str = Form(...), file: UploadFile = File(...)):
    contents = await file.read()

    nparr = np.frombuffer(contents, np.uint8)

    image = cv2.imdecode(nparr, cv2.IMREAD_COLOR)

    # OpenCV로 이미지 크기 변경
    resized_image = cv2.resize(image, (640, 640))

    results = trained_model(resized_image, conf=0.5, iou=0.1)
    pipes = []
    elbows = []

    if results:
        r = results[0]
        im_array = r.plot()
        im = Image.fromarray(im_array[..., ::-1])
        display(im)

        if r.masks:
            masks = r.masks
            masks = masks.data.cpu().numpy()
            boxes = r.boxes.xyxy.cpu().numpy()
            class_ids = r.boxes.cls.cpu().numpy()

            # Prepare for PCA and visualization
            im_pca = Image.fromarray(im_array[..., ::-1])

            # for i, mask in enumerate(masks):
            for i, (mask, class_id) in enumerate(zip(masks, class_ids)):
                y_coords, x_coords = np.where(mask)
                coordinates = np.column_stack((x_coords, y_coords))
                x_min, y_min, x_max, y_max = boxes[i]

                if class_id != 1:
                    center_point = ((x_min+x_max)/2, (y_min+y_max)/2)
                    elbows.append(center_point)
                    continue

                if len(coordinates) > 1:
                    pca = PCA(n_components=2)
                    pca.fit(coordinates)

                    center = coordinates.mean(axis=0)
                    direction = pca.components_[0]

                    scale_factor = 300
                    end_point1 = center + direction * scale_factor
                    end_point2 = center - direction * scale_factor

                    line_start, line_end = find_intersections(end_point1, end_point2, x_min, y_min, x_max, y_max)

                    pipes.append([list(line_start), list(line_end)])

            for elbow in elbows:
                distances = []
                for pipe in pipes:
                    for point in pipe:
                        dist = distance.euclidean(elbow, point)
                        distances.append((dist, point))

                closest_points = sorted(distances, key=lambda x: x[0])[:2]

                for _, closest_point in closest_points:
                    for pipe in pipes:
                        for j, point in enumerate(pipe):
                            if np.array_equal(point, closest_point):
                                pipe[j] = list(elbow)

    if len(pipes)==0:
        print("False")
        return False
    pipes = convert_numpy_types(pipes)
    scaled_pipes = [[[x / 20 for x in point] for point in pipe] for pipe in pipes]
    
    result[modelUuid] = scaled_pipes

    return True

# 모델링 생성 요청 API
@app.post("/modeling")
def modeling(modelUuid: str = Form(...)):
    response = create_model(result[modelUuid], modelUuid)
    del result[modelUuid]

    return response

if __name__ == "__main__":
    uvicorn.run(app, host=host, port=int(port))

# NOTE:
# 포트 오류 -> Stop-Process -Name python -Force