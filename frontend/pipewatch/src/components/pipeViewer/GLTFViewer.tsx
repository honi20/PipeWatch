import React, { useRef, useState } from "react";
import { Canvas } from "@react-three/fiber";
import * as THREE from "three";
import { PipeModel } from "@src/components/pipeViewer/renderer/PipeModel";
import { CameraControls } from "@react-three/drei";

interface GLTFViewerProps {
  gltfUrl: string;
}

const GLTFViewer: React.FC<GLTFViewerProps> = ({ gltfUrl }) => {
  const [selectedMesh, setSelectedMesh] = useState<THREE.Mesh | null>(null);
  const cameraControlsRef = useRef<CameraControls>(null!);

  const handleMeshClick = (mesh: THREE.Mesh) => {
    setSelectedMesh(mesh); // 클릭된 mesh 상태에 저장
    console.log(
      "Clicked mesh:",
      mesh.name,
      mesh.position.z,
      mesh.position.y,
      mesh.position.z
    ); // 클릭된 객체 콘솔 출력
    cameraControlsRef.current.fitToBox(mesh, true);
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
      <CameraControls
        ref={cameraControlsRef}
        enabled={true}
        dollyToCursor={true}
        minDistance={5}
        // maxDistance={20}
      />
      <axesHelper args={[10]} />
      <gridHelper />
    </Canvas>
  );
};

export default GLTFViewer;
