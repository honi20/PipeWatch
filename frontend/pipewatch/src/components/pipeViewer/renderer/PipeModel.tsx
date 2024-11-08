import React, { useEffect, useState } from "react";
import { GLTFLoader } from "three/examples/jsm/loaders/GLTFLoader";
import { DRACOLoader } from "three/examples/jsm/loaders/DRACOLoader";
import { ThreeEvent, useLoader } from "@react-three/fiber";
import * as THREE from "three";

export const PipeModel: React.FC<{
  gltfUrl: string;
  onModelLoad: (scene: THREE.Object3D) => void;
}> = ({ gltfUrl, onModelLoad }) => {
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

  const [visibleGroup, setVisibleGroup] = useState<string | null>(null);

  const handlePointerOver = (groupName: string) => {
    const group = meshesByGroup[groupName];
    group.forEach(({ originalMesh }) => {
      (originalMesh.material as THREE.MeshStandardMaterial).color.set(
        "#a6bdfc"
      ); // 그룹 내 모든 메쉬의 색상을 파란색으로 변경
    });
  };

  const handlePointerOut = (groupName: string) => {
    const group = meshesByGroup[groupName];
    group.forEach(({ originalMesh }) => {
      (originalMesh.material as THREE.MeshStandardMaterial).color.set("white"); // 그룹 내 모든 메쉬의 색상을 원래 색상으로 변경
    });
  };
  const handlePointerDown = (groupName: string) => {
    setVisibleGroup(groupName); // 클릭된 그룹을 상태로 설정
  };
  // const scale = [1.5, 1.5, 1.5];
  return (
    <group position={[-5, 0, 0]}>
      {Object.entries(meshesByGroup).map(([groupName, meshes], index) => {
        return (
          <group
            key={index}
            name={groupName}
            scale={[1.5, 1.5, 1.5]}
            onPointerOver={() => handlePointerOver(groupName)}
            onPointerOut={() => handlePointerOut(groupName)}
            onPointerDown={() => handlePointerDown(groupName)}
          >
            {meshes.map(({ originalMesh, segmentName }, i) => {
              const isTransparent =
                visibleGroup !== null && groupName !== visibleGroup;
              if (originalMesh.material instanceof THREE.MeshStandardMaterial) {
                originalMesh.material.transparent = !isTransparent;
                originalMesh.material.opacity = isTransparent ? 0.2 : 1.0; // 투명도 설정
                originalMesh.material.needsUpdate = true;
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
                  onPointerDown={(e: ThreeEvent<PointerEvent>) => {
                    e.stopPropagation();
                    handlePointerDown(groupName);
                  }}
                  onClick={() => console.log(segmentName)}
                />
              );
            })}
          </group>
        );
      })}
    </group>
  );
};
