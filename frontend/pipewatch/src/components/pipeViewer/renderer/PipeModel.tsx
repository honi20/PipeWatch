import React, { useEffect, useState } from "react";
import { GLTFLoader } from "three/examples/jsm/loaders/GLTFLoader";
import { DRACOLoader } from "three/examples/jsm/loaders/DRACOLoader";
import { ThreeEvent, useLoader } from "@react-three/fiber";
import { CameraControls } from "@react-three/drei";
import * as THREE from "three";
import { PipelineType } from "../Type/PipeType";
import { useSelectView } from "@src/components/context/SelectViewContext";
import { usePipe } from "@src/components/context/PipeContext";
import { useDefectStore } from "@src/stores/defectStore";
export const PipeModel: React.FC<{
  gltfUrl: string;
  onModelLoad: (scene: THREE.Object3D, vector?: number[]) => void;
  cameraControlsRef: React.RefObject<CameraControls>;
  isTotalView: boolean;
  setIsTotalView: React.Dispatch<React.SetStateAction<boolean>>;
  pipelines: PipelineType[];
  modelId: number;
  hasPipeId: boolean;
}> = ({
  gltfUrl,
  onModelLoad,
  cameraControlsRef,
  isTotalView,
  setIsTotalView,
  pipelines,
  modelId,
  hasPipeId,
}) => {
  const { setSelectView } = useSelectView();
  // selectedPipe
  const { selectedPipeId, setSelectedPipeId } = usePipe();
  const { viewDefect, defectedPipeList } = useDefectStore();
  // gltf loader
  const model = useLoader(GLTFLoader, gltfUrl, (loader) => {
    const dracoLoader = new DRACOLoader();
    dracoLoader.setDecoderPath("https://www.gstatic.com/draco/v1/decoders/");
    loader.setDRACOLoader(dracoLoader);
  });

  // 카메라 제어 함수
  const ControlTotalView = () => {
    if (model.scene && isTotalView) {
      onModelLoad(model.scene);
      // 중심 좌표 줄 경우
      // onModelLoad(model.scene, model.scenes[0].userData.extras.model_center);
      // console.log(model.scenes[0].userData.extras.model_center);
      setIsTotalView(false);
    }
  };

  // 카메라 제어를 위한 model 전달
  useEffect(() => {
    ControlTotalView();
  }, [model, isTotalView]);

  // 각 mesh의 이름에 따라 그룹화
  const meshesByGroup = React.useMemo(() => {
    const groups: {
      [key: string]: { originalMesh: THREE.Mesh; segmentName: string }[];
    } = {};
    model.scene.traverse((child) => {
      if (child instanceof THREE.Mesh) {
        const match = child.name.match(/(PipeObj_\d+)_/);
        if (match) {
          const groupName = match[1];
          const segmentName = child.name.replace(`${groupName}_`, "");
          if (!groups[groupName]) groups[groupName] = [];
          groups[groupName].push({ originalMesh: child, segmentName });
        }
      }
    });
    return groups;
  }, [model]);

  // 결함 여부에 따른 색상 업데이트
  useEffect(() => {
    model.scene.traverse((child) => {
      if (child instanceof THREE.Mesh) {
        const isDefected = confirmDefection(child);
        (child.material as THREE.MeshStandardMaterial).color.set(
          viewDefect[modelId] && isDefected ? "red" : "white"
        );
      }
    });
  }, [defectedPipeList, model, viewDefect]);

  const [clickedSegment, setClickedSegment] = useState<string | null>(null);

  const handlePointerOver = (originalMesh: THREE.Mesh) => {
    (originalMesh.material as THREE.MeshStandardMaterial).color.set("#a6bdfc");
  };

  const handlePointerOut = (originalMesh: THREE.Mesh) => {
    if (viewDefect[modelId] && confirmDefection(originalMesh)) {
      (originalMesh.material as THREE.MeshStandardMaterial).color.set("red");
    } else if (clickedSegment !== originalMesh.name) {
      (originalMesh.material as THREE.MeshStandardMaterial).color.set("white");
    }
  };
  const handlePointerDown = (segmentName: string) => {
    setClickedSegment(segmentName);
  };

  // camera control
  const handleClick = async (originalMesh: THREE.Mesh) => {
    model.scene.traverse((child) => {
      if (child instanceof THREE.Mesh) {
        if (child.name !== originalMesh.name) {
          (child.material as THREE.MeshStandardMaterial).color.set("white");
        }
      }
    });
    // const meshPosition = new THREE.Vector3();
    // originalMesh.getWorldPosition(meshPosition);

    if (cameraControlsRef.current) {
      // cameraControlsRef.current.setPosition(
      //   originalMesh.geometry.boundingBox!.max.x +
      //     originalMesh.geometry.boundingBox!.min.x,
      //   originalMesh.geometry.boundingBox!.max.y +
      //     originalMesh.geometry.boundingBox!.min.y,
      //   10
      // );
      cameraControlsRef.current.setTarget(
        originalMesh.geometry.boundingBox!.max.x +
          originalMesh.geometry.boundingBox!.min.x,
        originalMesh.geometry.boundingBox!.max.y +
          originalMesh.geometry.boundingBox!.min.y,
        originalMesh.geometry.boundingBox!.max.z +
          originalMesh.geometry.boundingBox!.min.z
      );

      cameraControlsRef.current.fitToBox(originalMesh, true, {
        paddingLeft: 20,
        paddingRight: 10,
      });
    }

    (originalMesh.material as THREE.MeshStandardMaterial).color.set("#a6bdfc");
    setClickedSegment(originalMesh.name);

    // 클릭된 mesh와 같은 이름의 파이프ID 찾아서 -> 메모 리스트 요청
    // groupName 추출
    // const groupName = originalMesh.name.split("_")[0];

    if (pipelines) {
      // 추후 pipelineId 가져와서 반영하기
      // const selectedPipeline = pipelines.filter((item)=>item.pipelineId === pipelineId)
      const filteredPipe = pipelines[0].pipes.filter(
        (item) => item.pipeUuid === originalMesh.name
      );
      // 파이프 ID 변경하기
      setSelectedPipeId(filteredPipe[0].pipeId);
    }
    // 파이프 메모 뷰 띄우기
    setSelectView("PIPE_MEMO");
  };

  // 해당 파이프의 결함 유무 체크
  const confirmDefection = (mesh: THREE.Mesh) => {
    if (hasPipeId) {
      const pipe = pipelines[0].pipes.filter(
        (item) => item.pipeUuid === mesh.name
      );
      return defectedPipeList?.includes(pipe[0].pipeId);
    }
  };

  return (
    <>
      <group position={[-10, 0, 0]}>
        {Object.entries(meshesByGroup).map(([groupName, meshes], index) => {
          return (
            <group key={index} name={groupName}>
              {meshes.map(({ originalMesh, segmentName }, i) => {
                if (
                  hasPipeId &&
                  viewDefect[modelId] &&
                  confirmDefection(originalMesh)
                ) {
                  (
                    originalMesh.material as THREE.MeshStandardMaterial
                  ).color.set("red");
                }
                return (
                  <mesh
                    key={i}
                    name={segmentName}
                    geometry={originalMesh.geometry}
                    material={originalMesh.material}
                    position={originalMesh.position}
                    rotation={originalMesh.rotation}
                    scale={originalMesh.scale}
                    onPointerOver={() => handlePointerOver(originalMesh)}
                    onPointerOut={() => handlePointerOut(originalMesh)}
                    onPointerDown={(e: ThreeEvent<PointerEvent>) => {
                      e.stopPropagation();
                      handlePointerDown(groupName);
                    }}
                    onClick={() => handleClick(originalMesh)}
                  />
                );
              })}
            </group>
          );
        })}
      </group>
    </>
  );
};
