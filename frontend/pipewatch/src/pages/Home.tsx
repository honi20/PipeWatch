import { useTranslation } from "react-i18next";
import { Link } from "react-router-dom";
import { Button } from "@headlessui/react";

import HomePipe from "@assets/images/home_pipe.png";

export const Home = () => {
  const { t } = useTranslation();

  return (
    <div className="flex flex-col items-center justify-center">
      <h1 className="font-bold text-[80px]">{t("home.greeting1")}</h1>
      <h1 className="font-bold text-[80px]">{t("home.greeting2")}</h1>
      <div className="flex gap-2 my-6">
        <Link className="" to="/account/auth/login">
          <Button className="px-6 py-3 border-[1px] bg-white dark:bg-black border-black border-solid rounded-lg text-s dark:border-white hover:text-primary-200">
            {t("header.login")}
          </Button>
        </Link>
        <Link className="" to="/account/auth/sign-up">
          <Button className="px-6 py-3 rounded-lg bg-primary-200 b text-s dark:border-white hover:text-primary-500">
            {t("home.signUp")}
          </Button>
        </Link>
      </div>
      <img src={HomePipe} width={"1000px"} />
    </div>
  );
};
