import { useTranslation } from "react-i18next";
import { Button } from "@headlessui/react";
import { useLocation } from "react-router-dom";

export const FindPasswordPending = () => {
  const { t } = useTranslation();
  const location = useLocation();

  const email = location.state.email;

  return (
    <div
      className="flex flex-col gap-[20px] items-center justify-center"
      style={{ height: "calc(100vh - 220px)" }}
    >
      <div className="flex flex-col bg-block rounded-[30px] p-[50px] gap-[40px] text-white">
        <div className="flex justify-center font-semibold text-[28px]">
          비밀번호 변경 메일 발송 완료
        </div>
        <div className="flex flex-col gap-2">
          <div className="flex gap-2 text-[24px] justify-center m-2">
            입력 이메일:
            <u>
              <b>{email}</b>
            </u>
          </div>
          <div className="flex justify-center text-[20px]">
            위 메일 주소로 새로운 비밀번호 설정을 위한 안내 메일이
            발송되었습니다.
          </div>
          <div className="flex justify-center text-[20px]">
            해당 메일을 확인 후 다시 접속해주세요.
          </div>
        </div>
        <Button
          className="flex items-center justify-center h-[56px] w-full px-[30px] text-white rounded-[20px] 
           bg-button-background hover:bg-button-background/80
             "
          onClick={() => (window.location.href = "/")}
        >
          {t("manageAccount.goToHomepage")}
        </Button>
      </div>
    </div>
  );
};
