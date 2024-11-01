import { useTranslation } from "react-i18next";
import { Button } from "@headlessui/react";
import { useNavigate } from "react-router-dom";
import ChevronRightIcon from "@mui/icons-material/ChevronRight";

const Dashboard = () => {
  const { t } = useTranslation();
  const navigate = useNavigate();

  return (
    <div className="w-[500px] flex flex-col bg-block rounded-[30px] p-[50px] gap-[40px] text-white">
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
        <a className="underline">paori@gmail.com</a>
      </div>
    </div>
  );
};

export default Dashboard;
