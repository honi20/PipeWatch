import { useEffect, useState } from "react";

import { Outlet, useLocation } from "react-router-dom";

import { Header } from "@components/common/Header";
import { HeaderAccount } from "@components/common/HeaderAccount";
import { Footer } from "@components/common/Footer";

import ArrowUpwardIcon from "@mui/icons-material/ArrowUpward";

function Layout() {
  const [theme, setTheme] = useState(localStorage.getItem("theme") || "dark");
  const handleTheme = () => {
    const newTheme = theme === "dark" ? "light" : "dark";
    setTheme(newTheme);
    localStorage.setItem("theme", newTheme);
  };

  const location = useLocation();
  const isAuth = location.pathname.includes("/account/auth");

  useEffect(() => {
    if (!localStorage.getItem("theme")) {
      localStorage.setItem("theme", "dark");
      setTheme("dark");
    }

    if (theme === "dark") {
      document.documentElement.classList.add("dark");
    } else {
      document.documentElement.classList.remove("dark");
    }
  }, [theme]);

  return (
    <div className="min-w-[850px] w-screen  text-black bg-white dark:bg-black dark:text-white">
      {isAuth ? (
        <HeaderAccount currentTheme={localStorage.theme} />
      ) : (
        <Header handleTheme={handleTheme} currentTheme={localStorage.theme} />
      )}
      <div className="py-[108px] min-h-full dark:bg-black bg-white">
        <Outlet />
      </div>
      <Footer currentTheme={localStorage.theme} />
      <button
        onClick={() => window.scrollTo(0, 0)}
        className="fixed bottom-[30px] right-[30px] dark:bg-black border-solid border-2 border-white rounded-[30px] py-[10px] px-[16px]"
      >
        <div className="flex flex-col items-center justify-center">
          <ArrowUpwardIcon />
          <p className="">Top</p>
        </div>
      </button>
    </div>
  );
}

export default Layout;
