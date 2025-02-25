import { useTranslation } from "react-i18next";
import React, { useEffect, useRef, useState } from "react";
import { Canvas } from "@react-three/fiber";
import { CameraControls } from "@react-three/drei";
import { ModelDetailType, PipelineType } from "./Type/PipeType";
import { SceneContent } from "@src/components/pipeViewer/SceneContent";
import { ModelMemo } from "@src/components/pipeViewer/ModelMemo";
import { ModelProperty } from "@src/components/pipeViewer/ModelProperty";
import { useSelectView } from "../context/SelectViewContext";
import { PipeMemo } from "@src/components/pipeViewer/PipeMemo";
import { usePipe } from "@src/components/context/PipeContext";
import * as THREE from "three";

interface GLTFViewerProps {
  gltfUrl: string;
  pipelines: PipelineType[];
  modelId: number;
  modelDetail: ModelDetailType;
  hasPipeId: boolean;
}

const GLTFViewer: React.FC<GLTFViewerProps> = (props) => {
  const { t } = useTranslation();
  const { setSelectedPipeId, isButtonClicked, setIsButtonClicked } = usePipe();
  const { selectView, setSelectView } = useSelectView();
  const { gltfUrl, pipelines, modelId, modelDetail, hasPipeId } = props;
  const cameraControlsRef = useRef<CameraControls | null>(null);
  const [isTotalView, setIsTotalView] = useState<boolean>(true);
  const rendererRef = useRef<THREE.WebGLRenderer | null>(null);

  const handleTotalViewButton = () => {
    setIsTotalView(true);
    setSelectView("MODEL_MEMO");
    setSelectedPipeId(null);
  };

  useEffect(() => {
    if (isButtonClicked) {
      handleScreenshot();
      setIsButtonClicked(!isButtonClicked);
    }
  }, [isButtonClicked]);

  // 캡처 로직
  const handleScreenshot = () => {
    if (!isButtonClicked && !rendererRef.current) return;

    const renderer = rendererRef.current;
    const canvas = renderer!.domElement;

    // 캡처 데이터 생성
    const link = document.createElement("a");
    link.setAttribute("download", `screenshot_${modelId}.png`);
    link.setAttribute(
      "href",
      canvas.toDataURL("image/png").replace("image/png", "image/octet-stream")
    );
    link.click();
  };

  return (
    <div className="relative w-full h-full">
      <Canvas
        style={{ height: "100vh" }}
        onCreated={({ gl }) => (rendererRef.current = gl)}
        shadows
        gl={{ preserveDrawingBuffer: true }}
      >
        <ambientLight intensity={1} />
        <directionalLight position={[5, 10, 7.5]} intensity={1} castShadow />
        <directionalLight position={[5, 10, -7.5]} intensity={1} castShadow />
        <SceneContent
          gltfUrl={gltfUrl}
          cameraControlsRef={cameraControlsRef}
          isTotalView={isTotalView}
          setIsTotalView={setIsTotalView}
          pipelines={pipelines}
          modelId={modelId}
          hasPipeId={hasPipeId}
        />
      </Canvas>
      <button
        className="sticky z-10 px-1 py-1 text-black transform -translate-x-1/2 -translate-y-10 bg-white border border-black bottom-10 left-1/2 rounded-xl text-[18px] hover:text-primary-500 hover:border-primary-200"
        onClick={handleTotalViewButton}
      >
        {t("PipeViewer.GLTFViewer.viewFullModel")}
      </button>
      <div className="absolute z-10 top-10 right-10">
        {modelDetail &&
          (selectView === "MODEL_MEMO" ? (
            <ModelMemo
              modelId={modelId}
              modelName={modelDetail.name}
              building={modelDetail.building}
              floor={modelDetail.floor}
              updatedAt={modelDetail.updatedAt}
              onViewChange={() => setSelectView("PROPERTY")}
              hasPipeId={hasPipeId}
            />
          ) : selectView === "PROPERTY" ? (
            <ModelProperty
              modelId={modelId}
              modelName={modelDetail.name}
              pipelines={modelDetail.pipelines}
              building={modelDetail.building}
              floor={modelDetail!.floor}
              onViewChange={() => setSelectView("MODEL_MEMO")}
            />
          ) : (
            selectView === "PIPE_MEMO" && (
              <PipeMemo
                modelName={modelDetail.name}
                building={modelDetail.building}
                floor={modelDetail.floor}
                updatedAt={modelDetail.updatedAt}
                onViewChange={() => setSelectView("MODEL_MEMO")}
                setIsTotalView={setIsTotalView}
                hasPipeId={hasPipeId}
              />
            )
          ))}
      </div>
    </div>
  );
};

export default GLTFViewer;
