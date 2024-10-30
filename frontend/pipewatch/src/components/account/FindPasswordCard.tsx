import { useTranslation } from "react-i18next";
import { Link } from "react-router-dom";
import { Input, Button } from "@headlessui/react";

export const FindPasswordCard = () => {
  const { t } = useTranslation();
  return (
    <div className="w-[500px] flex flex-col bg-block rounded-[30px] p-[50px] gap-[40px]">
      <div className="flex justify-center font-semibold text-[28px]">
        {t("account.login")}
      </div>
    </div>
  );
};
