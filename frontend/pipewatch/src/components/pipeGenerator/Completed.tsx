import { useTranslation } from "react-i18next";
import { IconButton } from "@components/common/IconButton";
import { useNavigate } from "react-router-dom";

import CheckCircleIcon from "@mui/icons-material/CheckCircle";

export const Completed = () => {
  const { t } = useTranslation();
  const navigate = useNavigate();

  return (
    <div className="p-[40px] h-full flex flex-col justify-center items-center">
      <CheckCircleIcon sx={{ fontSize: "96px", color: "#499B50" }} />
      <h1 className="text-[40px] font-bold ">
        {t("pipeGenerator.completed.title")}
      </h1>
      <p className="text-[20px]">
        {t("pipeGenerator.completed.instructions.completionMessage")}
      </p>
      <p className="text-[20px]">
        {t("pipeGenerator.completed.instructions.checkModelList")}
      </p>

      <IconButton
        handleClick={() => navigate("/pipe-viewer")}
        text={t("pipeGenerator.completed.buttons.viewModel")}
        color={"bg-button-background"}
        hoverColor={"hover:bg-button-background/80"}
      />
    </div>
  );
};
