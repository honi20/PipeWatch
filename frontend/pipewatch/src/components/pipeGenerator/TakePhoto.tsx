import { useTranslation } from "react-i18next";

import { useNavigate } from "react-router-dom";

import { IconButton } from "@components/common/IconButton";
import { StatusBar } from "@components/common/StatusBar";

import ElectricCarIcon from "@mui/icons-material/ElectricCar";
import WarningIcon from "@mui/icons-material/Warning";
import ReplayIcon from "@mui/icons-material/Replay";
import PhotoCameraIcon from "@mui/icons-material/PhotoCamera";

import CheckCircleIcon from "@mui/icons-material/CheckCircle";
import CancelIcon from "@mui/icons-material/Cancel";
import SyncIcon from "@mui/icons-material/Sync";

export const TakePhoto = () => {
  const { t } = useTranslation();
  const navigate = useNavigate();

  return (
    <div className="p-[40px]">
      <div>
        <div className="text-[24px] font-bold mb-[20px]">
          {t("pipeGenerator.takePhoto.connectRCCar.title")}
        </div>
        <p className="text-[16px]">
          {t("pipeGenerator.takePhoto.connectRCCar.instructions.checkPower")}
        </p>
        <p className="text-[16px]">
          {t(
            "pipeGenerator.takePhoto.connectRCCar.instructions.pressConnectButton"
          )}
        </p>
        <p className="text-[16px]">
          {t(
            "pipeGenerator.takePhoto.connectRCCar.instructions.connectionComplete"
          )}
        </p>
        <p className="text-[16px]">
          {t("pipeGenerator.takePhoto.connectRCCar.instructions.avoidMovement")}
        </p>
        <div className="flex justify-center w-full">
          <IconButton
            handleClick={() => console.log("button Clicked")}
            text={t("pipeGenerator.takePhoto.connectRCCar.buttons.connect")}
            color={"bg-primary-500"}
            hoverColor={"hover:bg-primary-500/80"}
            icon={<ElectricCarIcon sx={{ fontSize: "20px" }} />}
          />
        </div>
      </div>

      <div>
        <div className="text-[24px] font-bold mb-[20px]">
          {t("pipeGenerator.takePhoto.takePipePhoto.title")}
        </div>
        <p className="text-[16px]">
          {t("pipeGenerator.takePhoto.takePipePhoto.instructions.startCapture")}
        </p>
        <p className="text-[16px]">
          {t(
            "pipeGenerator.takePhoto.takePipePhoto.instructions.avoidInterference"
          )}
        </p>
        <p className="text-[16px]">
          {t("pipeGenerator.takePhoto.takePipePhoto.instructions.autoSave")}
        </p>
        <p className="text-[16px]">
          {t("pipeGenerator.takePhoto.takePipePhoto.instructions.stopOnError")}
        </p>
        <div className="flex justify-center w-full">
          <IconButton
            handleClick={() =>
              navigate("/pipe-generator/upload-model?action=auto")
            }
            text={t(
              "pipeGenerator.takePhoto.takePipePhoto.buttons.startCapture"
            )}
            color={"bg-primary-500"}
            hoverColor={"hover:bg-primary-500/80"}
            icon={<PhotoCameraIcon sx={{ fontSize: "20px" }} />}
          />
        </div>
      </div>

      <div>
        <StatusBar
          text={t(
            "pipeGenerator.takePhoto.connectRCCar.statusMessages.completed"
          )}
          icon={<CheckCircleIcon sx={{ fontSize: "20px" }} />}
          color={"bg-success"}
        />
        <StatusBar
          text={t(
            "pipeGenerator.takePhoto.connectRCCar.statusMessages.connecting"
          )}
          icon={<SyncIcon sx={{ fontSize: "20px" }} />}
          color={"bg-gray-800"}
        />
        <StatusBar
          text={t("pipeGenerator.takePhoto.connectRCCar.statusMessages.failed")}
          icon={<CancelIcon sx={{ fontSize: "20px" }} />}
          color={"bg-warn"}
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

      <div className="bg-warnBackground p-[20px] rounded-[10px] text-warn">
        <div className="flex gap-[4px] flex-col ">
          <div className="flex gap-[10px] font-bold">
            <WarningIcon sx={{ fontSize: "20px" }} />
            <div>{t("pipeGenerator.takePhoto.caution.title")}</div>
          </div>
          <div className="px-[30px]">
            <p>
              {t(
                "pipeGenerator.takePhoto.caution.instructions.pageDescription"
              )}
            </p>
            <p>
              {t(
                "pipeGenerator.takePhoto.caution.instructions.manualUploadInstruction"
              )}
            </p>
          </div>
        </div>
        <div className="flex justify-center w-full">
          <IconButton
            handleClick={() =>
              navigate("/pipe-generator/upload-model?action=manual")
            }
            text={t("pipeGenerator.takePhoto.caution.buttons.upload")}
            color={"bg-warn"}
            hoverColor={"hover:bg-warn/80"}
            icon={<ElectricCarIcon sx={{ fontSize: "20px" }} />}
          />
        </div>
      </div>
    </div>
  );
};
