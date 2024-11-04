import { NavLink, Link } from "react-router-dom";
import { useState } from "react";

import {
  Description,
  Dialog,
  DialogPanel,
  DialogTitle,
} from "@headlessui/react";

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
      <Dialog
        open={isOpen}
        onClose={() => setIsOpen(false)}
        className="relative z-50"
      >
        <div className="fixed inset-0 flex items-center justify-center w-screen p-4">
          <DialogPanel className="max-w-lg p-12 space-y-4 bg-white border">
            <DialogTitle className="font-bold">Manage Cookies</DialogTitle>
            <Description>
              We use our own cookies so that we can show you this website and
              understand how you use them to improve the services we offer.
            </Description>
            <div className="flex justify-center gap-4">
              <button onClick={() => setIsOpen(false)}>Accept All</button>
              <button onClick={() => setIsOpen(false)}>Deny All</button>
            </div>
          </DialogPanel>
        </div>
      </Dialog>
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
          {t("footer.manageCookies")}
        </button>
      </div>
      <div className="flex items-center gap-4">
        {t("home.greeting1")} {t("home.greeting2")}
      </div>
    </footer>
  );
};
