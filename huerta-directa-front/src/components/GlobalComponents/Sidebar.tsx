import React from "react";
import { useLocation } from "react-router-dom";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faHouse,
  faChartLine,
  faChartPie,
  faEnvelope,
  faFileLines,
  faUsers,
  faBoxesStacked,
  faFileContract,
  faGear,
  faUserPlus,
  faRightFromBracket,
  faXmark,
  faBars,
} from "@fortawesome/free-solid-svg-icons";
import logo from "../../assets/logo_huerta.png";

interface SidebarProps {
  isOpen: boolean;
  onToggle: () => void;
  role?: "producer" | "admin";
}

export const Sidebar: React.FC<SidebarProps> = ({ isOpen, onToggle, role = "producer" }) => {
  const location = useLocation();

  const isProducer = role === "producer";

  const producerOptions = [
    { label: "Dashboard", icon: faHouse, link: "/dashboard" },
    { label: "Gráficos", icon: faChartLine, link: "/DashBoardGraficos" },
    { label: "Área Social", icon: faEnvelope, link: "/MensajesAreaSocial" },
    { label: "Ordenes", icon: faFileLines, link: "/misOrdenes" },
    { label: "Agregar Producto", icon: faBoxesStacked, link: "/DashBoardAgregarProducto" },
    { label: "Mi Perfil", icon: faGear, link: "/actualizacionUsuario" },
    { label: "Pagina Principal", icon: faRightFromBracket, link: "/HomePage" },
  ];

  const adminOptions = [
    { label: "Admin Dashboard", icon: faHouse, link: "/admin-dashboard" },
    { label: "Estadísticas Globales", icon: faChartPie, link: "/admin/stats" },
    { label: "Gestión de Usuarios", icon: faUsers, link: "/admin/usuarios" },
    { label: "Gestión de Productos", icon: faBoxesStacked, link: "/admin/productos" },
    { label: "Reportes de Ventas", icon: faFileContract, link: "/admin/reportes" },
    { label: "Configuración", icon: faGear, link: "/admin/config" },
    { label: "Registrar Admin", icon: faUserPlus, link: "/admin/registrar" },
    { label: "Pagina Principal", icon: faRightFromBracket, link: "/HomePage" },
  ];

  const sidebarOptions = isProducer ? producerOptions : adminOptions;

  const btnBg = isProducer ? "bg-[#8dc84b]" : "bg-[#004d00]";
  const textTitle = isProducer ? "" : "text-[#004d00]";
  
  const activeBg = isProducer ? "bg-[#ddfacc]" : "bg-[#e8f5e9]";
  const activeText = isProducer ? "text-[#8dc84b]" : "text-[#004d00]";
  const activeBeforeBg = isProducer ? "before:bg-[#8dc84b]" : "before:bg-[#004d00]";
  const hoverBg = isProducer ? "hover:bg-[#ddfacc]" : "hover:bg-[#e8f5e9]";
  const hoverText = isProducer ? "hover:text-[#8dc84b]" : "hover:text-[#004d00]";

  return (
    <>
      {/* Sidebar Toggle Button */}
      <button
        className={`fixed left-5 top-25 z-1001 text-white w-11.25 h-11.25 rounded-full flex items-center justify-center text-xl shadow-lg hover:scale-110 transition-all duration-300 ${btnBg}`}
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
          <h2 className={`text-xl font-extrabold ${textTitle}`}>
            Huerta<span className="text-[#8dc84b]">{isProducer ? "Directa" : "Admin"}</span>
          </h2>
        </div>

        <nav className="flex flex-col gap-2 flex-1">
          {sidebarOptions.map((opt, idx) => {
            const isActive = location.pathname === opt.link;
            return (
              <a
                key={idx}
                href={opt.link}
                className={`flex items-center gap-5 p-4 rounded-xl transition-all font-medium relative ${
                  isActive
                    ? `${activeBg} ${activeText} before:content-[''] before:absolute before:left-0 before:top-[20%] before:h-[60%] before:w-1.25 ${activeBeforeBg} before:rounded-r-full`
                    : `text-gray-500 ${hoverBg} ${hoverText}`
                } ${idx === sidebarOptions.length - 1 ? "mt-auto border-t border-gray-100 pt-6" : ""}`}
              >
                <FontAwesomeIcon icon={opt.icon} className="text-2xl w-6.25" />
                <h3 className="text-base m-0">{opt.label}</h3>
              </a>
            );
          })}
        </nav>
      </aside>
    </>
  );
};
