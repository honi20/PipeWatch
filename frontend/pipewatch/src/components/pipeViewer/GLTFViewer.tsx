import React, { useRef } from "react";
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
  const { camera, scene } = useThree();
  const helper = new THREE.CameraHelper(camera);
  scene.add(helper);

  // 모델 로드 시 카메라 제어
  const adjustCameraOnModelLoad = (scene: THREE.Object3D) => {
    if (camera instanceof THREE.PerspectiveCamera) {
      const box = new THREE.Box3().setFromObject(scene);
      const size = new THREE.Vector3();
      box.getSize(size);
      const center = new THREE.Vector3();
      box.getCenter(center);

      // Z축에서 XY 평면을 바라보도록 카메라 위치 조정
      // const maxDim = Math.max(size.x, size.y, size.z);
      // const fov = camera.fov * (Math.PI / 180);
      // const cameraZ = Math.abs(maxDim / (2 * Math.tan(fov / 2)));
      // 카메라를 Z축에서 XY 평면을 바라보는 위치로 설정
      // camera.position.set(0, 0, cameraZ + maxDim * 1.5); // Z축 상에서 일정 거리 뒤에 위치
      camera.position.set(0, 5, 0);
      camera.lookAt(0, 1, 0); // XY 평면의 중심을 향해 회전

      if (cameraControlsRef.current) {
        cameraControlsRef.current.setTarget(0, 1, 0, true);
        cameraControlsRef.current.fitToBox(scene, true, {
          paddingLeft: 20,
          paddingRight: 20,
        });
        console.log(" gltfviewer");
      }

      console.log(
        "Camera adjusted to view from Z-axis:",
        camera.position,
        cameraControlsRef
      );
    } else {
      console.warn("Camera type is not PerspectiveCamera");
    }
  };
  return (
    <>
      <PipeModel
        gltfUrl={gltfUrl}
        onModelLoad={adjustCameraOnModelLoad}
        cameraControlsRef={cameraControlsRef}
      />
      <CameraControls
        ref={cameraControlsRef}
        enabled={true}
        dollyToCursor={true}
        minDistance={5}
        // maxDistance={1000}
      />
      <axesHelper args={[10]} />
      <gridHelper />
    </>
  );
};

const GLTFViewer: React.FC<GLTFViewerProps> = ({ gltfUrl }) => {
  const cameraControlsRef = useRef<CameraControls>(null!);

  return (
    <Canvas style={{ height: "100vh" }}>
      <ambientLight intensity={1} />
      <directionalLight position={[5, 10, 7.5]} intensity={1} castShadow />
      <directionalLight position={[5, 10, -7.5]} intensity={1} castShadow />
      <SceneContent gltfUrl={gltfUrl} cameraControlsRef={cameraControlsRef} />
    </Canvas>
  );
};

export default GLTFViewer;
