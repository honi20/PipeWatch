import { Trans, useTranslation } from "react-i18next";
import { Button, Input } from "@headlessui/react";
import { useNavigate } from "react-router-dom";
import { useState, ChangeEvent, FocusEvent } from "react";

const WithdrawalCard = () => {
  const { t } = useTranslation();
  const navigate = useNavigate();
  const [password, setPassword] = useState("");
  const [showPasswordError, setShowPasswordError] = useState(false);
  const [isNoticeChecked, setIsNoticeChecked] = useState(false);

  // 임시 가입 이메일
  const tempRegisteredEmail = "zoozoofin7755@gmail.com";

  const passwordPattern =
    /^(?=.*[A-Za-z])(?=.*\d)(?=.*[!@#$%^&*])[A-Za-z\d!@#$%^&*]{8,}$/;

  // 유효성 검사
  const validatePassword = (value: string) => passwordPattern.test(value);

  // 비밀번호 이벤트 핸들러
  const handlePasswordChange = (e: ChangeEvent<HTMLInputElement>) => {
    const value = e.target.value;
    setPassword(value);
    setShowPasswordError(!validatePassword(value));
  };
  const handlePasswordBlur = (e: FocusEvent<HTMLInputElement>) => {
    setShowPasswordError(!validatePassword(e.target.value));
  };

  // 회원탈퇴 버튼 활성화
  const isFormValid: boolean =
    !showPasswordError && password !== "" && isNoticeChecked;

  return (
    <div className="w-[500px] flex flex-col bg-block rounded-[30px] p-[50px] gap-[40px] text-white">
      <div className="flex justify-center font-semibold text-[28px]">
        {t("manageAccount.dashboard.withdrawal")}
      </div>

      <div className="flex px-[20px] text-[16px]">
        <div className="flex-[3] font-bold">
          {t("manageAccount.withdrawal.registrationEmail")}
        </div>
        <div className="flex-[4]">{tempRegisteredEmail}</div>
      </div>

      {/* 탈퇴 안내 Box */}
      <div className="border-solid border-[1px] border-warn py-[30px] px-[20px] rounded-[10px]">
        <ul className="list-disc list-outside px-[20px]">
          <li>
            <Trans
              i18nKey="manageAccount.withdrawal.withdrawalNotices.0"
              components={{
                strong: <span className="font-bold text-warn" />,
              }}
            />
          </li>
          <li> {t("manageAccount.withdrawal.withdrawalNotices.1")}</li>
          <li> {t("manageAccount.withdrawal.withdrawalNotices.2")}</li>
          <li> {t("manageAccount.withdrawal.withdrawalNotices.3")}</li>
        </ul>
      </div>

      <div className="flex gap-2 whitespace-normal space-normal">
        <input
          id="checkbox"
          type="checkbox"
          className="w-10"
          onClick={() => {
            setIsNoticeChecked(!isNoticeChecked);
          }}
        />
        <label htmlFor="checkbox">
          {t("manageAccount.withdrawal.withdrawalNoticeConfirmation")}
        </label>
      </div>

      {/* password */}
      <div className="flex flex-col gap-[2px]">
        <Input
          type="password"
          value={password}
          onChange={handlePasswordChange}
          onBlur={handlePasswordBlur}
          aria-invalid={showPasswordError}
          aria-describedby={showPasswordError ? "passwordError" : undefined}
          className="h-[56px] w-full px-5 text-gray-500 rounded-[5px] peer"
          placeholder={t("account.password")}
          required
        />
        {showPasswordError && (
          <span className="w-full px-2 whitespace-normal text-warn break-keep">
            {t("account.passwordError")}
          </span>
        )}
      </div>

      <div className="flex flex-col gap-[20px]">
        <Button
          className={`flex items-center justify-center h-[56px] w-full px-[30px] text-white rounded-[20px] 
            ${
              isFormValid
                ? "bg-button-background hover:bg-button-background/80"
                : "bg-gray-800 cursor-not-allowed"
            }`}
          onClick={() => {}}
          disabled={!isFormValid}
        >
          {t("manageAccount.withdrawal.confirmWithdrawal")}
        </Button>
      </div>

      <div
        className="flex justify-center gap-2 underline whitespace-normal cursor-pointer space-normal"
        onClick={() => navigate("/account/manage")}
      >
        {t("manageAccount.goToPreviousCard")}
      </div>
    </div>
  );
};

export default WithdrawalCard;
