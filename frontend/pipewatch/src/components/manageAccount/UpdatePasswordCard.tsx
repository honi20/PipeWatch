import { useTranslation } from "react-i18next";
import { Button } from "@headlessui/react";
import { useNavigate } from "react-router-dom";

const UpdatePasswordCard = () => {
  const { t } = useTranslation();
  const navigate = useNavigate();

  return (
    <div className="w-[500px] flex flex-col bg-block rounded-[30px] p-[50px] gap-[40px] text-white">
      <div className="flex justify-center font-semibold text-[28px]">
        {t("manageAccount.dashboard.updatePassword")}
      </div>

      <div className="flex flex-col gap-[20px]">
        <Button
          className={`flex items-center h-[56px] w-full px-[30px] text-white rounded-lg bg-button-background`}
          onClick={() => {}}
        >
          {t("manageAccount.dashboard.updatePassword")}
        </Button>
      </div>

      <div
        className="flex justify-center gap-2 underline whitespace-normal space-normal"
        onClick={() => navigate("/account/manage")}
      >
        {t("manageAccount.goToPreviousCard")}
      </div>
    </div>
  );
};

export default UpdatePasswordCard;
