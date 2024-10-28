import { Link } from "react-router-dom";

import { useTranslation } from "react-i18next";
import LogoHeaderDark from "../../assets/images/logo_header_dark.png";
import LogoHeaderLight from "../../assets/images/logo_header_light.png";

import DarkModeIcon from "@mui/icons-material/DarkMode";
import LightModeIcon from "@mui/icons-material/LightMode";
import LanguageIcon from "@mui/icons-material/Language";

type Props = {
  handleTheme: () => void;
  currentTheme: string;
};

export const Header = ({ handleTheme, currentTheme }: Props) => {
  const { t, i18n } = useTranslation();
  const handleLanguage = (language: string) => {
    i18n.changeLanguage(language);
  };

  const isKorean = i18n.language === "ko";
  const toggleLanguage = isKorean ? "en" : "ko";
  const buttonText = isKorean ? "Eng" : "Kor";

  const isDark = currentTheme === "dark";

  return (
    <div className="flex items-center justify-between text-black dark:text-white">
      <div className="flex ">
        {isDark ? (
          <img src={LogoHeaderDark} width="100px" />
        ) : (
          <img src={LogoHeaderLight} width="100px" />
        )}
        <Link className="p-5" to="/">
          Home
        </Link>
        <Link className="p-5" to="/about-us/service">
          {t("header.subMenu.aboutUs")}
        </Link>
        <Link className="p-5" to="/create-model">
          {t("header.subMenu.createModel")}
        </Link>
        <Link className="p-5" to="/model-list">
          {t("header.subMenu.modelList")}
        </Link>
        <Link className="p-5" to="/contact">
          {t("header.subMenu.contact")}
        </Link>
      </div>

      <div className="gap-3 flex items-center">
        <div
          className="flex gap-2"
          onClick={() => handleLanguage(toggleLanguage)}
        >
          <LanguageIcon />
          <div className="w-8">{buttonText}</div>
        </div>

        <div onClick={() => handleTheme()}>
          {isDark ? <LightModeIcon /> : <DarkModeIcon />}
        </div>
        <Link to="/account/login">
          <button className="w-20 border-solid rounded-lg border-2 p-3 bg-white text-black border-black dark:border-white dark:bg-black dark:text-white">
            {t("header.login")}
          </button>
        </Link>
      </div>
    </div>
  );
};
