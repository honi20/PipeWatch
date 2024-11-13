import { useState } from "react";
import { useTranslation } from "react-i18next";
import { Button } from "@headlessui/react";
import { useNavigate } from "react-router-dom";
import ChevronRightIcon from "@mui/icons-material/ChevronRight";

import { ReVerifyModal } from "@components/manageAccount/ReVerifyModal";

const Dashboard = () => {
  const [isOpen, setIsOpen] = useState(false);

  const { t } = useTranslation();
  const navigate = useNavigate();

  const role = sessionStorage.getItem("role");
  const userState = sessionStorage.getItem("userState");

  return (
    <div className="w-[500px] flex flex-col bg-block rounded-[30px] p-[50px] gap-[40px] text-white">
      <ReVerifyModal isOpen={isOpen} onClose={() => setIsOpen(false)} />

      <div className="flex justify-center font-semibold text-[28px]">
        {t("manageAccount.dashboard.title")}
      </div>

      <div className="flex flex-col gap-[20px]">
        <Button
          className={`flex items-center h-[56px] w-full px-[30px] text-white rounded-lg bg-button-background`}
          onClick={() => navigate("/account/manage/edit-info")}
        >
          <ChevronRightIcon />
          {t("manageAccount.dashboard.editInfo")}
        </Button>

        {/* 현재 user의 Role = 'USER' && state = 'ACTIVE'일 때 => 기업 인증 거부된 상태 */}
        {role === "USER" && userState === "ACTIVE " && (
          <Button
            className={`flex items-center h-[56px] w-full px-[30px] text-white rounded-lg bg-button-background`}
            onClick={() => setIsOpen(true)}
          >
            <ChevronRightIcon />
            {t("manageAccount.dashboard.reverificationRequest")}
          </Button>
        )}

        <Button
          className={`flex items-center h-[56px] w-full px-[30px] text-white rounded-lg bg-button-background`}
          onClick={() => navigate("/account/manage/update-pw")}
        >
          <ChevronRightIcon />
          {t("manageAccount.dashboard.updatePassword")}
        </Button>

        <Button
          className={`flex items-center h-[56px] w-full px-[30px] text-white rounded-lg bg-button-background`}
          onClick={() => navigate("/account/manage/withdrawal")}
        >
          <ChevronRightIcon />
          {t("manageAccount.dashboard.withdrawal")}
        </Button>
      </div>

      <div className="flex justify-center gap-2 whitespace-normal space-normal">
        {t("manageAccount.dashboard.inquiry")}
        <a className="underline cursor-pointer">paori@gmail.com</a>
      </div>
    </div>
  );
};

export default Dashboard;
