import { useTranslation } from "react-i18next";
import { useParams, useNavigate } from "react-router-dom";
import { Button } from "@headlessui/react";

export const CompletedManageAccount = () => {
  const { t } = useTranslation();

  const { action } = useParams();
  const navigate = useNavigate();

  const titles = {
    "edit-info": `${t("manageAccount.editInfo.complete")}`,
    "update-pw": `${t("manageAccount.updatePassword.complete")}`,
    withdrawal: `${t("manageAccount.withdrawal.complete")}`,
  };

  return (
    <div
      className="flex flex-col gap-[20px] items-center justify-center"
      style={{ height: "calc(100vh - 220px)" }}
    >
      <div className="flex flex-col bg-block rounded-[30px] p-[50px] gap-[40px] text-white">
        <div className="flex justify-center font-semibold text-[28px]">
          {titles[action as keyof typeof titles] || t("error.access")}
        </div>
        <Button
          className="flex items-center justify-center h-[56px] w-full px-[30px] text-white rounded-[20px] 
           bg-button-background hover:bg-button-background/80
             "
          onClick={() => navigate("/")}
        >
          {t("manageAccount.goToHomepage")}
        </Button>
      </div>
    </div>
  );
};
