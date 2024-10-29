import { useTranslation, Trans } from "react-i18next";
import { Input, Button } from "@headlessui/react";
import { Link } from "react-router-dom";

const SignUpCard = () => {
  const { t } = useTranslation();
  return (
    <div className="w-[500px] flex flex-col bg-block rounded-[30px] p-[50px] gap-[40px]">
      {/* header */}
      <div className="flex justify-center font-semibold text-[28px]">
        {t("account.signUp")}
      </div>

      {/* content */}
      <div className="flex flex-col gap-[20px]">
        {/* email */}
        <Input
          type="email"
          className="h-[56px] w-full px-5 text-gray-500 rounded-[5px]"
          placeholder={t("account.email")}
          required
        />
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
