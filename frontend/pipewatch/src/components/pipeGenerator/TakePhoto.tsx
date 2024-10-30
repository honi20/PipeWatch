import { useTranslation } from "react-i18next";
import { IconButton } from "@components/common/IconButton";

import ElectricCarIcon from "@mui/icons-material/ElectricCar";
export const TakePhoto = () => {
  const { t } = useTranslation();

  return (
    <div className="p-[40px]">
      <div>
        <div className="text-[24px] font-bold">
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
            color={"primary-500"}
            hoverColor={"primary-200"}
            icon={<ElectricCarIcon sx={{ fontSize: "20px" }} />}
          />
        </div>
      </div>

      <div>
        StatusBar
        <div>
          {t("pipeGenerator.takePhoto.connectRCCar.statusMessages.connecting")}
        </div>
        <div>
          {t("pipeGenerator.takePhoto.connectRCCar.statusMessages.completed")}
        </div>
        <div>
          {t("pipeGenerator.takePhoto.connectRCCar.statusMessages.failed")}
        </div>
      </div>

      <div>
        <div>{t("pipeGenerator.takePhoto.caution.title")}</div>
        <div>
          {t("pipeGenerator.takePhoto.caution.instructions.pageDescription")}
        </div>
        <div>
          {t(
            "pipeGenerator.takePhoto.caution.instructions.manualUploadInstruction"
          )}
        </div>
        <button>{t("pipeGenerator.takePhoto.caution.buttons.upload")}</button>
      </div>
    </div>
  );
};
