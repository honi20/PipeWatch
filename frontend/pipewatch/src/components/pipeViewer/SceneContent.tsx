import { CameraControls } from "@react-three/drei";
import { PipeModel } from "@src/components/pipeViewer/renderer/PipeModel";
import React from "react";
import * as THREE from "three";
import { PipelineType } from "./Type/PipeType";

export const SceneContent: React.FC<{
  gltfUrl: string;
  cameraControlsRef: React.RefObject<CameraControls>;
  isTotalView: boolean;
  setIsTotalView: React.Dispatch<React.SetStateAction<boolean>>;
  pipelines: PipelineType[];
  modelId: number;
  hasPipeId: boolean;
}> = ({
  gltfUrl,
  cameraControlsRef,
  isTotalView,
  setIsTotalView,
  pipelines,
  modelId,
  hasPipeId,
}) => {
  const clock = new THREE.Clock();
  const delta = clock.getDelta();

  // 모델 로드 시 카메라 제어
  const adjustCameraOnModelLoad = (
    scene: THREE.Object3D,
    vector?: number[]
  ) => {
    if (vector) {
      console.log(vector);
      if (cameraControlsRef.current) {
        // cameraControlsRef를 사용하여 카메라 위치와 타겟을 설정
        cameraControlsRef.current.setPosition(vector[0], vector[1], 30);
        cameraControlsRef.current.setTarget(vector[0], vector[1], vector[2]);
        cameraControlsRef.current.update(delta);
      }
    } else {
      if (cameraControlsRef.current) {
        const box = new THREE.Box3().setFromObject(scene);
        const size = new THREE.Vector3();
        box.getSize(size);
        const center = new THREE.Vector3();
        box.getCenter(center);

        // 모델 전체뷰를 보기 위한 거리 계산
        const maxDim = Math.max(size.x, size.y, size.z);

        // 카메라 위치 설정 (cameraControlsRef를 통해 제어)
        if (cameraControlsRef.current) {
          cameraControlsRef.current.setPosition(center.x, center.y, maxDim);
          cameraControlsRef.current.setTarget(center.x, center.y, center.z);
          cameraControlsRef.current.update(delta);
          console.log(
            "Camera adjusted using cameraControlsRef",
            cameraControlsRef.current
          );
        }
      }
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
        modelId={modelId}
        hasPipeId={hasPipeId}
      />
      <CameraControls
        ref={cameraControlsRef}
        enabled={true}
        dollyToCursor={true}
        minDistance={5}
        maxDistance={100}
        minPolarAngle={0}
        maxPolarAngle={Math.PI}
        dampingFactor={0.25}
      />
      {/* <gridHelper /> */}
    </>
  );
};
