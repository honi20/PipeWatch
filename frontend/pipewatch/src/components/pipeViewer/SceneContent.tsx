import React, { useEffect } from "react";
import { useThree } from "@react-three/fiber";
import * as THREE from "three";
import { PipeModel } from "@src/components/pipeViewer/renderer/PipeModel";
import { CameraControls } from "@react-three/drei";
import { PipelineType } from "./Type/PipeType";

export const SceneContent: React.FC<{
  gltfUrl: string;
  cameraControlsRef: React.RefObject<CameraControls>;
  isTotalView: boolean;
  setIsTotalView: React.Dispatch<React.SetStateAction<boolean>>;
  pipelines: PipelineType[];
}> = ({
  gltfUrl,
  cameraControlsRef,
  isTotalView,
  setIsTotalView,
  pipelines,
}) => {
  const { camera, scene } = useThree();
  const helper = new THREE.CameraHelper(camera);

  useEffect(() => {
    scene.add(helper);
    return () => {
      scene.remove(helper); // 컴포넌트 언마운트 시 제거
    };
  }, [camera, scene, helper]);

  // 모델 로드 시 카메라 제어
  const adjustCameraOnModelLoad = (scene: THREE.Object3D) => {
    if (camera instanceof THREE.PerspectiveCamera) {
      const box = new THREE.Box3().setFromObject(scene);
      const size = new THREE.Vector3();
      box.getSize(size);
      const center = new THREE.Vector3();
      box.getCenter(center);

      // 카메라가 Z 축에서 XY 평면에 위치한 모델을 바라보도록 조정
      const maxDim = Math.max(size.x, size.y, size.z);
      const fov = camera.fov * (Math.PI / 180);
      const cameraZ = Math.abs(maxDim / (2 * Math.tan(fov / 2))) * 1.5; // 적절한 거리 계산

      // 카메라를 Z 축 상의 적절한 위치로 설정
      camera.position.set(center.x, center.y, center.z + cameraZ * 2);
      camera.lookAt(center.x, center.y, center.z); // 모델의 중심을 바라봄
      camera.updateProjectionMatrix();

      if (cameraControlsRef.current) {
        cameraControlsRef.current.setTarget(center.x, center.y, center.z, true);
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
        isTotalView={isTotalView}
        setIsTotalView={setIsTotalView}
        pipelines={pipelines}
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
