import { useRef, forwardRef, useImperativeHandle } from "react";
import { useNavigate } from "react-router-dom";

import { Canvas, useLoader } from "@react-three/fiber";
import { OrbitControls } from "@react-three/drei";
import * as THREE from "three";
import { GLTFLoader } from "three/examples/jsm/loaders/GLTFLoader";
import { DRACOLoader } from "three/examples/jsm/loaders/DRACOLoader";

import { getApiClient } from "@src/stores/apiClient";

interface TestRenderingProps {
  modelId: string;
}

// GLTF 모델 경로
const gltfUrl = "/assets/models/PipeLine.gltf";

const Model: React.FC = () => {
  // gltf 파일 가져오기
  const gltf = useLoader(GLTFLoader, gltfUrl, (loader) => {
    const dracoLoader = new DRACOLoader();
    dracoLoader.setDecoderPath("https://www.gstatic.com/draco/v1/decoders/");
    (loader as GLTFLoader).setDRACOLoader(dracoLoader);
  });

  // Shadow, Material 설정
  gltf.scene.traverse((child) => {
    if ((child as THREE.Mesh).isMesh) {
      const mesh = child as THREE.Mesh;
      mesh.receiveShadow = true;
      mesh.castShadow = true;
    }
  });

  // 모델 보여주기
  return <primitive object={gltf.scene} />;
};

const apiClient = getApiClient();

const TestRendering = forwardRef<unknown, TestRenderingProps>(
  ({ modelId }, ref) => {
    const navigate = useNavigate();

    const canvasRef = useRef<HTMLCanvasElement | null>(null);

    const takeScreenshot = () => {
      if (!canvasRef.current) return;

      const canvas = canvasRef.current;

      // Screenshot 찍는 함수
      // const link = document.createElement("a");
      // link.setAttribute("download", `screenshot${modelId}.png`);
      // link.setAttribute(
      //   "href",
      //   canvas.toDataURL("image/png").replace("image/png", "image/octet-stream")
      // );
      // link.click();

      // PATCH 요청 수행하는 함수
      // 캔버스 데이터를 Blob으로 변환
      canvas.toBlob(async (blob) => {
        if (!blob) {
          console.error("Canvas Blob creation failed");
          return;
        }

        // FormData 생성 및 이미지 파일 추가
        const formData = new FormData();

        formData.append("file", blob, `screenshot${modelId}.png`);

        try {
          const response = await apiClient.patch(
            `/api/models/thumbnail/${modelId}`,
            formData,
            {
              headers: {
                "Content-Type": "multipart/form-data",
              },
            }
          );

          console.log("upload thumbnail: ", response.data.header.message);
          navigate("/pipe-generator/completed");
        } catch (error) {
          console.error("Image upload failed:", error);
        }
      }, "image/png");
    };

    // 부모 컴포넌트에서 takeScreenshot 호출 가능하도록 expose
    useImperativeHandle(ref, () => ({
      takeScreenshot,
    }));

    return (
      <Canvas ref={canvasRef} shadows gl={{ preserveDrawingBuffer: true }}>
        <ambientLight intensity={0.5} />
        <directionalLight position={[5, 10, 7.5]} intensity={5} castShadow />
        <Model />
        <OrbitControls enableDamping />
      </Canvas>
    );
  }
);

export default TestRendering;
