import React, { useState } from "react";
import { Canvas, ThreeEvent, useLoader } from "@react-three/fiber";
import { OrbitControls } from "@react-three/drei";
import * as THREE from "three";
import { GLTFLoader } from "three/examples/jsm/loaders/GLTFLoader";
import { DRACOLoader } from "three/examples/jsm/loaders/DRACOLoader";
import { getApiClient } from "@src/stores/apiClient";
interface GLTFViewerProps {
  gltfUrl: string;
}

const Model: React.FC<{
  gltfUrl: string;
  onClick: (mesh: THREE.Mesh) => void;
}> = ({ gltfUrl, onClick }) => {
  const gltf = useLoader(GLTFLoader, gltfUrl, (loader) => {
    const dracoLoader = new DRACOLoader();
    dracoLoader.setDecoderPath("https://www.gstatic.com/draco/v1/decoders/");
    (loader as GLTFLoader).setDRACOLoader(dracoLoader);
  });

  gltf.scene.traverse((child) => {
    if ((child as THREE.Mesh).isMesh) {
      const mesh = child as THREE.Mesh;
      mesh.receiveShadow = true;
      mesh.castShadow = true;
      mesh.material = (mesh.material as THREE.Material).clone(); // 개별 색상 제어를 위해 material 복제
    }
  });

  return (
    <primitive
      object={gltf.scene}
      onPointerOver={(e: ThreeEvent<PointerEvent>) => {
        e.stopPropagation();
        const mesh = e.object as THREE.Mesh;
        // setHovered(mesh);
        (mesh.material as THREE.MeshStandardMaterial).color.set("blue"); // Hover 시 파란색으로 변경
      }}
      onPointerOut={(e: ThreeEvent<PointerEvent>) => {
        e.stopPropagation();
        const mesh = e.object as THREE.Mesh;
        // setHovered(null);
        (mesh.material as THREE.MeshStandardMaterial).color.set("white"); // Hover 해제 시 원래 색상으로 변경
      }}
      onPointerDown={(e: ThreeEvent<PointerEvent>) => {
        e.stopPropagation();
        const mesh = e.object as THREE.Mesh;
        onClick(mesh); // 클릭된 mesh 객체 전달
      }}
    />
  );
};
// const apiClient = getApiClient();

const GLTFViewer: React.FC<GLTFViewerProps> = ({ gltfUrl }) => {
  const [selectedMesh, setSelectedMesh] = useState<THREE.Mesh | null>(null);

  // const [modelList, setModelList] = useState();
  // const [hovered, setHovered] = useState<THREE.Mesh | null>(null);
  // const test = async () => {
  //   try {
  //     const res = await apiClient.get("/api/models");
  //     console.log(res);
  //   } catch (err) {
  //     console.log(err);
  //   }
  // };
  const handleMeshClick = (mesh: THREE.Mesh) => {
    setSelectedMesh(mesh); // 클릭된 mesh 상태에 저장
    console.log("Clicked mesh:", mesh.name); // 클릭된 객체 콘솔 출력
  };

  return (
    <Canvas style={{ height: "100vh" }} shadows>
      <ambientLight intensity={0.5} />
      <directionalLight position={[5, 10, 7.5]} intensity={1} castShadow />
      <Model gltfUrl={gltfUrl} onClick={handleMeshClick} />
      <OrbitControls enableDamping />
    </Canvas>
  );
};

export default GLTFViewer;
