import { NavLink, Link, useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";

import {
  Button,
  Popover,
  PopoverButton,
  PopoverPanel,
} from "@headlessui/react";

import { useTranslation } from "react-i18next";
import LogoHeaderDark from "@assets/images/logo_header_dark.png";
import LogoHeaderLight from "@assets/images/logo_header_light.png";

import ExpandMoreIcon from "@mui/icons-material/ExpandMore";
import DarkModeIcon from "@mui/icons-material/DarkMode";
import LightModeIcon from "@mui/icons-material/LightMode";
import LanguageIcon from "@mui/icons-material/Language";

import { StatusBar } from "@components/common/StatusBar";

import { getApiClient } from "@src/stores/apiClient";
import { useUserStore } from "@src/stores/userStore";

type Props = {
  handleTheme: () => void;
  currentTheme: string;
};

// 로컬스토리지에 저장하기
const saveLanguageToStorage = (language: string) => {
  localStorage.setItem("language", language);
};

export const Header = ({ handleTheme, currentTheme }: Props) => {
  // User Box 관련 로직
  const navigate = useNavigate();

  const handleRoleNavigation = () => {
    switch (role) {
      case "ENTERPRISE":
        navigate("/enterprise/verification");
        break;
      case "EMPLOYEE":
      case "ADMIN":
        navigate("/account/manage");
        break;
      case "USER":
        break;
      default:
        console.log("Unknown role");
    }
  };

  // 로그인 상태 관리
  const {
    name,
    role,
    userState,
    isLogin,
    setName,
    setLogin,
    setRole,
    setUserState,
  } = useUserStore();
  console.log("login상태: ", isLogin);

  useEffect(() => {
    const isLoggedIn = !!localStorage.getItem("accessToken");
    console.log("Header: 로그인 상태 확인 ", isLoggedIn);
    setLogin(isLoggedIn);

    const role = localStorage.getItem("role") || "UNAUTHORIZED";
    setRole(role);
    const name = localStorage.getItem("name") || "";
    setName(name);
    const state = localStorage.getItem("userState") || "";
    setUserState(state);
    console.log("useEffect 실행");
  }, []);

  useEffect(() => {
    if (userState === "INACTIVE") {
      logout();
    }
  });

  // 로그아웃 함수
  const apiClient = getApiClient();
  const logout = async () => {
    try {
      const res = await apiClient.get("/api/auth/logout");
      console.log("로그아웃 API 호출: header.message", res.data.header.message);
    } catch (err) {
      console.error("로그아웃 API 호출 실패", err);
    } finally {
      // 로그아웃 처리
      localStorage.removeItem("accessToken");
      localStorage.removeItem("role");
      localStorage.removeItem("name");
      localStorage.removeItem("userState");
      localStorage.removeItem("enterpriseName");

      window.location.href = "/";
    }
  };

  const [waitingList, setWaitingList] = useState([]);

  const getWaitingList = async () => {
    try {
      const res = await apiClient.get(`/api/employees/waiting`);

      console.log("Waiting Employees Data: ", res.data.body.employees);
      setWaitingList(res.data.body.employees);
    } catch (err) {
      console.log(err);
    }
  };

  useEffect(() => {
    if (role === "ENTERPRISE") {
      getWaitingList();
    }
  }, []);

  const { t, i18n } = useTranslation();
  const handleLanguage = (language: string) => {
    saveLanguageToStorage(language);
    i18n.changeLanguage(language);
  };

  const isKorean = i18n.language === "ko";
  const currentLanguage = isKorean ? "한국어" : "English";

  const isDark = currentTheme === "dark";

  return (
    <header className="min-w-[850px] fixed top-0 left-0 right-0 flex flex-col items-center justify-between p-2 text-black bg-white dark:bg-black dark:text-white z-10">
      <div className="flex justify-between w-full">
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
            {t("header.subMenu.home")}
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
                    <ExpandMoreIcon
                      className={`size-5 transition-transform duration-200 ${
                        open ? "rotate-180" : ""
                      }`}
                    />
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
                        <p className="">{t("header.subMenu.aboutService")}</p>
                      </Link>
                      <Link
                        className="block px-3 py-2 transition rounded-lg hover:bg-white/5 hover:text-primary-200"
                        to="/about-us/team"
                        onClick={() => close()}
                      >
                        <p className="">{t("header.subMenu.aboutTeam")}</p>
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

          <NavLink
            className={({ isActive }) =>
              `p-3 pb-2 hover:text-primary-200 ${
                isActive
                  ? "text-primary-200 border-solid border-b-2 border-b-primary-200"
                  : ""
              }`
            }
            to="/enterprise"
          >
            임시 Enterprise
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
                    <ExpandMoreIcon
                      className={`size-5 transition-transform duration-200 ${
                        open ? "rotate-180" : ""
                      }`}
                    />
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

          {/* 계정 관련 status 변경 Box */}
          {isLogin ? (
            // 로그인 상태
            <div className="flex items-center gap-4 cursor-pointer">
              <div
                className="flex items-center justify-center gap-2 cursor-pointer"
                onClick={handleRoleNavigation}
              >
                <div>{name}</div>
                {role === "ENTERPRISE" ? (
                  // 기업 계정
                  <div className="flex items-center justify-center px-2 rounded-[30px] text-[14px] bg-warn">
                    {waitingList && waitingList.length}
                  </div>
                ) : (
                  // 사원 계정(관리자/일반)
                  <div
                    id="badge"
                    className="px-2 py-1 rounded-[30px] text-[12px] text-white bg-block dark:text-black dark:bg-white"
                  >
                    {role === "ADMIN" ? "관리자" : "사원"}
                  </div>
                )}
              </div>

              <Button
                className="px-4 py-2 bg-white border-[1px] border-black border-solid rounded-lg text-s dark:bg-black dark:border-white hover:text-primary-200"
                onClick={() => {
                  logout();
                }}
              >
                로그아웃
                {/* {t("header.login")} */}
              </Button>
            </div>
          ) : (
            // 로그인 필요
            <Link className="" to="/account/auth/login">
              <Button className="px-4 py-2 bg-white border-[1px] border-black border-solid rounded-lg text-s dark:bg-black dark:border-white hover:text-primary-200">
                {t("header.login")}
              </Button>
            </Link>
          )}
        </div>
      </div>
      {userState === "PENDING" && (
        <StatusBar
          // text={t("pipeGenerator.takePhoto.connectRCCar.statusMessages.failed")}
          text={
            "등록된 기업이 없습니다. 기업 인증 상태를 확인해주세요. 내 기업 보기"
          }
          icon={""}
          // icon={<CancelIcon sx={{ fontSize: "20px" }} />}
          color={"bg-warn"}
        />
      )}
    </header>
  );
};
