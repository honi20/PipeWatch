import { ChangeEvent, useCallback, useState } from "react";
import { Trans, useTranslation } from "react-i18next";
import { Link, useNavigate } from "react-router-dom";

import { Button, Input } from "@headlessui/react";

import { CompanyListBox } from "@components/account/SignUp/CompanyListbox";
import { CompanyType } from "@src/components/account/SignUp/inputType";

import CheckIcon from "@mui/icons-material/Check";
import CloseIcon from "@mui/icons-material/Close";
import { baseInstance } from "@src/stores/apiClient";
import { BeatLoader } from "react-spinners";

interface FormState {
  email: string;
  password: string;
  name: string;
  enterpriseId: number;
  empNo: number;
  department: string;
  empClass: string;
  verifyCode: string;
}

const SignUpCard = () => {
  const [emailVeriCode, setEmailVeriCode] = useState("");
  const [isEmailVerified, setIsEmailVerified] = useState(false);
  const [EmailVeriError, setEmailVeriError] = useState(false);

  const [showEmailError, setEmailError] = useState(false);
  const [showPasswordError, setPasswordError] = useState(false);
  const [showConfirmPasswordError, setConfirmPasswordError] = useState(false);
  const [passwordMatchError, setPasswordMatchError] = useState(false);

  const [emailErrorText, setEmailErrorText] = useState<string>("");

  const [isEmailChecked, setIsEmailChecked] = useState<boolean>(false);

  const { t } = useTranslation();
  const navigate = useNavigate();

  const initialFormState: FormState = {
    email: "",
    password: "",
    name: "",
    enterpriseId: 0,
    empNo: 0,
    department: "",
    empClass: "",
    verifyCode: "",
  };

  const verifyEmail = useCallback(
    (email: string) => {
      setIsEmailChecked(true);
      setEmailError(false);

      baseInstance
        .post("/api/auth/email-code/send", email)
        .then((res) => {
          console.log(res.data.body);
        })
        .catch((err) => {
          const status = err.status;

          if (status === 403) {
            setEmailErrorText(t("account.emailError"));
          } else if (status === 409) {
            setEmailErrorText(t("account.duplicateEmail"));
          }
          console.log(err);
          setIsEmailChecked(false);
          setEmailError(true);
        });
    },
    [t]
  );

  const [formState, setFormState] = useState(initialFormState);

  // 이메일 유효성 검사
  const emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
  const validateEmail = (value: string) => emailPattern.test(value);

  // 비밀번호 유효성 검사
  const passwordPattern =
    /^(?=.*[A-Za-z])(?=.*\d)(?=.*[!@#$%^&*])[A-Za-z\d!@#$%^&*]{8,}$/;
  const validatePassword = (value: string) => passwordPattern.test(value);

  const handleInputChange = (e: ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;

    const transformedValue = name === "empNo" ? Number(value) : value;

    setFormState((prevFormState) => ({
      ...prevFormState,
      [name]: transformedValue,
    }));

    if (name === "email") {
      setEmailError(value !== "" && !validateEmail(value));
      setEmailErrorText(t("account.emailError"));
    }
    if (name === "password") {
      setPasswordError(value !== "" && !validatePassword(value));
    }
    if (name === "confirmPassword") {
      setConfirmPasswordError(value !== "" && !validatePassword(value));
      setPasswordMatchError(value !== "" && value !== formState.password);
    }
    if (name === "verifyCode") {
      setEmailVeriCode(e.target.value);
    }
  };

  const verifyEmailCode = (email: string, verifyCode: string) => {
    baseInstance
      .post("/api/auth/email-code/verify", { email, verifyCode })
      .then((res) => {
        console.log(res.data.body);
        setIsEmailVerified(true);
        setEmailVeriError(false);
      })
      .catch((err) => {
        console.log(err);
        setIsEmailVerified(false);
        setEmailVeriError(true);
      });
    return <></>;
  };

  const handleCompanyChange = (selectedCompany: CompanyType) => {
    setFormState((prevFormState) => ({
      ...prevFormState,
      enterpriseId: selectedCompany.enterpriseId,
    }));
  };

  const isFormValid: boolean =
    emailVeriCode !== "" &&
    !showEmailError &&
    !showPasswordError &&
    !showConfirmPasswordError &&
    !passwordMatchError &&
    isEmailVerified &&
    Object.values(formState).every((value) => value !== "");

  const confirmSignUp = (formState: FormState) => {
    baseInstance
      .post("/api/auth", formState)
      .then((res) => {
        console.log("회원가입 성공: ", res.data.body);
        navigate("/account/auth/completed", {
          state: { email: formState.email },
        });
      })
      .catch((err) => {
        console.log(err);
      });
    return <></>;
  };

  console.log(formState);

  return (
    <div className="w-[500px] flex flex-col bg-block rounded-[30px] p-[50px] gap-[40px] text-white">
      {/* header */}
      <div className="flex justify-center font-semibold text-[28px]">
        {t("account.signUp")}
      </div>

      {/* content */}
      <div className="flex flex-col gap-[20px]">
        {/* email verification */}
        <div className="flex justify-between">
          <Input
            type="email"
            name="email"
            className={`h-[56px] px-5 text-gray-500 rounded-[5px] ${
              showEmailError && "border-solid border-2 border-warn"
            }`}
            placeholder={t("account.email")}
            required
            onChange={handleInputChange}
          />
          <Button
            className={`h-[56px] w-[120px] px-6 py-2 flex items-center justify-center text-white rounded-[20px] ${
              isEmailChecked
                ? "bg-gray-800 "
                : "bg-primary-500 hover:bg-primary-500/80"
            } `}
            onClick={() => !showEmailError && verifyEmail(formState.email)}
            disabled={isEmailChecked}
          >
            {isEmailChecked ? (
              <BeatLoader size={8} color="#FFFFFF" />
            ) : (
              t("account.requestVerification")
            )}
          </Button>
        </div>
        {showEmailError && (
          <span className="w-full px-2 whitespace-normal text-warn break-keep">
            {emailErrorText}
          </span>
        )}

        {/* email verification code */}
        <div className="flex items-center justify-between">
          <Input
            type="text"
            name="verifyCode"
            value={emailVeriCode}
            className={`h-[56px] px-5 text-gray-500 rounded-[5px] ${
              isEmailVerified ? "bg-gray-800" : "bg-white"
            } ${EmailVeriError && "border-solid border-2 border-warn"}`}
            onChange={handleInputChange}
            placeholder={t("account.verificationCode")}
            required
            disabled={isEmailVerified}
          />
          {isEmailVerified ? (
            <div className="text-success flex gap-[4px] px-[20px] ">
              <CheckIcon sx={{ fontSize: "20px" }} />
              {t("account.verificationComplete")}
            </div>
          ) : (
            <Button
              className={`h-[56px] w-[120px] px-6 py-2 flex items-center justify-center text-white rounded-[20px] ${
                emailVeriCode !== ""
                  ? "bg-black hover:bg-black/80"
                  : "bg-gray-800"
              } `}
              onClick={() => {
                verifyEmailCode(formState.email, formState.verifyCode);
              }}
            >
              {t("account.confirm")}
            </Button>
          )}
        </div>
        {EmailVeriError && (
          <div className="flex justify-center items-center text-warn gap-[4px]">
            <CloseIcon sx={{ fontSize: "20px" }} />
            {t("account.verificationFailed")}
          </div>
        )}

        {/* password */}
        <Input
          type="password"
          name="password"
          className={`h-[56px]  w-full  px-5 text-gray-500 rounded-[5px] ${
            showPasswordError && "border-solid border-2 border-warn"
          }`}
          placeholder={t("account.password")}
          onChange={handleInputChange}
        />
        {showPasswordError && (
          <span className="w-full px-2 whitespace-normal text-warn break-keep">
            {t("account.passwordError")}
          </span>
        )}

        {/* confirm password */}
        <Input
          type="password"
          name="confirmPassword"
          className={`h-[56px]  w-full  px-5 text-gray-500 rounded-[5px] ${
            showConfirmPasswordError && "border-solid border-2 border-warn"
          }`}
          placeholder={t("account.confirmPassword")}
          onChange={(e: ChangeEvent<HTMLInputElement>) => {
            const value = e.target.value;
            setConfirmPasswordError(value !== "" && !validatePassword(value));
            setPasswordMatchError(value !== "" && value !== formState.password);
          }}
        />
        {showConfirmPasswordError ? (
          <span className="w-full px-2 whitespace-normal text-warn break-keep">
            {t("account.passwordError")}
          </span>
        ) : (
          passwordMatchError && (
            <span className="w-full px-2 whitespace-normal text-warn break-keep">
              {t("account.passwordMismatchError")}
            </span>
          )
        )}

        {/* name */}
        <Input
          type="text"
          name="name"
          className="h-[56px] w-full px-5 text-gray-500 rounded-[5px]"
          placeholder={t("account.name")}
          onChange={handleInputChange}
        />

        {/* company name */}
        <div className="flex items-center justify-between w-full h-[56px]">
          <p className=""> {t("account.companyName")}</p>
          <div className="flex flex-col w-2/3 h-full gap-5">
            <CompanyListBox onCompanyChange={handleCompanyChange} />
          </div>
        </div>

        {/* employee */}
        <div className="flex items-start justify-between w-full">
          <div>{t("account.employeeInformation")}</div>
          <div className="flex flex-col w-2/3 gap-5">
            <Input
              type="text"
              name="empNo"
              className="h-[56px] w-full px-5 text-gray-500 rounded-[5px]"
              placeholder={t("account.employeeId")}
              onChange={handleInputChange}
            />
            <Input
              type="text"
              name="department"
              className="h-[56px] w-full px-5 text-gray-500 rounded-[5px]"
              placeholder={t("account.employeeDepartment")}
              onChange={handleInputChange}
            />
            <Input
              type="text"
              name="empClass"
              className="h-[56px] w-full px-5 text-gray-500 rounded-[5px]"
              placeholder={t("account.employeePosition")}
              onChange={handleInputChange}
            />
          </div>
        </div>
      </div>

      {/* confirm button */}
      <Button
        className={`h-[56px] w-full text-white rounded-lg ${
          isFormValid ? "bg-button-background" : "bg-gray-800"
        }`}
        disabled={!isFormValid}
        onClick={() => {
          confirmSignUp(formState);
        }}
      >
        {t("account.signUp")}
      </Button>

      {/* footer */}
      <div className="text-center whitespace-normal">
        <Trans i18nKey="account.termsAndPrivacyConsent">
          회원가입 시, 귀하는 당사의
          <Link to="/terms-and-policy" className="underline">
            서비스 약관
          </Link>
          과
          <Link to="/terms-and-policy" className="underline">
            개인정보 처리방침
          </Link>
          에 동의하는 것으로 간주됩니다.
        </Trans>
      </div>
    </div>
  );
};

export default SignUpCard;
