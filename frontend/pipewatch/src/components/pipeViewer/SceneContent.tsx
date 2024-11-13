import React, { useEffect } from "react";
import { useThree } from "@react-three/fiber";
import * as THREE from "three";
import { PipeModel } from "@src/components/pipeViewer/renderer/PipeModel";
import { CameraControls, OrbitControls } from "@react-three/drei";
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
  const clock = new THREE.Clock();
  const delta = clock.getDelta();
  useEffect(() => {
    scene.add(helper);
    return () => {
      scene.remove(helper); // 컴포넌트 언마운트 시 제거
    };
  }, [camera, scene, helper]);

  // 모델 로드 시 카메라 제어
  const adjustCameraOnModelLoad = (
    scene: THREE.Object3D,
    vector?: number[]
  ) => {
    console.log(scene);
    if (vector) {
      console.log(vector);
      if (cameraControlsRef.current) {
        // cameraControlsRef를 사용하여 카메라 위치와 타겟을 설정
        cameraControlsRef.current.setPosition(vector[0], vector[1], 50);
        cameraControlsRef.current.setTarget(vector[0], vector[1], vector[2]);

        cameraControlsRef.current.update(delta);
        console.log("카메라 조정 중", cameraControlsRef.current);
      }
    } else {
      if (cameraControlsRef.current) {
        const box = new THREE.Box3().setFromObject(scene);
        const size = new THREE.Vector3();
        box.getSize(size);
        const center = new THREE.Vector3();
        box.getCenter(center);

        // 모델을 보기 위한 적절한 거리 계산
        // console.log(camera);
        const maxDim = Math.max(size.x, size.y, size.z);
        // const fov = camera.fov * (Math.PI / 180);
        // const cameraZ = Math.abs(maxDim / (2 * Math.tan(fov / 2))) * 1.5;

        // 카메라 위치 설정 (cameraControlsRef를 통해 제어)
        if (cameraControlsRef.current) {
          cameraControlsRef.current.setPosition(center.x, center.y, maxDim * 2);
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
      />
      <CameraControls
        ref={cameraControlsRef}
        enabled={true}
        dollyToCursor={true}
        minDistance={5}
        maxDistance={50}
        minPolarAngle={0}
        maxPolarAngle={Math.PI}
        dampingFactor={0.25}
      />
      <axesHelper args={[10]} />
      <gridHelper />
    </>
  );
};
