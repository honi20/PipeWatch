import { NavLink, Link } from "react-router-dom";
import { ChevronDownIcon } from "@heroicons/react/20/solid";

import {
  Button,
  Popover,
  PopoverButton,
  PopoverPanel,
} from "@headlessui/react";

import { useTranslation } from "react-i18next";
import LogoHeaderDark from "@assets/images/logo_header_dark.png";
import LogoHeaderLight from "@assets/images/logo_header_light.png";

import DarkModeIcon from "@mui/icons-material/DarkMode";
import LightModeIcon from "@mui/icons-material/LightMode";
import LanguageIcon from "@mui/icons-material/Language";

type Props = {
  handleTheme: () => void;
  currentTheme: string;
};

// 로컬스토리지에 저장하기
const saveLanguageToStorage = (language: string) => {
  localStorage.setItem("language", language);
};

export const Header = ({ handleTheme, currentTheme }: Props) => {
  const { t, i18n } = useTranslation();
  const handleLanguage = (language: string) => {
    saveLanguageToStorage(language);
    i18n.changeLanguage(language);
  };

  const isKorean = i18n.language === "ko";
  // const toggleLanguage = isKorean ? "en" : "ko";
  const currentLanguage = isKorean ? "한국어" : "English";

  const isDark = currentTheme === "dark";

  return (
    <header className="min-w-[850px] fixed top-0 left-0 right-0 flex items-center justify-between p-2 text-black bg-white dark:bg-black dark:text-white z-10">
      <div className="flex items-center gap-4">
        <Link className="p-2 hover:text-primary-200 " to="/">
          <img
            className="w-16 h-10"
            src={isDark ? LogoHeaderDark : LogoHeaderLight}
          />
        </Link>

        <NavLink
          className={({ isActive }) =>
            `p-3 pb-2 hover:text-primary-200 ${
              isActive
                ? "text-primary-200 border-solid border-b-2 border-b-primary-200"
                : ""
            }`
          }
          to="/"
        >
          Home
        </NavLink>

        <Popover className="p-3 pb-2 group">
          {({ open, close }) => (
            <>
              <NavLink
                className={({ isActive }) =>
                  `p-3  hover:text-primary-200 ${
                    isActive ? "text-primary-200 " : ""
                  }`
                }
                to="/about-us"
              >
                <PopoverButton className="flex gap-1 items-center bg-white dark:bg-black focus:outline-none data-[active]:text-primary-200  data-[hover]:text-primary-200 data-[focus]:outline-1 data-[focus]:outline-white">
                  {t("header.subMenu.aboutUs")}
                  <ChevronDownIcon className="size-5 group-data-[open]:rotate-180" />
                </PopoverButton>
              </NavLink>
              {open && (
                <PopoverPanel
                  transition
                  anchor="bottom"
                  className="my-3 divide-y divide-white/5 rounded-xl bg-block dark:bg-block text-sm/6 transition duration-200 ease-in-out [--anchor-gap:var(--spacing-5)] data-[closed]:-translate-y-1 data-[closed]:opacity-0 z-10"
                >
                  <div className="p-3 text-white dark:text-white">
                    <Link
                      className="block px-3 py-2 transition rounded-lg hover:bg-white/5 hover:text-primary-200"
                      to="/about-us/service"
                      onClick={() => close()}
                    >
                      <p className=""> {t("header.subMenu.aboutService")}</p>
                    </Link>
                    <Link
                      className="block px-3 py-2 transition rounded-lg hover:bg-white/5 hover:text-primary-200"
                      to="/about-us/team"
                      onClick={() => close()}
                    >
                      <p className=""> {t("header.subMenu.aboutTeam")}</p>
                    </Link>
                  </div>
                </PopoverPanel>
              )}
            </>
          )}
        </Popover>

        <NavLink
          className={({ isActive }) =>
            `p-3 pb-2 hover:text-primary-200  ${
              isActive
                ? "text-primary-200 border-solid border-b-2 border-b-primary-200"
                : ""
            }`
          }
          to="/pipe-generator"
        >
          {t("header.subMenu.pipeGenerator")}
        </NavLink>

        <NavLink
          className={({ isActive }) =>
            `p-3 pb-2 hover:text-primary-200 ${
              isActive
                ? "text-primary-200 border-solid border-b-2 border-b-primary-200"
                : ""
            }`
          }
          to="/pipe-viewer"
        >
          {t("header.subMenu.pipeViewer")}
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
          {t("header.subMenu.contact")}
        </NavLink>
      </div>
      <div className="flex items-center gap-4">
        <Popover className="group">
          {({ open, close }) => (
            <>
              <div className="hover:text-primary-200">
                <PopoverButton className="flex gap-2 items-center bg-white dark:bg-black focus:outline-none data-[active]:text-primary-200  data-[hover]:text-primary-200 data-[focus]:outline-1 data-[focus]:outline-white">
                  <LanguageIcon />
                  <div className="">{currentLanguage}</div>
                  <ChevronDownIcon className="size-5 group-data-[open]:rotate-180" />
                </PopoverButton>
              </div>
              {open && (
                <PopoverPanel
                  transition
                  anchor="bottom"
                  className="my-3 divide-y divide-white/5 rounded-xl bg-block dark:bg-block text-sm/6 transition duration-200 ease-in-out [--anchor-gap:var(--spacing-5)] data-[closed]:-translate-y-1 data-[closed]:opacity-0 z-10"
                >
                  <div className="p-3 text-white dark:text-white">
                    <div
                      className="block px-3 py-2 transition rounded-lg hover:bg-white/5 hover:text-primary-200"
                      onClick={() => {
                        handleLanguage("ko");
                        close();
                      }}
                    >
                      <p className="">한국어</p>
                    </div>
                    <div
                      className="block px-3 py-2 transition rounded-lg hover:bg-white/5 hover:text-primary-200"
                      onClick={() => {
                        handleLanguage("en");
                        close();
                      }}
                    >
                      <p className="">English</p>
                    </div>
                  </div>
                </PopoverPanel>
              )}
            </>
          )}
        </Popover>

        <div
          className="cursor-pointer hover:text-primary-200"
          onClick={() => handleTheme()}
        >
          {isDark ? <LightModeIcon /> : <DarkModeIcon />}
        </div>
        <Link className="" to="/account/auth/login">
          <Button className="px-4 py-2 bg-white border-[1px] border-black border-solid rounded-lg text-s dark:bg-black dark:border-white hover:text-primary-200">
            {t("header.login")}
          </Button>
        </Link>
      </div>
    </header>
  );
};
