import React, { useEffect, useState } from "react";
import GLTFViewer from "@src/components/pipeViewer/GLTFViewer";
import "./viewer.css";
import { getApiClient } from "@src/stores/apiClient";
import { ModelDetailType } from "@src/components/pipeViewer/PipeType";
import { ModelMemo } from "@src/components/pipeViewer/ModelMemo";

interface ModelDetailViewProps {
  modelId: number;
}

export const ModelDetailView: React.FC<ModelDetailViewProps> = ({
  modelId,
}) => {
  const [selectView, setSelectView] = useState<"MEMO" | "PROPERTY">("MEMO");
  const [cardFlipClass, setCardFlipClass] = useState("");
  const [modelDetail, setModelDetail] = useState<ModelDetailType>();

  // model 상세 정보 조회해야함
  const getModelDetail = async () => {
    const apiClient = getApiClient();
    try {
      const res = await apiClient({
        method: "get",
        url: `/api/models/${modelId}`,
      });
      console.log(res.data.header.httpStatusCode, res.data.header.message);
      console.log(res.data.body);
      setModelDetail(res.data.body);
    } catch (err) {
      console.log(err);
    }
  };

  useEffect(() => {
    if (!modelDetail || Object.keys(modelDetail).length === 0) {
      getModelDetail();
    }
  }, [modelDetail]);

  // pipe 정보 조회해야함
  // 카드 회전 CSS
  useEffect(() => {
    setCardFlipClass(
      selectView === "MEMO" ? "rotateY(180deg)" : "rotateY(0deg)"
    );
  }, [selectView]);

  return (
    <div className="relative w-full h-full">
      {modelDetail && <GLTFViewer gltfUrl={modelDetail.modelingUrl} />}
      <div className="absolute top-5 right-10">
        {modelDetail && (
          <ModelMemo
            modelId={modelId}
            modelName={modelDetail!.name}
            building={modelDetail!.building}
            floor={modelDetail!.floor}
            updatedAt={modelDetail!.updatedAt}
          />
        )}
      </div>
    </div>
  );
};
