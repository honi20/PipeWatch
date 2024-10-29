import { useEffect, useState } from "react";

import { Outlet } from "react-router-dom";

import { Header } from "../components/common/Header";

function Layout() {
  const [theme, setTheme] = useState("light");
  const handleTheme = () => {
    setTheme(
      localStorage.theme === "dark"
        ? (localStorage.theme = "light")
        : (localStorage.theme = "dark")
    );
  };

  useEffect(() => {
    // if (theme === "dark") {
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
    <div className="h-full text-black bg-white dark:bg-black dark:text-white ">
      <Header handleTheme={handleTheme} currentTheme={localStorage.theme} />
      <Outlet />
    </div>
  );
}

export default Layout;
