import NoAccessImage from "@assets/images/status/no_access.png";
import { Button } from "@headlessui/react";
import { useNavigate } from "react-router-dom";
import { useTranslation } from "react-i18next";

export const AccessBlocked = () => {
  const navigate = useNavigate();
  const { t } = useTranslation();

  return (
    <div
      className="flex flex-col items-center justify-center w-full"
      style={{ height: "calc(100vh - 220px)" }}
    >
      <img src={NoAccessImage} width="350px" alt="No Access" />

      <div className="text-center font-bold text-[40px] my-[20px]">
        {t("error.accessBlocked")}
      </div>

      <Button
        className={`h-[56px] px-[20px] border-[1px] border-solid dark:border-white  text-white rounded-lg
           dark:bg-button-background bg-primary-500
        `}
        disabled={false}
        onClick={() => navigate("/")}
      >
        {t("error.toHome")}
      </Button>
    </div>
  );
};
