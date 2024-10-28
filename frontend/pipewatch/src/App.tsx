import "./App.css";
import { useEffect, useState } from "react";

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

  return (
    <div className="bg-white dark:bg-black w-full h-screen">
      <h1 className="font-bold underline text-primary-500 dark:text-primary-200">
        Starting Pipe Watch Project
      </h1>
      <button onClick={handleTheme} className="bg-gray-800 dark:bg-gray-500">
        Switch
      </button>
    </div>
  );
}

export default App;
