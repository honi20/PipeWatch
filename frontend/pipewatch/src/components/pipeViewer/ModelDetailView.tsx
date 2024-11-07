import React, { useEffect, useState } from "react";
import GLTFViewer from "@src/components/pipeViewer/GLTFViewer";
import { PipeMemo } from "@src/components/pipeViewer/PipeMemo";
import { PipeProperty } from "@src/components/pipeViewer/PipeProperty";
import "./viewer.css";

interface ModelDetailViewProps {
  modelId: number;
}
export const ModelDetailView: React.FC<ModelDetailViewProps> = ({
  modelId,
}) => {
  const [selectView, setSelectView] = useState<"MEMO" | "PROPERTY">("PROPERTY");
  const [cardFlipClass, setCardFlipClass] = useState("");
  // 카드 회전 CSS
  useEffect(() => {
    setCardFlipClass(
      selectView === "MEMO" ? "rotateY(180deg)" : "rotateY(0deg)"
    );
  }, [selectView]);
  return (
    <div className="relative w-full h-full">
      <div>{modelId}</div>
      <GLTFViewer gltfUrl="/assets/models/PipeLine.gltf" />
      <div className="absolute card-container top-5 right-10">
        <div className="card" style={{ transform: cardFlipClass }}>
          <div className="card-front">
            <PipeProperty
              pipe={pipe}
              onViewChange={() => setSelectView("MEMO")}
            />
          </div>
          <div className="card-back">
            <PipeMemo
              pipeId={modelId}
              onViewChange={() => setSelectView("PROPERTY")}
            />
          </div>
        </div>
      </div>
    </div>
  );
};
