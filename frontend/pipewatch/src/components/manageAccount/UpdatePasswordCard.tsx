import { useTranslation } from "react-i18next";
import { Button, Input } from "@headlessui/react";
import { useNavigate } from "react-router-dom";
import { useState, ChangeEvent, FocusEvent } from "react";

const UpdatePasswordCard = () => {
  const { t } = useTranslation();
  const navigate = useNavigate();

  // check current password
  const tempCurrentPassword = "paori1234@";

  const [inputPassword, setInputPassword] = useState("");
  const [showCurrentPasswordError, setCurrentPasswordError] = useState(false);

  const handleInputPasswordChange = (e: ChangeEvent<HTMLInputElement>) => {
    const value = e.target.value;
    setInputPassword(value);
    // 기존 비밀번호와 일치 여부 확인
    setCurrentPasswordError(tempCurrentPassword !== value);
  };

  const handleCurrentPasswordBlur = (e: FocusEvent<HTMLInputElement>) => {
    const value = e.target.value;
    setCurrentPasswordError(tempCurrentPassword !== value);
  };

  // check new password
  const [newPassword, setNewPassword] = useState("");
  const [showNewPasswordError, setShowNewPasswordError] = useState(false);
  // 기존 비밀번호와 불일치 여부 확인
  const [showPasswordMismatchError, setShowPasswordMismatchError] =
    useState(false);

  const passwordPattern =
    /^(?=.*[A-Za-z])(?=.*\d)(?=.*[!@#$%^&*])[A-Za-z\d!@#$%^&*]{8,}$/;

  // 유효성 검사
  const validatePassword = (value: string) => passwordPattern.test(value);

  // 비밀번호 이벤트 핸들러
  const handleNewPasswordChange = (e: ChangeEvent<HTMLInputElement>) => {
    const value = e.target.value;
    setNewPassword(value);
    // 비밀번호 유효성 확인
    setShowNewPasswordError(!validatePassword(value));
    // 기존 비밀번호와 불일치 여부 확인
    setShowPasswordMismatchError(tempCurrentPassword === value);
  };

  const handleNewPasswordBlur = (e: FocusEvent<HTMLInputElement>) => {
    setShowNewPasswordError(!validatePassword(e.target.value));
  };

  return (
    <div className="w-[500px] flex flex-col bg-block rounded-[30px] p-[50px] gap-[40px] text-white">
      <div className="flex justify-center font-semibold text-[28px]">
        {t("manageAccount.dashboard.updatePassword")}
      </div>

      {/* current password */}
      <div className="flex flex-col gap-[20px]">
        <Input
          type="password"
          value={inputPassword}
          onChange={handleInputPasswordChange}
          onBlur={handleCurrentPasswordBlur}
          // aria-invalid={showNewasswordError}
          // aria-describedby={showNewasswordError ? "passwordError" : undefined}
          className="h-[56px] w-full px-5 text-gray-500 rounded-[5px] peer"
          placeholder={t("manageAccount.updatePassword.currentPassword")}
          required
        />
        {showCurrentPasswordError && (
          <span className="w-full px-2 text-[14px] whitespace-normal text-warn break-keep">
            {t("manageAccount.updatePassword.passwordMismatchError")}
          </span>
        )}

        {/* new password */}
        <Input
          type="password"
          value={newPassword}
          onChange={handleNewPasswordChange}
          onBlur={handleNewPasswordBlur}
          // aria-invalid={showNewPasswordError}
          // aria-describedby={showNewPasswordError ? "passwordError" : undefined}
          className="h-[56px] w-full px-5 text-gray-500 rounded-[5px] peer"
          placeholder={t("manageAccount.updatePassword.newPassword")}
          required
        />
        {showNewPasswordError && (
          <span className="w-full px-2 whitespace-normal text-warn break-keep">
            {t("account.passwordError")}
          </span>
        )}
        {showPasswordMismatchError && (
          <span className="w-full px-2 whitespace-normal text-warn break-keep">
            {t("manageAccount.updatePassword.matchError")}
          </span>
        )}

        {/* new password check */}
        <Input
          type="password"
          value={newPassword}
          onChange={handleNewPasswordChange}
          onBlur={handleNewPasswordBlur}
          // aria-invalid={showNewPasswordError}
          // aria-describedby={showNewPasswordError ? "passwordError" : undefined}
          className="h-[56px] w-full px-5 text-gray-500 rounded-[5px] peer"
          placeholder={t("manageAccount.updatePassword.confirmNewPassword")}
          required
        />
      </div>

      <div className="flex flex-col gap-[20px]">
        <Button
          className={`flex items-center h-[56px] w-full px-[30px] text-white rounded-lg bg-button-background`}
          onClick={() => navigate("/account/manage/update-pw/completed")}
        >
          {t("manageAccount.dashboard.updatePassword")}
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

export default UpdatePasswordCard;
