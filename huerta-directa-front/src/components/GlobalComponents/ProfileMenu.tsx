import { useState, useRef, useEffect } from "react";
import {
  faUser,
  faRightFromBracket,
  faArrowTurnDown,
  faChartColumn,
} from "@fortawesome/free-solid-svg-icons";
import logo from "../../assets/logo_huerta.png";
import { Button } from "./Button";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { ThemeToggle } from "./ThemeToggle";

interface ProfileMenuProps {
  userName?: string;
  userRole?: string;
  onLogout?: () => void;
}

export const ProfileMenu = ({
  userName = "Usuario",
  userRole = "Cliente",
  onLogout,
}: ProfileMenuProps) => {
  const [open, setOpen] = useState(false);
  const menuRef = useRef<HTMLDivElement>(null);

  // Cerrar al hacer click afuera
  useEffect(() => {
    const handleClickOutside = (event: MouseEvent) => {
      if (menuRef.current && !menuRef.current.contains(event.target as Node)) {
        setOpen(false);
      }
    };

    document.addEventListener("mousedown", handleClickOutside);
    return () => document.removeEventListener("mousedown", handleClickOutside);
  }, []);

  return (
    <div className="relative" ref={menuRef}>
      {/* Botón que despliega */}
      <button
        onClick={() => setOpen(!open)}
        className="flex items-center gap-2 bg-[#FEF5DC] px-3 py-2 rounded-xl shadow-md hover:scale-105 transition duration-500"
      >
        <img src={logo} alt="Profile" className="w-9 h-9 rounded-full" />
        <span className="text-gray-500 text-xl">
          <FontAwesomeIcon icon={faArrowTurnDown} />
        </span>
      </button>

      {open && (
        <div className="absolute right-0 mt-4 w-60 bg-[#EBEFE5]/70 backdrop-blur-md  border border-white/30 p-4 rounded-xl shadow-lg  flex flex-col gap-3 animate-fadeIn z-50">
          {/* Header */}
          <div className="flex flex-col items-center gap-1 border-b border-gray-300 dark:border-gray-600 pb-4">
            <img src={logo} alt="Profile" className="w-12 h-12" />
            <b className="text-[15px] text-black">{userName}</b>
            <small className="text-[13px] text-gray-600 dark:text-gray-400">
              {userRole}
            </small>
          </div>

          {/* Opciones */}
          <div className="flex flex-col gap-3 border-b border-gray-300 dark:border-gray-600 pb-4">
            <Button
              text="Mi perfil"
              to="/actualizacionUsuario"
              iconRight={faUser}
              className="w-full bg-[#20571b] hover:bg-[#52a54a] rounded-xl py-2"
            />

            <Button
            //validacion de roles para el dashboard 
              //text="DashBoard"
              //to={userRole === "Administrador" || userRole === "Administrador Global" ? "/admin-dashboard" : "/dashboard"}
              text="DashBoard admin"
              to="/admin-dashboard"
              iconRight={faChartColumn}
              className="w-full bg-[#20571b] hover:bg-[#52a54a] rounded-xl py-2"
            />
          </div>

          {/* Logout */}
          <Button
            text="Cerrar Sesión"
            iconRight={faRightFromBracket}
            onClick={onLogout}
            className="w-full bg-[#d6031f] hover:bg-[#df707f] rounded-xl py-2"
          />

          {/* Theme */}
          <div className="pt-2">
            <ThemeToggle />
          </div>
        </div>
      )}
    </div>
  );
};
