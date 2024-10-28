import "./App.css";
import { useEffect, useState } from "react";

import { useTranslation } from "react-i18next";

function App() {
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

  const { t, i18n } = useTranslation();
  const handleLanguage = (language: string) => {
    i18n.changeLanguage(language);
  };

  return (
    <div className="bg-white dark:bg-black w-full h-screen">
      <h1 className="font-bold underline text-primary-500 dark:text-primary-200">
        Starting Pipe Watch Project
      </h1>
      <button onClick={handleTheme} className="bg-gray-800 dark:bg-gray-500">
        Dark/Light
      </button>
      <button onClick={() => handleLanguage("ko")}>Kor</button>
      <button onClick={() => handleLanguage("en")}>Eng</button>
      <h1>{t("greeting1")}</h1>
      <h1>{t("greeting2")}</h1>
    </div>
  );
}

export default App;
