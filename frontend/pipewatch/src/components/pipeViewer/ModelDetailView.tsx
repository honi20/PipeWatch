import React, { useEffect, useState } from "react";
import GLTFViewer from "@src/components/pipeViewer/GLTFViewer";
import "./viewer.css";
import { getApiClient } from "@src/stores/apiClient";
import { ModelDetailType } from "./Type/PipeType";
import { useSelectView } from "../context/SelectViewContext";
import { useDefectStore } from "@src/stores/defectStore";

interface ModelDetailViewProps {
  modelId: number;
}

export const ModelDetailView: React.FC<ModelDetailViewProps> = ({
  modelId,
}) => {
  const { selectView } = useSelectView();
  const [modelDetail, setModelDetail] = useState<ModelDetailType>();
  const { setDefectedPipeList } = useDefectStore();

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
      setDefectedPipeList(res.data.body.defectPipeList);
    } catch (err) {
      console.log(err);
    }
  };

  useEffect(() => {
    getModelDetail();
  }, [modelId, selectView]);

  return (
    <div className="w-full h-full">
      {modelDetail && (
        <GLTFViewer
          gltfUrl={modelDetail.modelingUrl}
          pipelines={modelDetail.pipelines}
          modelId={modelId}
          modelDetail={modelDetail}
          hasPipeId={modelDetail.pipelines.length > 0}
        />
      )}
    </div>
  );
};
