import { useEffect, useState } from "react";

import { Outlet, useLocation } from "react-router-dom";

import { Header } from "@components/common/Header";
import { Footer } from "@components/common/Footer";

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
    if (
      localStorage.theme === "dark" ||
      (!("theme" in localStorage) &&
        window.matchMedia("(prefers-color-scheme: dark)").matches)
    ) {
      setTheme("dark");
      document.documentElement.classList.add("dark");
    } else {
      setTheme("light");
      document.documentElement.classList.remove("dark");
    }
  }, [theme]);

  return (
    <div className="min-w-[850px] w-screen  text-black bg-white dark:bg-black dark:text-white">
      {isAuth ? null : (
        <Header handleTheme={handleTheme} currentTheme={localStorage.theme} />
      )}
      <div className="py-[108px] min-h-full">
        <Outlet />
      </div>
      <Footer currentTheme={localStorage.theme} />
    </div>
  );
}

export default Layout;
