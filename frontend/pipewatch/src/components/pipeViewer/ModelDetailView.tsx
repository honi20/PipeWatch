import React, { useEffect, useState } from "react";
import GLTFViewer from "@src/components/pipeViewer/GLTFViewer";
import "./viewer.css";
import { getApiClient } from "@src/stores/apiClient";
import { ModelDetailType } from "@src/components/pipeViewer/PipeType";

interface ModelDetailViewProps {
  modelId: number;
}

export const ModelDetailView: React.FC<ModelDetailViewProps> = ({
  modelId,
}) => {
  // const [cardFlipClass, setCardFlipClass] = useState("");
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
    getModelDetail();
  }, [modelId]);

  return (
    <div className="w-full h-full">
      {modelDetail && (
        <GLTFViewer
          gltfUrl={modelDetail.modelingUrl}
          pipelines={modelDetail.pipelines}
          modelId={modelId}
          modelDetail={modelDetail}
        />
      )}
    </div>
  );
};
