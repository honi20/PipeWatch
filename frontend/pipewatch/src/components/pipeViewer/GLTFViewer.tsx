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
      // console.log(box.getSize(size));
      const maxDim = Math.max(size.x, size.y);
      const fov = camera.fov * (Math.PI / 180);

      const cameraZ = Math.abs(maxDim / (2 * Math.tan(fov / 2)));
      // console.log(cameraZ);
      if (cameraControlsRef.current) {
        // const distance = 2 * box.getSize(new THREE.Vector3()).length();
        camera.position.set(center.z, center.y, cameraZ);
        console.log(camera.position);
        cameraControlsRef.current.setTarget(center.x, center.y, cameraZ, true);
        cameraControlsRef.current.fitToBox(scene, true, {
          paddingLeft: size.x / 2,
          paddingRight: size.x / 2,
        });
      }
    } else {
      console.warn("alert 창 필요");
    }
  };

  return (
    <>
      <PipeModel gltfUrl={gltfUrl} onModelLoad={adjustCameraOnModelLoad} />
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
      }}
    >
      <ambientLight intensity={0.5} />
      <directionalLight position={[5, 10, -7.5]} intensity={1} castShadow />
      <SceneContent gltfUrl={gltfUrl} cameraControlsRef={cameraControlsRef} />
    </Canvas>
  );
};

export default GLTFViewer;
