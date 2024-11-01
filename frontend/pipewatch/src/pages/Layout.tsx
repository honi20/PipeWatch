import { useEffect, useState } from "react";

import { Outlet, useLocation } from "react-router-dom";

import { Header } from "@components/common/Header";

function Layout() {
  const [theme, setTheme] = useState("light");
  const handleTheme = () => {
    setTheme(
      localStorage.theme === "dark"
        ? (localStorage.theme = "light")
        : (localStorage.theme = "dark")
    );
  };

  const location = useLocation();
  // console.log("first location: ", location);
  const isAuth = location.pathname.includes("/account/auth");
  // console.log("isAuth: ", isAuth);

  useEffect(() => {
    if (
      localStorage.theme === "dark" ||
      (!("theme" in localStorage) &&
        window.matchMedia("(prefers-color-scheme: dark)").matches)
    ) {
      localStorage.setItem("theme", "dark");
      document.documentElement.classList.add("dark");
    } else {
      localStorage.setItem("theme", "light");
      document.documentElement.classList.remove("dark");
    }
  }, [theme]);

  return (
    <div className="min-w-[850px] w-screen overflow-auto text-black bg-white dark:bg-black dark:text-white">
      {isAuth ? null : (
        <Header handleTheme={handleTheme} currentTheme={localStorage.theme} />
      )}
      <div className="py-[108px] min-h-full">
        <Outlet />
      </div>
    </div>
  );
}

export default Layout;
