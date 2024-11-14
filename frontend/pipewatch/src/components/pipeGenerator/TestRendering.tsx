import { useRef, forwardRef, useImperativeHandle } from "react";
import { useNavigate } from "react-router-dom";

import { Canvas, useLoader } from "@react-three/fiber";
import { OrbitControls } from "@react-three/drei";
import * as THREE from "three";
import { GLTFLoader } from "three/examples/jsm/loaders/GLTFLoader";
import { DRACOLoader } from "three/examples/jsm/loaders/DRACOLoader";

import { getApiClient } from "@src/stores/apiClient";

interface TestRenderingProps {
  gltfUrl: string;
  modelId: string;
}

const TestRendering = forwardRef<unknown, TestRenderingProps>(
  ({ gltfUrl, modelId }, ref) => {
    const Model: React.FC = () => {
      // gltf 파일 가져오기
      const gltf = useLoader(GLTFLoader, gltfUrl, (loader) => {
        const dracoLoader = new DRACOLoader();
        dracoLoader.setDecoderPath(
          "https://www.gstatic.com/draco/v1/decoders/"
        );
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

    const navigate = useNavigate();

    const canvasRef = useRef<HTMLCanvasElement | null>(null);

    const takeScreenshot = () => {
      if (!canvasRef.current) return;

      const canvas = canvasRef.current;

      // PATCH 요청 수행하는 함수
      canvas.toBlob(async (blob) => {
        if (!blob) {
          console.error("Canvas Blob creation failed");
          return;
        }

        const formData = new FormData();

        formData.append("file", blob, `screenshot${modelId}.png`);

        try {
          const apiClient = getApiClient();
          const response = await apiClient.patch(
            `/api/models/${modelId}/thumbnail`,
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
      <Canvas
        ref={canvasRef}
        shadows
        gl={{ preserveDrawingBuffer: true }}
        style={{ background: "#ffffff" }}
      >
        <ambientLight intensity={0.5} />
        <directionalLight position={[5, 10, 7.5]} intensity={5} castShadow />
        <Model />
        <OrbitControls enableDamping />
      </Canvas>
    );
  }
);

export default TestRendering;
