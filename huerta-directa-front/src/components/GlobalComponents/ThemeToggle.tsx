import { useEffect, useState } from "react";
import { faSun, faMoon } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";

export const ThemeToggle = () => {
  const [darkMode, setDarkMode] = useState(() =>
    document.documentElement.classList.contains("dark")
  );

  useEffect(() => {
    const root = document.documentElement;
    if (darkMode) {
      root.classList.add("dark");
      localStorage.setItem("theme", "dark");
    } else {
      root.classList.remove("dark");
      localStorage.setItem("theme", "light");
    }
  }, [darkMode]);

  return (
    <div className="flex items-center justify-between">
      <span className="text-sm font-medium text-gray-700 dark:text-gray-300">
        Tema
      </span>
      <button
        onClick={() => setDarkMode(!darkMode)}
        
        className={`relative w-14 h-7 flex items-center rounded-full p-1 transition-all duration-300
          ${darkMode ? "bg-[#20571b]" : "bg-gray-300"}`}
      >
        <div className={`w-5 h-5 bg-white rounded-full shadow-md transform transition-all duration-300 flex items-center justify-center
          ${darkMode ? "translate-x-7" : "translate-x-0"}`}>
          <FontAwesomeIcon
            icon={darkMode ? faMoon : faSun}
            className={`text-[10px] ${darkMode ? "text-indigo-500" : "text-yellow-500"}`}
          />
        </div>
      </button>
    </div>
  );
};