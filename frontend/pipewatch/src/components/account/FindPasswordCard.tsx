import { useTranslation } from "react-i18next";
import { Input, Button } from "@headlessui/react";
import { useState, ChangeEvent, FocusEvent } from "react";
import { Link } from "react-router-dom";

export const FindPasswordCard = () => {
  const { t } = useTranslation();
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
          가입하신 이메일 주소로
          <br />
          새로운 비밀번호 설정을 위한 안내 메일이 발송됩니다.
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
              성명을 입력해주세요.
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
      >
        비밀번호 변경 메일 보내기
      </Button>

      {/* footer */}
      <div className="flex justify-center underline">
        <Link to="/account/auth/login">로그인 페이지로 돌아가기</Link>
      </div>
    </div>
  );
};
