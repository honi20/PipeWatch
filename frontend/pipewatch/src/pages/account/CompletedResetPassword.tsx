import { useTranslation } from "react-i18next";
import { useNavigate } from "react-router-dom";
import { Button } from "@headlessui/react";

export const CompletedResetPassword = () => {
  const { t } = useTranslation();

  const navigate = useNavigate();

  return (
    <div
      className="flex flex-col gap-[20px] items-center justify-center"
      style={{ height: "calc(100vh - 220px)" }}
    >
      <div className="flex flex-col bg-block rounded-[30px] p-[50px] gap-[40px] text-white">
        <div className="flex justify-center font-semibold text-[28px]">
          {t("account.resetPassword.complete")}
        </div>
        <Button
          className="flex items-center justify-center h-[56px] w-full px-[30px] text-white rounded-[20px] 
           bg-button-background hover:bg-button-background/80
             "
          onClick={() => navigate("/account/auth/login")}
        >
          {t("account.resetPassword.goToLoginPage")}
        </Button>
      </div>
    </div>
  );
};
