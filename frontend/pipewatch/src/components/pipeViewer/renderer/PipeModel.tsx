import React, { useEffect, useState } from "react";
import { GLTFLoader } from "three/examples/jsm/loaders/GLTFLoader";
import { DRACOLoader } from "three/examples/jsm/loaders/DRACOLoader";
import { ThreeEvent, useLoader } from "@react-three/fiber";
import { CameraControls } from "@react-three/drei";
import * as THREE from "three";

export const PipeModel: React.FC<{
  gltfUrl: string;
  onModelLoad: (scene: THREE.Object3D) => void;
  cameraControlsRef: React.RefObject<CameraControls>;
}> = ({ gltfUrl, onModelLoad, cameraControlsRef }) => {
  // gltf loader
  const model = useLoader(GLTFLoader, gltfUrl, (loader) => {
    const dracoLoader = new DRACOLoader();
    dracoLoader.setDecoderPath("https://www.gstatic.com/draco/v1/decoders/");
    loader.setDRACOLoader(dracoLoader);
  });

  // 카메라 제어를 위한 model 전달
  useEffect(() => {
    if (model.scene) {
      onModelLoad(model.scene);
    }
  }, [model]);

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

  const [clickedSegment, setClickedSegment] = useState<string | null>(null);

  const handlePointerOver = (originalMesh: THREE.Mesh) => {
    (originalMesh.material as THREE.MeshStandardMaterial).color.set("#a6bdfc");
  };

  const handlePointerOut = (originalMesh: THREE.Mesh) => {
    if (clickedSegment !== originalMesh.name) {
      (originalMesh.material as THREE.MeshStandardMaterial).color.set("white");
    }
  };
  const handlePointerDown = (segmentName: string) => {
    setClickedSegment(segmentName); // 클릭된 세그먼트를 상태로 설정
  };

  // camera control
  const handleClick = (originalMesh: THREE.Mesh) => {
    const meshPosition = new THREE.Vector3();
    originalMesh.getWorldPosition(meshPosition);
    console.log(originalMesh.geometry.boundingBox?.max);

    if (cameraControlsRef.current) {
      cameraControlsRef.current.setTarget(
        originalMesh.geometry.boundingBox!.max.x,
        originalMesh.geometry.boundingBox!.max.y,
        originalMesh.geometry.boundingBox!.max.z
      );
      cameraControlsRef.current.fitToBox(originalMesh, true, {
        paddingLeft: 5,
        paddingRight: 5,
      });
    }

    console.log(`Clicked on: ${originalMesh.name}`);
  };
  return (
    <group position={[0, 0, 0]}>
      {Object.entries(meshesByGroup).map(([groupName, meshes], index) => {
        return (
          <group key={index} name={groupName} scale={[1.5, 1.5, 1.5]}>
            {meshes.map(({ originalMesh, segmentName }, i) => {
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
  );
};
