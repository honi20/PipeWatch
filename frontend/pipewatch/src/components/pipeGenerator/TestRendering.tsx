import { useRef, forwardRef, useImperativeHandle } from "react";
import {
  Canvas,
  // ThreeEvent,
  useLoader,
} from "@react-three/fiber";
import { OrbitControls } from "@react-three/drei";
import * as THREE from "three";
import { GLTFLoader } from "three/examples/jsm/loaders/GLTFLoader";
import { DRACOLoader } from "three/examples/jsm/loaders/DRACOLoader";

// GLTF 모델 경로
const gltfUrl = "/assets/models/PipeLine.gltf";

const Model: React.FC = () => {
  // canvas내에서만 호출
  //   const { gl } = useThree();

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
  return (
    <primitive
      object={gltf.scene}
      //   onPointerDown={(e: ThreeEvent<PointerEvent>) => {
      //     e.stopPropagation();
      //   }}
    />
  );
};

// const TestRendering = () => {
//   const canvasRef = useRef<HTMLCanvasElement | null>(null);
const TestRendering = forwardRef((_, ref) => {
  const canvasRef = useRef<HTMLCanvasElement | null>(null);
  // Screenshot 찍는 함수
  const takeScreenshot = () => {
    if (!canvasRef.current) return;

    const canvas = canvasRef.current;
    const link = document.createElement("a");
    link.setAttribute("download", "screenshot.png");
    link.setAttribute(
      "href",
      canvas.toDataURL("image/png").replace("image/png", "image/octet-stream")
    );
    link.click();
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
      //   onClick={takeScreenshot}
    >
      <ambientLight intensity={0.5} />
      <directionalLight position={[5, 10, 7.5]} intensity={5} castShadow />
      <Model />
      <OrbitControls enableDamping />
    </Canvas>
  );
});

export default TestRendering;
