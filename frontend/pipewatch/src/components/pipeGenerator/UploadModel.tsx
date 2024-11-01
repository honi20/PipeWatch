import { useTranslation } from "react-i18next";
import { useLocation } from "react-router-dom";

import { UploadModelAuto } from "@components/pipeGenerator/UploadModelAuto";
import { UploadModelManual } from "@components/pipeGenerator/UploadModelManual";

export const UploadModel = () => {
  const { t } = useTranslation();

  const location = useLocation();
  const query = new URLSearchParams(location.search);
  const action = query.get("action");

  console.log("action 탐지중: ", action);

  return (
    <div className="p-[40px]">
      <div className="text-[24px] font-bold mb-[20px]">
        {t("pipeGenerator.uploadModel.title")}
      </div>

      {action === "auto" && <UploadModelAuto />}

      {action !== "auto" && <UploadModelManual />}
    </div>
  );
};
