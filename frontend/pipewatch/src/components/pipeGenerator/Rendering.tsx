import { Suspense, useRef, useEffect } from "react";
import { useTranslation } from "react-i18next";
import { useParams } from "react-router-dom";

import { Leva } from "leva";
import { Loader } from "@react-three/drei";
import TestRendering from "@components/pipeGenerator/TestRendering";

import { IconButton } from "@components/common/IconButton";

export const Rendering = () => {
  const { t } = useTranslation();

  const testRenderingRef = useRef<{ takeScreenshot: () => void }>(null);

  const { modelId } = useParams<string>();

  useEffect(() => {
    console.log(modelId);
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
          {modelId ? (
            <TestRendering ref={testRenderingRef} modelId={modelId} />
          ) : (
            <div>model 없음</div>
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
