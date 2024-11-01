import { useState } from "react";
import { useTranslation } from "react-i18next";
import { useNavigate } from "react-router-dom";

import { IconButton } from "@components/common/IconButton";
import { StatusBar } from "@components/common/StatusBar";

import CheckCircleIcon from "@mui/icons-material/CheckCircle";
import CancelIcon from "@mui/icons-material/Cancel";
import ReplayIcon from "@mui/icons-material/Replay";

export const UploadModelAuto = () => {
  const { t } = useTranslation();
  const navigate = useNavigate();

  const [status, setStatus] = useState<"initial" | "success" | "fail">(
    "initial"
  );

  const Result = ({ status }: { status: string }) => {
    if (status === "success") {
      return t(
        "pipeGenerator.uploadModel.captureStarted.statusMessages.success"
      );
    } else if (status === "fail") {
      return t(
        "pipeGenerator.uploadModel.captureStarted.statusMessages.failed"
      );
    } else {
      return null;
    }
  };

  // 저장 버튼 Click Action
  const handleSave = () => {
    // POST 함수 추가 예정

    // 모델 렌더링 페이지로 이동
    navigate("/pipe-generator/input-data");
  };

  return (
    <div>
      <p className="text-[16px]">
        {t(
          "pipeGenerator.uploadModel.captureStarted.instructions.modelingProcess"
        )}
      </p>
      <StatusBar
        text={t(
          "pipeGenerator.takePhoto.connectRCCar.statusMessages.completed"
        )}
        icon={<CheckCircleIcon sx={{ fontSize: "20px" }} />}
        color={"bg-success"}
      />
      <div className="flex justify-center w-full my-[20px]">
        <IconButton
          handleClick={() => handleSave()}
          text={t("pipeGenerator.commonButtons.save")}
          color={"bg-primary-500"}
          hoverColor={"hover:bg-primary-500/80"}
        />
      </div>
      <div className="flex justify-center w-full my-[30px]">
        <div className="relative text-gray-800 py-[60px] px-[50px] w-[500px] h-[300px] flex flex-col gap-[20px] justify-center items-center bg-whiteBox shadow-md rounded-[12px] shadow-gray-500">
          모델링 상태 그래픽(완료/실패)
        </div>
      </div>
      <StatusBar
        text={t("pipeGenerator.takePhoto.connectRCCar.statusMessages.failed")}
        icon={<CancelIcon sx={{ fontSize: "20px" }} />}
        color={"bg-warn"}
      />
      <div className="flex justify-center w-full my-[20px]">
        <IconButton
          handleClick={() => navigate("/pipe-generator/take-photo")}
          text={t("pipeGenerator.commonButtons.retry")}
          color={"bg-gray-800"}
          hoverColor={"hover:bg-gray-800/80"}
          icon={<ReplayIcon sx={{ fontSize: "20px" }} />}
        />
      </div>
    </div>
  );
};
