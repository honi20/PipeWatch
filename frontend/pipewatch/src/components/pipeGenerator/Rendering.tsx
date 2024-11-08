import { Suspense, useRef, useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import { useNavigate, useLocation } from "react-router-dom";

import { Leva } from "leva";
import { Loader } from "@react-three/drei";
import TestRendering from "@components/pipeGenerator/TestRendering";

import { IconButton } from "@components/common/IconButton";

// import { getApiClient } from "@src/stores/apiClient";

export const Rendering = () => {
  const { t } = useTranslation();
  // const navigate = useNavigate();

  const testRenderingRef = useRef<{ takeScreenshot: () => void }>(null);

  // modelId: Input Data Page에서 POST 요청 후 navigate state로 받아옴
  const [modelId, setModelId] = useState("");
  const location = useLocation();

  useEffect(() => {
    setModelId(location.state.modelId);
  }, []);

  // const modelId = "15"; // 테스트용

  // 저장 버튼 Click Action
  const handleSave = async () => {
    // POST 함수 추가 예정
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
          <TestRendering ref={testRenderingRef} modelId={modelId} />
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
