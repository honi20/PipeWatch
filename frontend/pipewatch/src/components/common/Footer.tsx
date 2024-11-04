import { NavLink, Link } from "react-router-dom";
import { useState } from "react";

import { ManageCookies } from "@components/common/ManageCookies";

import { useTranslation } from "react-i18next";
import LogoHeaderDark from "@assets/images/logo_header_dark.png";
import LogoHeaderLight from "@assets/images/logo_header_light.png";

type Props = {
  currentTheme: string;
};

export const Footer = ({ currentTheme }: Props) => {
  const [isOpen, setIsOpen] = useState(false);

  const { t } = useTranslation();

  const isDark = currentTheme === "dark";

  return (
    <footer className="min-w-[850px] bottom-0 left-0 right-0 flex items-center justify-between p-2 text-gray-800 bg-white dark:bg-black dark:text-gray-500 z-10">
      <ManageCookies isOpen={isOpen} onClose={() => setIsOpen(false)} />
      <div className="flex items-center gap-4">
        <Link className="p-2 hover:text-primary-200 " to="/">
          <img
            className="w-16 h-10"
            src={isDark ? LogoHeaderDark : LogoHeaderLight}
          />
        </Link>

        <div className="p-3 pb-2"> {t("footer.paoriTeam")}</div>

        <NavLink
          className={({ isActive }) =>
            `p-3 pb-2 hover:text-primary-200  ${
              isActive
                ? "text-primary-200 border-solid border-b-2 border-b-primary-200"
                : ""
            }`
          }
          to="/terms-and-policy"
        >
          {t("footer.termsAndPolicy")}
        </NavLink>

        <NavLink
          className={({ isActive }) =>
            `p-3 pb-2 hover:text-primary-200 ${
              isActive
                ? "text-primary-200 border-solid border-b-2 border-b-primary-200"
                : ""
            }`
          }
          to="/contact"
        >
          {t("footer.contact")}
        </NavLink>

        <button
          onClick={() => setIsOpen(true)}
          className="p-3 pb-2 bg-transparent hover:text-primary-200"
        >
          {t("footer.manageCookies.title")}
        </button>
      </div>
      <div className="flex items-center gap-4">
        {t("home.greeting1")} {t("home.greeting2")}
      </div>
    </footer>
  );
};
