import { useTranslation } from "react-i18next";
import { Button, Input } from "@headlessui/react";
import { useNavigate } from "react-router-dom";
import { useState, ChangeEvent } from "react";

import { getApiClient } from "@src/stores/apiClient";

const UpdatePasswordCard = () => {
  const { t } = useTranslation();
  const navigate = useNavigate();

  const apiClient = getApiClient();

  const updatePassword = async (password: string, newPassword: string) => {
    try {
      const res = await apiClient.patch(`/api/users/modify-pwd`, {
        password: password,
        newPassword: newPassword,
      });
      console.log("비밀번호 수정 성공: ", res.data.header.message);
      navigate("/account/manage/update-pw/completed");
    } catch (err) {
      console.log(err);
    }
  };

  const passwordPattern =
    /^(?=.*[A-Za-z])(?=.*\d)(?=.*[!@#$%^&*])[A-Za-z\d!@#$%^&*]{8,}$/;

  const [passwordData, setPasswordData] = useState({
    inputPassword: "",
    newPassword: "",
    checkNewPassword: "",
  });
  const [passwordErrors, setPasswordErrors] = useState({
    currentPasswordError: false,
    newPasswordError: false,
    passwordMismatchError: false,
    checkNewPasswordError: false,
  });

  // 핸들러 함수
  const handlePasswordChange = (field: string, value: string) => {
    setPasswordData((prevData) => ({ ...prevData, [field]: value }));

    switch (field) {
      case "inputPassword":
        setPasswordErrors((prevErrors) => ({
          ...prevErrors,
        }));
        break;
      case "newPassword":
        setPasswordErrors((prevErrors) => ({
          ...prevErrors,
          newPasswordError: !passwordPattern.test(value),
        }));
        break;
      case "checkNewPassword":
        setPasswordErrors((prevErrors) => ({
          ...prevErrors,
          checkNewPasswordError: passwordData.newPassword !== value,
        }));
        break;
      default:
        break;
    }
  };

  const isFormValid =
    Object.values(passwordData).every((val) => val !== "") &&
    Object.values(passwordErrors).every((error) => !error);

  return (
    <div className="w-[500px] flex flex-col bg-block rounded-[30px] p-[50px] gap-[40px] text-white">
      <div className="flex justify-center font-semibold text-[28px]">
        {t("manageAccount.dashboard.updatePassword")}
      </div>

      <div className="flex flex-col gap-[20px]">
        <Input
          type="password"
          value={passwordData.inputPassword}
          onChange={(e: ChangeEvent<HTMLInputElement>) =>
            handlePasswordChange("inputPassword", e.target.value)
          }
          className="h-[56px] w-full px-5 text-gray-500 rounded-[5px] peer"
          placeholder={t("manageAccount.updatePassword.currentPassword")}
          required
        />
        {passwordErrors.currentPasswordError && (
          <span className="w-full px-2 text-[14px] whitespace-normal text-warn break-keep">
            {t("manageAccount.updatePassword.passwordMismatchError")}
          </span>
        )}

        <Input
          type="password"
          value={passwordData.newPassword}
          onChange={(e: ChangeEvent<HTMLInputElement>) =>
            handlePasswordChange("newPassword", e.target.value)
          }
          className="h-[56px] w-full px-5 text-gray-500 rounded-[5px] peer"
          placeholder={t("manageAccount.updatePassword.newPassword")}
          required
        />
        {passwordErrors.newPasswordError ? (
          <span className="w-full px-2 text-[14px] whitespace-normal text-warn break-keep">
            {t("account.passwordError")}
          </span>
        ) : (
          passwordErrors.passwordMismatchError && (
            <span className="w-full px-2 text-[14px] whitespace-normal text-warn break-keep">
              {t("manageAccount.updatePassword.newPasswordMatchError")}
            </span>
          )
        )}

        <Input
          type="password"
          value={passwordData.checkNewPassword}
          onChange={(e: ChangeEvent<HTMLInputElement>) =>
            handlePasswordChange("checkNewPassword", e.target.value)
          }
          className="h-[56px] w-full px-5 text-gray-500 rounded-[5px] peer"
          placeholder={t("manageAccount.updatePassword.confirmNewPassword")}
          required
        />
        {passwordErrors.checkNewPasswordError && (
          <span className="w-full px-2 text-[14px] whitespace-normal text-warn break-keep">
            {t("manageAccount.updatePassword.newPasswordMismatchError")}
          </span>
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
          onClick={() =>
            updatePassword(passwordData.inputPassword, passwordData.newPassword)
          }
          disabled={!isFormValid}
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
