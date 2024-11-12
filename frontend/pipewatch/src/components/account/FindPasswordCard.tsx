import { useTranslation, Trans } from "react-i18next";
import { Input, Button } from "@headlessui/react";
import { useState, ChangeEvent, FocusEvent } from "react";
import { Link, useNavigate } from "react-router-dom";

import { baseInstance } from "@src/stores/apiClient";

export const FindPasswordCard = () => {
  const { t } = useTranslation();
  const navigate = useNavigate();

  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [showNameError, setShowNameError] = useState(false);
  const [showEmailError, setShowEmailError] = useState(false);

  const emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;

  // 유효성 검사
  const validateName = (value: string) => value.trim() !== "";
  const validateEmail = (value: string) => emailPattern.test(value);

  // 이름 이벤트 핸들러
  const handleNameChange = (e: ChangeEvent<HTMLInputElement>) => {
    setName(e.target.value);
  };
  const handleNameBlur = (e: FocusEvent<HTMLInputElement>) => {
    setShowNameError(!validateName(e.target.value));
  };

  // 이메일 이벤트 핸들러
  const handleEmailChange = (e: ChangeEvent<HTMLInputElement>) => {
    const value = e.target.value;
    setEmail(value);
    setShowEmailError(value !== "" && !validateEmail(value));
  };
  const handleEmailBlur = (e: FocusEvent<HTMLInputElement>) => {
    setShowEmailError(!validateEmail(e.target.value));
  };

  // 버튼 활성화
  const isFormValid: boolean =
    !showNameError && !showEmailError && name !== "" && email !== "";

  const confirmFindPassword = (name: string, email: string) => {
    baseInstance
      .post("/api/auth/password-reset/request", {
        name: name,
        email: email,
      })
      .then((res) => {
        console.log("비밀번호 변경 메일 전송됨: ", res);
        navigate("/account/auth/find-pw/pending", {
          state: { email: email },
        });
      })
      .catch((err) => {
        console.log(err);
      });
    return <></>;
  };

  return (
    <div className="w-[500px] flex flex-col bg-block rounded-[30px] p-[50px] gap-[40px] text-white">
      {/* header */}
      <div className="flex flex-col gap-[10px]">
        {/* title */}
        <div className="flex justify-center font-semibold text-[28px]">
          {t("account.findPassword")}
        </div>
        {/* subtitle */}
        <div className="text-center whitespace-normal break-keep">
          <Trans i18nKey="account.passwordReset" components={{ br: <br /> }} />
        </div>
      </div>

      {/* content */}
      <div className="flex flex-col gap-[20px]">
        {/* name */}
        <div className="flex flex-col gap-[2px]">
          <Input
            type="text"
            value={name}
            onChange={handleNameChange}
            onBlur={handleNameBlur}
            aria-invalid={showNameError}
            aria-describedby={showNameError ? "nameError" : undefined}
            className="h-[56px] w-full px-5 text-gray-500 rounded-[5px]"
            placeholder={t("account.name")}
            required
          />
          {showNameError && (
            <span className="w-full px-2 whitespace-normal text-warn break-keep">
              {t("account.enterYourName")}
            </span>
          )}
        </div>

        {/* email */}
        <div className="flex flex-col gap-[2px]">
          <Input
            type="email"
            value={email}
            onChange={handleEmailChange}
            onBlur={handleEmailBlur}
            aria-invalid={showEmailError}
            aria-describedby={showEmailError ? "emailError" : undefined}
            className="h-[56px] w-full px-5 text-gray-500 rounded-[5px]"
            placeholder={t("account.email")}
            required
          />
          {showEmailError && (
            <span
              id="emailError"
              className="w-full px-2 whitespace-normal text-warn break-keep"
            >
              {t("account.emailError")}
            </span>
          )}
        </div>
      </div>

      {/* button */}
      <Button
        className={`h-[56px] w-full text-white rounded-lg ${
          isFormValid
            ? "bg-primary-500"
            : "bg-button-background cursor-not-allowed"
        }`}
        disabled={!isFormValid}
        onClick={() => confirmFindPassword(name, email)}
      >
        {t("account.sendPasswordResetEmail")}
      </Button>

      {/* footer */}
      <div className="flex justify-center underline">
        <Link to="/account/auth/login">{t("account.goBackToLogin")}</Link>
      </div>
    </div>
  );
};
