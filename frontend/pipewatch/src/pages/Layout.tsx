import { useEffect, useState } from "react";

import { Outlet } from "react-router-dom";

import { Header } from "../components/common/Header";

function Layout() {
  const [theme, setTheme] = useState("light");
  const handleTheme = () => {
    setTheme(theme === "dark" ? "light" : "dark");
  };

  useEffect(() => {
    if (theme === "dark") {
      document.documentElement.classList.add("dark");
    } else {
      document.documentElement.classList.remove("dark");
    }
  }, [theme]);

  return (
    <div className="bg-white dark:bg-black">
      <Header handleTheme={handleTheme} />
      <Outlet />
    </div>
  );
}

export default Layout;
