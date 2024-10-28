import { Link } from "react-router-dom";

import { useTranslation } from "react-i18next";

type Props = {
  handleTheme: () => void;
};

export const Header = ({ handleTheme }: Props) => {
  const { t, i18n } = useTranslation();
  const handleLanguage = (language: string) => {
    i18n.changeLanguage(language);
  };
  return (
    <div className="text-black dark:text-white">
      <Link to="/">Home</Link>
      <Link to="/about-us/service">{t("header.subMenu.aboutUs")}</Link>
      <Link to="/create-model">{t("header.subMenu.createModel")}</Link>
      <Link to="/model-list">{t("header.subMenu.modelList")}</Link>
      <Link to="/contact">{t("header.subMenu.contact")}</Link>

      <button onClick={() => handleLanguage("ko")}>Kor</button>
      <button onClick={() => handleLanguage("en")}>Eng</button>
      <button onClick={() => handleTheme()}>Dark/Light</button>
      <Link to="/account/login">{t("header.login")}</Link>
    </div>
  );
};
