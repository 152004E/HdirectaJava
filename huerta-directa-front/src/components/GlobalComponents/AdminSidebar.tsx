import React from "react";
import { useLocation } from "react-router-dom";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faHouse,
  faChartPie,
  faUsers,
  faBoxesStacked,
  faFileContract,
  faGear,
  faRightFromBracket,
  faXmark,
  faBars,
} from "@fortawesome/free-solid-svg-icons";
import logo from "../../assets/logo_huerta.png";

interface AdminSidebarProps {
  isOpen: boolean;
  onToggle: () => void;
}

export const AdminSidebar: React.FC<AdminSidebarProps> = ({ isOpen, onToggle }) => {
  const location = useLocation();

  const sidebarOptions = [
    { label: "Admin Dashboard", icon: faHouse, link: "/admin-dashboard" },
    { label: "Estadísticas Globales", icon: faChartPie, link: "/admin/stats" },
    { label: "Gestión de Usuarios", icon: faUsers, link: "/admin/usuarios" },
    { label: "Gestión de Productos", icon: faBoxesStacked, link: "/admin/productos" },
    { label: "Reportes de Ventas", icon: faFileContract, link: "/admin/reportes" },
    { label: "Configuración", icon: faGear, link: "/admin/config" },
    { label: "Pagina Principal", icon: faRightFromBracket, link: "/" },
  ];

  return (
    <>
      {/* Sidebar Toggle Button */}
      <button
        className="fixed left-5 top-25 z-1001 bg-[#004d00] text-white w-11.25 h-11.25 rounded-full flex items-center justify-center text-xl shadow-lg hover:scale-110 transition-all duration-300"
        onClick={onToggle}
      >
        <FontAwesomeIcon icon={isOpen ? faXmark : faBars} />
      </button>

      {/* Overlay */}
      {isOpen && (
        <div
          className="fixed inset-0 bg-black/30 backdrop-blur-[2px] z-999 lg:hidden"
          onClick={onToggle}
        />
      )}

      {/* Dynamic Sidebar */}
      <aside
        className={`fixed top-0 h-screen bg-white z-1000 shadow-2xl transition-all duration-400 ease-[cubic-bezier(0.175,0.885,0.32,1.1)] p-8 flex flex-col w-[320px] ${
          isOpen ? "left-0" : "-left-85"
        }`}
      >
        <div className="flex items-center gap-4 mb-10 pb-4 border-b border-gray-100">
          <img src={logo} alt="Logo" className="w-11.25" />
          <h2 className="text-xl font-extrabold text-[#004d00]">
            Huerta<span className="text-[#8dc84b]">Admin</span>
          </h2>
        </div>

        <nav className="flex flex-col gap-2 flex-1">
          {sidebarOptions.map((opt, idx) => (
            <a
              key={idx}
              href={opt.link}
              className={`flex items-center gap-5 p-4 rounded-xl transition-all font-medium relative ${
                location.pathname === opt.link
                  ? "bg-[#e8f5e9] text-[#004d00] before:content-[''] before:absolute before:left-0 before:top-[20%] before:h-[60%] before:w-1.25 before:bg-[#004d00] before:rounded-r-full"
                  : "text-gray-500 hover:bg-[#e8f5e9] hover:text-[#004d00]"
              } ${idx === sidebarOptions.length - 1 ? "mt-auto border-t border-gray-100 pt-6" : ""}`}
            >
              <FontAwesomeIcon icon={opt.icon} className="text-2xl w-6.25" />
              <h3 className="text-base m-0">{opt.label}</h3>
            </a>
          ))}
        </nav>
      </aside>
    </>
  );
};
