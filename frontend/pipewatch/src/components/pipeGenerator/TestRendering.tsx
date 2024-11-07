import { useRef } from "react";
import { Canvas, ThreeEvent, useLoader, useThree } from "@react-three/fiber";
import { OrbitControls } from "@react-three/drei";
import * as THREE from "three";
import { GLTFLoader } from "three/examples/jsm/loaders/GLTFLoader";
import { DRACOLoader } from "three/examples/jsm/loaders/DRACOLoader";

// GLTF 모델 경로
const gltfUrl = "/assets/models/PipeLine.gltf";
//   const canvasRef = useRef<HTMLCanvasElement | null>(null);

const Model: React.FC = () => {
  // canvas내에서만 호출
  const { gl } = useThree();

  // gltf 파일 가져오기
  const gltf = useLoader(GLTFLoader, gltfUrl, (loader) => {
    const dracoLoader = new DRACOLoader();
    dracoLoader.setDecoderPath("https://www.gstatic.com/draco/v1/decoders/");
    (loader as GLTFLoader).setDRACOLoader(dracoLoader);
  });

  // Screenshot 찍는 함수
  const takeScreenshot = () => {
    const link = document.createElement("a");
    link.setAttribute("download", "screenshot.png");
    link.setAttribute(
      "href",
      gl.domElement
        .toDataURL("image/png")
        .replace("image/png", "image/octet-stream")
    );
    link.click();
  };

  // Shadow, Material 설정
  gltf.scene.traverse((child) => {
    if ((child as THREE.Mesh).isMesh) {
      const mesh = child as THREE.Mesh;
      mesh.receiveShadow = true;
      mesh.castShadow = true;
    }
  });

  // 모델 보여주기
  return (
    <primitive
      object={gltf.scene}
      onPointerDown={(e: ThreeEvent<PointerEvent>) => {
        e.stopPropagation();

        /////// 스크린샷 찍기 ///////
        takeScreenshot();
      }}
    />
  );
};

const TestRendering = () => {
  const canvasRef = useRef<HTMLCanvasElement | null>(null);
  return (
    <Canvas ref={canvasRef} shadows gl={{ preserveDrawingBuffer: true }}>
      <ambientLight intensity={0.5} />
      <directionalLight position={[5, 10, 7.5]} intensity={5} castShadow />
      <Model />
      <OrbitControls enableDamping />
    </Canvas>
  );
};

export default TestRendering;
