import { useState, ChangeEvent, FocusEvent } from "react";
import { useTranslation, Trans } from "react-i18next";
import { Link } from "react-router-dom";

import { Input, Button } from "@headlessui/react";

import CheckIcon from "@mui/icons-material/Check";
import CloseIcon from "@mui/icons-material/Close";

const SignUpCard = () => {
  const { t } = useTranslation();

  const tempEmailVeriCode = "123456";
  const [emailVeriCode, setEmailVeriCode] = useState("");
  const [isEmailVerified, setIsEmailVerified] = useState(false);
  const [EmailVeriError, setEmailVeriError] = useState(false);

  return (
    <div className="w-[500px] flex flex-col bg-block rounded-[30px] p-[50px] gap-[40px] text-white">
      {/* header */}
      <div className="flex justify-center font-semibold text-[28px]">
        {t("account.signUp")}
      </div>

      {/* content */}
      <div className="flex flex-col gap-[20px]">
        {/* email */}
        <div className="flex justify-between">
          <Input
            type="email"
            className="h-[56px] px-5 text-gray-500 rounded-[5px]"
            placeholder={t("account.email")}
            required
          />
          <Button
            className={`h-[56px] w-[120px] px-6 py-2 flex items-center justify-center text-white rounded-[20px] bg-primary-500 hover:bg-primary-500/80`}
          >
            {t("account.requestVerification")}
          </Button>
        </div>
        <div className="flex items-center justify-between">
          <Input
            type="email"
            value={emailVeriCode}
            className={`h-[56px] px-5 text-gray-500 rounded-[5px] ${
              isEmailVerified ? "bg-gray-800" : "bg-white"
            }`}
            onChange={(e: ChangeEvent<HTMLInputElement>) =>
              setEmailVeriCode(e.target.value)
            }
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
              className={`h-[56px] w-[120px] px-6 py-2 flex items-center justify-center text-white rounded-[20px] bg-black hover:bg-black/80`}
              onClick={() => {
                if (emailVeriCode === tempEmailVeriCode) {
                  setIsEmailVerified(true);
                  setEmailVeriError(false);
                } else {
                  setIsEmailVerified(false);
                  setEmailVeriError(true);
                }
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
          className="h-[56px] w-full px-5 text-gray-500 rounded-[5px]"
          placeholder={t("account.password")}
        />
        {/* confirm password */}
        <Input
          type="password"
          className="h-[56px] w-full px-5 text-gray-500 rounded-[5px]"
          placeholder={t("account.confirmPassword")}
        />
        {/* name */}
        <Input
          type="text"
          className="h-[56px] w-full px-5 text-gray-500 rounded-[5px]"
          placeholder={t("account.name")}
        />
        {/* company */}
        <Input
          type="text"
          className="h-[56px] w-full px-5 text-gray-500 rounded-[5px]"
          placeholder={t("account.companyName")}
        />
        {/* employee */}
        <div className="flex items-start justify-between w-full">
          <div>{t("account.employeeInformation")}</div>
          <div className="flex flex-col w-2/3 gap-5">
            <Input
              type="text"
              className="h-[56px] w-full px-5 text-gray-500 rounded-[5px]"
              placeholder={t("account.employeeId")}
            />
            <Input
              type="text"
              className="h-[56px] w-full px-5 text-gray-500 rounded-[5px]"
              placeholder={t("account.employeeDepartment")}
            />
            <Input
              type="text"
              className="h-[56px] w-full px-5 text-gray-500 rounded-[5px]"
              placeholder={t("account.employeePosition")}
            />
          </div>
        </div>
      </div>

      {/* button */}
      <Button
        className={`h-[56px] w-full text-white rounded-lg bg-button-background`}
      >
        {t("account.signUp")}
      </Button>

      {/* footer */}
      <div className="text-center whitespace-normal">
        <Trans i18nKey="account.termsAndPrivacyConsent">
          회원가입 시, 귀하는 당사의
          <Link to="." className="underline">
            서비스 약관
          </Link>
          과
          <Link to="." className="underline">
            개인정보 처리방침
          </Link>
          에 동의하는 것으로 간주됩니다.
        </Trans>
      </div>
    </div>
  );
};

export default SignUpCard;
