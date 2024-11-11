import React, { useRef, useState, useEffect } from "react";
import { Canvas, useThree } from "@react-three/fiber";
import * as THREE from "three";
import { PipeModel } from "@src/components/pipeViewer/renderer/PipeModel";
import { CameraControls } from "@react-three/drei";
import {
  ModelDetailType,
  PipelineType,
} from "@src/components/pipeViewer/PipeType";
import { SceneContent } from "@src/components/pipeViewer/SceneContent";
import { ModelMemo } from "@src/components/pipeViewer/ModelMemo";
import { ModelProperty } from "@src/components/pipeViewer/ModelProperty";

interface GLTFViewerProps {
  gltfUrl: string;
  pipelines: PipelineType[];
  modelId: number;
  modelDetail: ModelDetailType;
}

const GLTFViewer: React.FC<GLTFViewerProps> = (props) => {
  const [selectView, setSelectView] = useState<string | null>("MEMO");
  const { gltfUrl, pipelines, modelId, modelDetail } = props;
  const cameraControlsRef = useRef<CameraControls | null>(null);
  const [isTotalView, setIsTotalView] = useState<boolean>(true);

  return (
    <div className="relative w-full h-full">
      <Canvas style={{ height: "100vh" }}>
        <ambientLight intensity={1} />
        <directionalLight position={[5, 10, 7.5]} intensity={1} castShadow />
        <directionalLight position={[5, 10, -7.5]} intensity={1} castShadow />
        <SceneContent
          gltfUrl={gltfUrl}
          cameraControlsRef={cameraControlsRef}
          isTotalView={isTotalView}
          setIsTotalView={setIsTotalView}
          pipelines={pipelines}
        />
      </Canvas>
      <button
        className="sticky z-10 transform -translate-x-1/2 -translate-y-10 bottom-10 left-1/2"
        onClick={() => setIsTotalView(true)}
      >
        전체 뷰 보기 버튼
      </button>
      <div className="absolute z-10 top-10 right-10">
        {modelDetail &&
          (selectView === "MEMO" ? (
            <ModelMemo
              modelId={modelId}
              modelName={modelDetail!.name}
              building={modelDetail!.building}
              floor={modelDetail!.floor}
              updatedAt={modelDetail!.updatedAt}
              onViewChange={() => setSelectView("PROPERTY")}
            />
          ) : (
            selectView === "PROPERTY" && (
              <ModelProperty
                pipelines={modelDetail!.pipelines}
                building={modelDetail!.building}
                floor={modelDetail!.floor}
                onViewChange={() => setSelectView("MEMO")}
              />
            )
          ))}
      </div>
    </div>
  );
};

export default GLTFViewer;
