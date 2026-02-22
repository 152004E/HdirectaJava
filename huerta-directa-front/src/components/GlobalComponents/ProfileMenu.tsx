import { useState, useRef, useEffect } from "react";
import {
  faUser,
  faTableColumns,
  faRightFromBracket,
} from "@fortawesome/free-solid-svg-icons";
import logo from "../../assets/logo_huerta.png";
import { Button } from "./Button";

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
        className="flex items-center gap-2 bg-white px-3 py-2 rounded-xl shadow-md hover:scale-105 transition"
      >
        <img src={logo} alt="Profile" className="w-9 h-9 rounded-full" />
        <span className="text-black text-xl">⌄</span>
      </button>

      {/* Dropdown */}
      {open && (
        <div className="absolute right-0 mt-4 w-60 bg-[#ebefe5] p-4 rounded-xl shadow-lg flex flex-col gap-3 animate-fadeIn">
          {/* Header */}
          <div className="flex flex-col items-center border-b border-black pb-3">
            <img src={logo} alt="Profile" className="w-10 h-10 mb-2" />
            <b className="text-[15px]">{userName}</b>
            <small className="text-[13px] text-gray-600">{userRole}</small>
          </div>

          {/* Opciones */}
          <div className="flex flex-col gap-2 border-b border-black pb-3">
            <Button
              text="Mi perfil"
              to="/actualizacionUsuario"
              iconRight={faUser}
              className="w-full bg-[#20571b] hover:bg-[#52a54a] rounded-xl"
            />

            <Button
              text="DashBoard"
              to={userRole === "Administrador" ? "/DashboardAdmin" : "/home"}
              iconRight={faTableColumns}
              className="w-full bg-[#20571b] hover:bg-[#52a54a] rounded-xl"
            />
          </div>

          {/* Logout */}
          <Button
            text="Cerrar Sesión"
            iconRight={faRightFromBracket}
            onClick={onLogout}
            className="w-full bg-[#d6031f] hover:bg-[#df707f] rounded-xl"
          />
        </div>
      )}
    </div>
  );
};
