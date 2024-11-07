import axios from "axios";

import { useUserStore } from "@src/stores/userStore";
import { getApiClient } from "@src/stores/apiClient";

import { useTranslation } from "react-i18next";
import { Link, useNavigate } from "react-router-dom";
import { Input, Button } from "@headlessui/react";
import { useState, ChangeEvent, FocusEvent } from "react";

const API_URL = import.meta.env.VITE_URL;

const LoginCard = () => {
  const { t } = useTranslation();
  const navigate = useNavigate();

  const { setLogin, setUserInfo } = useUserStore();

  // UserInfo 호출, store 저장
  const saveUserInfo = async () => {
    const apiClient = getApiClient();
    try {
      const res = await apiClient.get("/api/users/mypage");
      console.log(res.data.body);
      setUserInfo(res.data.body);
    } catch (err) {
      console.log(err);
    }
  };

  // pipewatch_admin@paori.com
  // pipewatch941555!

  const login = (email: string, password: string) => {
    axios
      .post(`${API_URL}/api/auth/signin`, { email, password })
      .then((res) => {
        // console.log(res.data.body);
        localStorage.setItem("accessToken", res.data.body.accessToken);
        // 로그인 상태 변경
        setLogin(true);
        // Role 불러와서 store에 저장
        saveUserInfo();
        // Home으로 이동
        navigate("/");
        console.log("로그인 완료");
      })
      .catch((err) => {
        console.log(err);
      });
    return <></>;
  };

  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [showEmailError, setShowEmailError] = useState(false);
  const [showPasswordError, setShowPasswordError] = useState(false);

  const emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
  const passwordPattern =
    /^(?=.*[A-Za-z])(?=.*\d)(?=.*[!@#$%^&*])[A-Za-z\d!@#$%^&*]{8,}$/;

  // 유효성 검사
  const validateEmail = (value: string) =>
    emailPattern.test(value) && value.trim() !== "";
  const validatePassword = (value: string) => passwordPattern.test(value);

  // 이메일 이벤트 핸들러
  const handleEmailChange = (e: ChangeEvent<HTMLInputElement>) => {
    const value = e.target.value;
    setEmail(value);
    setShowEmailError(!validateEmail(value));
  };
  const handleEmailBlur = (e: FocusEvent<HTMLInputElement>) => {
    setShowEmailError(!validateEmail(e.target.value));
  };

  // 비밀번호 이벤트 핸들러
  const handlePasswordChange = (e: ChangeEvent<HTMLInputElement>) => {
    const value = e.target.value;
    setPassword(value);
    setShowPasswordError(!validatePassword(value));
  };
  const handlePasswordBlur = (e: FocusEvent<HTMLInputElement>) => {
    setShowPasswordError(!validatePassword(e.target.value));
  };

  // 로그인 버튼 활성화
  const isFormValid: boolean =
    !showEmailError && !showPasswordError && email !== "" && password !== "";

  return (
    <div className="w-[500px] flex flex-col bg-block rounded-[30px] p-[50px] gap-[40px] text-white">
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
            onBlur={handleEmailBlur}
            aria-invalid={showEmailError}
            aria-describedby={showEmailError ? "emailError" : undefined}
            className="h-[56px] w-full px-5 text-gray-500 rounded-[5px]"
            placeholder={t("account.email")}
            required
          />
          {showEmailError && (
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
        onClick={() => (isFormValid ? login(email, password) : null)}
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
