import { useTranslation } from "react-i18next";
import { IconButton } from "@components/common/IconButton";

import DriveFolderUploadIcon from "@mui/icons-material/DriveFolderUpload";

import ReplayIcon from "@mui/icons-material/Replay";

export const UploadModel = () => {
  const { t } = useTranslation();

  return (
    <div className="p-[40px]">
      <div>
        <div className="text-[24px] font-bold">
          {t("pipeGenerator.uploadModel.title")}
        </div>
        <p className="text-[16px]">
          {t("pipeGenerator.uploadModel.directUpload.instructions.uploadModel")}
        </p>
        <p className="text-[16px]">
          {t(
            "pipeGenerator.uploadModel.directUpload.instructions.recommendedFormat"
          )}
          : <span className={"font-bold"}>.gltf</span>
        </p>

        <div className="flex justify-center w-full my-[20px]">
          <div className="w-[500px] h-[300px] flex flex-col justify-center items-center bg-whiteBox shadow-md rounded-[12px] shadow-gray-500">
            <label className="flex flex-col items-center justify-center preview">
              <input type="file" className="hidden" />
              <DriveFolderUploadIcon
                sx={{ fontSize: "96px", color: "#D9D9D9" }}
              />
              <p className="text-gray-500 underline preview_msg">
                {t(
                  "pipeGenerator.uploadModel.directUpload.uploadBox.selectFile"
                )}
              </p>
            </label>

            <p className="preview_uploading">
              {t(
                "pipeGenerator.uploadModel.directUpload.uploadBox.statusMessages.uploading"
              )}
            </p>
            <p>
              {t(
                "pipeGenerator.uploadModel.directUpload.uploadBox.statusMessages.success"
              )}
            </p>
            <p>
              {t(
                "pipeGenerator.uploadModel.directUpload.uploadBox.statusMessages.failed"
              )}
            </p>
          </div>
        </div>
        <div className="flex justify-center w-full">
          <IconButton
            handleClick={() => console.log("button Clicked")}
            text={t("pipeGenerator.commonButtons.save")}
            color={"bg-primary-500"}
            hoverColor={"hover:bg-primary-500/80"}
          />
        </div>
        <div className="flex justify-center w-full">
          <IconButton
            handleClick={() => console.log("button Clicked")}
            text={t("pipeGenerator.commonButtons.retry")}
            color={"bg-gray-800"}
            hoverColor={"hover:bg-gray-800/80"}
            icon={<ReplayIcon sx={{ fontSize: "20px" }} />}
          />
        </div>
      </div>
    </div>
  );
};
