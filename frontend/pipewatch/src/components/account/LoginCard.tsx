import { useTranslation } from "react-i18next";
import { Link } from "react-router-dom";
import { Input, Button } from "@headlessui/react";
import { useState, ChangeEvent } from "react";

const LoginCard = () => {
  const { t } = useTranslation();
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [isEmailValid, setIsEmailValid] = useState(true);
  const [isPasswordValid, setIsPasswordValid] = useState(true);

  const emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
  const passwordPattern =
    /^(?=.*[A-Za-z])(?=.*\d)(?=.*[!@#$%^&*])[A-Za-z\d!@#$%^&*]{8,}$/;

  // 이메일 이벤트 핸들러
  const handleEmailChange = (e: ChangeEvent<HTMLInputElement>) => {
    const value = e.target.value;
    setEmail(value);
    setIsEmailValid(emailPattern.test(value) || value === "");
  };

  // 비밀번호 이벤트 핸들러
  const handlePasswordChange = (e: ChangeEvent<HTMLInputElement>) => {
    const value = e.target.value;
    setPassword(value);
    setIsPasswordValid(passwordPattern.test(value) || value === "");
  };

  // 로그인 버튼 활성화
  const isFormValid: boolean =
    isEmailValid && isPasswordValid && email !== "" && password !== "";

  return (
    <div className="w-[500px] flex flex-col bg-block rounded-[30px] p-[50px] gap-[40px]">
      {/* header */}
      <div className="flex justify-center font-semibold text-[28px]">
        {t("account.login")}
      </div>

      {/* content */}
      <div className="flex flex-col gap-[20px]">
        {/* email */}
        <div className="flex flex-col gap-[2px]">
          <Input
            type="email"
            value={email}
            onChange={handleEmailChange}
            className="h-[56px] w-full px-5 text-gray-500 rounded-[5px]"
            placeholder={t("account.email")}
            required
          />
          {!isEmailValid && (
            <span className="w-full px-2 whitespace-normal text-warn break-keep">
              {t("account.emailError")}
            </span>
          )}
        </div>

        {/* password */}
        <div className="flex flex-col gap-[2px]">
          <Input
            type="password"
            value={password}
            onChange={handlePasswordChange}
            className="h-[56px] w-full px-5 text-gray-500 rounded-[5px] peer"
            placeholder={t("account.password")}
            required
          />
          {!isPasswordValid && (
            <span className="w-full px-2 whitespace-normal text-warn break-keep">
              {t("account.passwordError")}
            </span>
          )}
        </div>
      </div>

      {/* button */}
      {/* 로그인 성공 -> 메인화면으로 돌아감 */}
      {/* 로그인 실패 -> 실패 배너 생성 */}
      <Button
        className={`h-[56px] w-full text-white rounded-lg ${
          isFormValid
            ? "bg-primary-500"
            : "bg-button-background cursor-not-allowed"
        }`}
        disabled={!isFormValid}
      >
        {t("account.login")}
      </Button>

      {/* footer */}
      <div className="flex justify-center gap-2">
        <Link to="/account/auth/find-pw">{t("account.findPassword")}</Link>
        <span>|</span>
        <Link to="/account/auth/sign-up">{t("account.signUp")}</Link>
      </div>
    </div>
  );
};

export default LoginCard;
