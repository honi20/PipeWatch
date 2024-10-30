import { useTranslation } from "react-i18next";
import { IconButton } from "@components/common/IconButton";

import ElectricCarIcon from "@mui/icons-material/ElectricCar";
import WarningIcon from "@mui/icons-material/Warning";

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
            color={"bg-primary-500"}
            hoverColor={"hover:bg-primary-200"}
            icon={<ElectricCarIcon sx={{ fontSize: "20px" }} />}
          />
        </div>
      </div>

      <div>
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

      <div className="bg-warnTheme-background p-[20px] rounded-[10px] text-warn">
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
            handleClick={() => console.log("button Clicked")}
            text={t("pipeGenerator.takePhoto.caution.buttons.upload")}
            color={"bg-warn"}
            hoverColor={"hover:bg-warnTheme-200"}
            icon={<ElectricCarIcon sx={{ fontSize: "20px" }} />}
          />
        </div>
      </div>
    </div>
  );
};
