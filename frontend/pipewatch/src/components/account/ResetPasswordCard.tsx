import { useTranslation } from "react-i18next";
import { Button, Input } from "@headlessui/react";
import { useNavigate } from "react-router-dom";
import { useState, ChangeEvent, FocusEvent } from "react";

const ResetPasswordCard = () => {
  const { t } = useTranslation();
  const navigate = useNavigate();

  const passwordPattern =
    /^(?=.*[A-Za-z])(?=.*\d)(?=.*[!@#$%^&*])[A-Za-z\d!@#$%^&*]{8,}$/;

  const [passwordData, setPasswordData] = useState({
    newPassword: "",
    checkNewPassword: "",
  });
  const [passwordErrors, setPasswordErrors] = useState({
    newPasswordError: false,
    checkNewPasswordError: false,
    newPasswordMismatchError: false,
  });

  // 공통 핸들러 함수
  const updatePasswordData = (field: string, value: string) => {
    setPasswordData((prevData) => ({ ...prevData, [field]: value }));

    setPasswordErrors((prevErrors) => ({
      ...prevErrors,
      newPasswordError:
        field === "newPassword"
          ? !passwordPattern.test(value)
          : prevErrors.newPasswordError,
      checkNewPasswordError:
        field === "checkNewPassword"
          ? !passwordPattern.test(value)
          : prevErrors.checkNewPasswordError,
      newPasswordMismatchError:
        field === "checkNewPassword"
          ? value !== passwordData.newPassword
          : prevErrors.newPasswordMismatchError,
    }));
  };

  const handlePasswordChange = (field: string, value: string) => {
    updatePasswordData(field, value);
  };

  const handlePasswordBlur = (field: string, value: string) => {
    updatePasswordData(field, value);
  };

  const isFormValid =
    Object.values(passwordData).every((val) => val !== "") &&
    Object.values(passwordErrors).every((error) => !error);

  return (
    <div className="w-[500px] flex flex-col bg-block rounded-[30px] p-[50px] gap-[40px] text-white">
      <div className="flex justify-center font-semibold text-[28px]">
        {t("account.resetPassword.title")}
      </div>
      <div className="text-center whitespace-normal cursor-pointer space-normal">
        {t("account.resetPassword.instruction")}
      </div>

      <div className="flex flex-col gap-[20px]">
        <Input
          type="password"
          value={passwordData.newPassword}
          onChange={(e: ChangeEvent<HTMLInputElement>) =>
            handlePasswordChange("newPassword", e.target.value)
          }
          onBlur={(e: FocusEvent<HTMLInputElement>) =>
            handlePasswordBlur("newPassword", e.target.value)
          }
          className="h-[56px] w-full px-5 text-gray-500 rounded-[5px] peer"
          placeholder={t("manageAccount.updatePassword.newPassword")}
          required
        />
        {passwordErrors.newPasswordError && (
          <span className="w-full px-2 text-[14px] whitespace-normal text-warn break-keep">
            {t("account.passwordError")}
          </span>
        )}

        <Input
          type="password"
          value={passwordData.checkNewPassword}
          onChange={(e: ChangeEvent<HTMLInputElement>) =>
            handlePasswordChange("checkNewPassword", e.target.value)
          }
          onBlur={(e: FocusEvent<HTMLInputElement>) =>
            handlePasswordBlur("checkNewPassword", e.target.value)
          }
          className="h-[56px] w-full px-5 text-gray-500 rounded-[5px] peer"
          placeholder={t("manageAccount.updatePassword.confirmNewPassword")}
          required
        />
        {passwordErrors.checkNewPasswordError ? (
          <span className="w-full px-2 text-[14px] whitespace-normal text-warn break-keep">
            {t("account.passwordError")}
          </span>
        ) : (
          passwordErrors.newPasswordMismatchError && (
            <span className="w-full px-2 text-[14px] whitespace-normal text-warn break-keep">
              {t("manageAccount.updatePassword.newPasswordMismatchError")}
            </span>
          )
        )}
      </div>

      <div className="flex flex-col gap-[20px]">
        <Button
          className={`flex items-center h-[56px] w-full px-[30px] justify-center text-white rounded-[20px]
            ${
              isFormValid
                ? "bg-button-background hover:bg-button-background/80"
                : "bg-gray-800 cursor-not-allowed"
            }
          `}
          onClick={() => navigate("/account/auth/reset-pw/completed")}
          disabled={!isFormValid}
        >
          {t("account.resetPassword.title")}
        </Button>
      </div>
    </div>
  );
};

export default ResetPasswordCard;
