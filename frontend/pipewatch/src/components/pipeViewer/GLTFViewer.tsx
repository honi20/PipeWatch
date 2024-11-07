import React, { useRef, useState } from "react";
import { Canvas, useThree } from "@react-three/fiber";
import * as THREE from "three";
import { PipeModel } from "@src/components/pipeViewer/renderer/PipeModel";
import { CameraControls } from "@react-three/drei";

interface GLTFViewerProps {
  gltfUrl: string;
}

const SceneContent: React.FC<{
  gltfUrl: string;
  cameraControlsRef: React.RefObject<CameraControls>;
}> = ({ gltfUrl, cameraControlsRef }) => {
  const { camera } = useThree();

  const handleModelLoad = (scene: THREE.Object3D) => {
    if (camera instanceof THREE.PerspectiveCamera) {
      const box = new THREE.Box3().setFromObject(scene);
      const size = new THREE.Vector3();
      box.getSize(size);
      const center = new THREE.Vector3();
      box.getCenter(center);

      const maxDim = Math.max(size.x, size.y);
      const fov = camera.fov * (Math.PI / 180);
      let cameraZ = Math.abs(maxDim / (2 * Math.tan(fov / 2)));

      // 약간의 여유를 추가하여 전체 모델이 잘 보이도록 함
      cameraZ *= 1.5;

      // 카메라를 XY 평면을 내려다보는 위치로 설정
      camera.position.set(center.x, center.y, cameraZ);
      camera.up.set(1, 0, 0); // 카메라의 'up' 벡터를 Y 축으로 설정
      camera.lookAt(center.x, center.y, 0);
      camera.far = cameraZ + maxDim * 2;
      camera.updateProjectionMatrix();

      // CameraControls를 사용해 카메라 위치를 조정
      if (cameraControlsRef.current) {
        cameraControlsRef.current.setTarget(center.x, center.y, 0);
        cameraControlsRef.current.fitToBox(scene, true);
      }
    } else {
      console.warn(
        "The camera is not a PerspectiveCamera. Adjustments skipped."
      );
    }
  };

  return (
    <>
      <PipeModel
        gltfUrl={gltfUrl}
        onModelLoad={handleModelLoad}
        onClick={() => {}}
      />
      <CameraControls
        ref={cameraControlsRef}
        enabled={true}
        dollyToCursor={true}
        minDistance={5}
      />
      <axesHelper args={[10]} />
      <gridHelper />
    </>
  );
};

const GLTFViewer: React.FC<GLTFViewerProps> = ({ gltfUrl }) => {
  const cameraControlsRef = useRef<CameraControls>(null!);

  return (
    <Canvas
      style={{ height: "100vh" }}
      camera={{
        near: 1,
        fov: 75,
      }}
    >
      <ambientLight intensity={0.5} />
      <directionalLight position={[5, 10, 7.5]} intensity={1} castShadow />
      <SceneContent gltfUrl={gltfUrl} cameraControlsRef={cameraControlsRef} />
    </Canvas>
  );
};

export default GLTFViewer;
