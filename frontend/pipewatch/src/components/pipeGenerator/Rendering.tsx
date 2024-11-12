import { Suspense, useRef, useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import { useParams } from "react-router-dom";

import { Leva } from "leva";
import { Loader } from "@react-three/drei";
import TestRendering from "@components/pipeGenerator/TestRendering";

import { IconButton } from "@components/common/IconButton";
import { getApiClient } from "@src/stores/apiClient";

type urlType = {
  modelId: string;
};

export const Rendering = () => {
  const { t } = useTranslation();

  const testRenderingRef = useRef<{ takeScreenshot: () => void }>(null);

  const { modelId } = useParams() as urlType;

  const apiClient = getApiClient();
  const [gltfUrl, setGltfUrl] = useState("");

  const getGLTFUrl = async () => {
    try {
      const res = await apiClient.get(`/api/models/${modelId}`);
      console.log("모델 상세 조회 in Rendering: ", res.data.body.modelingUrl);
      setGltfUrl(res.data.body.modelingUrl);
    } catch (err) {
      console.log(err);
    }
  };

  useEffect(() => {
    getGLTFUrl();
  }, []);

  // 저장 버튼 Click Action
  const handleSave = async () => {
    if (testRenderingRef.current) {
      testRenderingRef.current.takeScreenshot();
    }
  };

  return (
    <div className="p-[40px]">
      <div className="text-[24px] font-bold mb-[20px]">
        {t("pipeGenerator.rendering.title")}
      </div>
      <p className="text-[16px]">
        {t("pipeGenerator.rendering.instructions.completed.message")}
      </p>
      <p className="text-[16px]">
        {t("pipeGenerator.rendering.instructions.completed.previewAndSave")}
      </p>

      <div className="w-full h-[400px]">
        <Suspense fallback={<Loader />}>
          {gltfUrl ? (
            <TestRendering
              ref={testRenderingRef}
              gltfUrl={gltfUrl}
              modelId={modelId}
            />
          ) : (
            <div>model 생성중</div>
          )}
          <Leva collapsed />
        </Suspense>
      </div>

      <div className="flex justify-center w-full">
        <IconButton
          handleClick={handleSave}
          text={t("pipeGenerator.commonButtons.save")}
          color={"bg-primary-500"}
          hoverColor={"hover:bg-primary-500/80"}
        />
      </div>
    </div>
  );
};
