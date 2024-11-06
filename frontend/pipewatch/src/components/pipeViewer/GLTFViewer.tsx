import React, { useState } from "react";
import { Canvas } from "@react-three/fiber";
import * as THREE from "three";
import { PipeModel } from "@src/components/pipeViewer/renderer/PipeModel";
import { OrbitControls } from "@react-three/drei";

interface GLTFViewerProps {
  gltfUrl: string;
}

const GLTFViewer: React.FC<GLTFViewerProps> = ({ gltfUrl }) => {
  const [selectedMesh, setSelectedMesh] = useState<THREE.Mesh | null>(null);

  const handleMeshClick = (mesh: THREE.Mesh) => {
    setSelectedMesh(mesh); // 클릭된 mesh 상태에 저장
    console.log("Clicked mesh:", mesh.name); // 클릭된 객체 콘솔 출력
  };

  return (
    <Canvas
      style={{ height: "100vh" }}
      camera={{
        near: 1,
        far: 100,
        fov: 75,
        position: [0, 0, 10],
      }}
    >
      <ambientLight intensity={0.5} />
      <directionalLight position={[5, 10, 7.5]} intensity={1} castShadow />
      <PipeModel gltfUrl={gltfUrl} onClick={handleMeshClick} />
      <OrbitControls />
      <axesHelper args={[10]} />
      <gridHelper />
    </Canvas>
  );
};

export default GLTFViewer;
