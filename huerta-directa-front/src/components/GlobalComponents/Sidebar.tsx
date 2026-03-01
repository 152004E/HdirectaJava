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

  const textTitle = isProducer ? "" : "text-[#004d00]";
  
  const activeBg = isProducer ? "bg-[#e8f5e9]" : "bg-[#e8f5e9]";
  const activeText = isProducer ? "text-[#2e7d32] font-bold" : "text-[#004d00] font-bold";
  const hoverBg = isProducer ? "hover:bg-gray-100" : "hover:bg-gray-100";

  return (
    <>
      {/* Overlay for mobile */}
      <div
        className={`fixed inset-0 bg-black/30 backdrop-blur-[2px] z-999 md:hidden transition-opacity duration-300 ${
          isOpen ? "opacity-100" : "opacity-0 pointer-events-none"
        }`}
        onClick={onToggle}
      />

      {/* Dynamic Sidebar */}
      <aside
        className={`fixed top-0 left-0 h-screen bg-white z-[1040] shadow-xl transition-all duration-300 ease-in-out flex flex-col group/sidebar ${
          isOpen 
            ? "w-[280px]" 
            : "w-0 md:w-[80px] md:hover:w-[280px] -translate-x-full md:translate-x-0"
        }`}
      >
        {/* Toggle & Logo Section */}
        <div className={`flex items-center gap-4 px-4 h-20 transition-all ${!isOpen && "md:group-hover/sidebar:justify-start md:justify-center"}`}>
           <button
            className={`w-10 h-10 flex items-center justify-center rounded-full hover:bg-gray-100 transition-colors text-gray-500 text-xl cursor-pointer border-none bg-transparent shrink-0`}
            onClick={onToggle}
          >
            <FontAwesomeIcon icon={faBars} />
          </button>
          
          <div className={`flex items-center gap-2 overflow-hidden whitespace-nowrap transition-all duration-300 ${
            isOpen ? "opacity-100 w-auto" : "opacity-0 w-0 md:group-hover/sidebar:opacity-100 md:group-hover/sidebar:w-auto"
          }`}>
            <img src={logo} alt="Logo" className="w-8 h-8 shrink-0" />
            <h2 className={`text-lg font-black ${textTitle}`}>
              Huerta<span className="text-[#8dc84b]">{isProducer ? "Directa" : "Admin"}</span>
            </h2>
          </div>
        </div>

        {/* Navigation Section */}
        <nav className="flex flex-col gap-1 px-3 mt-4 flex-1 overflow-x-hidden overflow-y-auto custom-scrollbar">
          {sidebarOptions.map((opt, idx) => {
            const isActive = location.pathname === opt.link;
            return (
              <a
                key={idx}
                href={opt.link}
                className={`flex items-center transition-all h-12 rounded-full px-4 relative group/item overflow-hidden whitespace-nowrap ${
                  isActive
                    ? `${activeBg} ${activeText}`
                    : `text-gray-600 ${hoverBg}`
                } ${!isOpen ? "md:justify-start md:px-4 md:w-full mx-auto" : ""}`}
                title={!isOpen ? opt.label : ""}
              >
                <div className={`flex items-center justify-center w-6 shrink-0 transition-all ${!isOpen && "md:ml-1"}`}>
                  <FontAwesomeIcon icon={opt.icon} className={`text-xl ${isActive ? (isProducer ? "text-[#8dc84b]" : "text-[#004d00]") : "text-gray-500"}`} />
                </div>
                
                <span className={`ml-5 text-sm font-medium tracking-wide transition-all duration-300 ${
                  isOpen ? "opacity-100 translate-x-0" : "opacity-0 -translate-x-4 md:group-hover/sidebar:opacity-100 md:group-hover/sidebar:translate-x-0"
                }`}>
                  {opt.label}
                </span>
                
                {/* Tooltip for collapsed state (hidden on hover expansion) */}
                {!isOpen && (
                  <div className="absolute left-16 bg-gray-800 text-white text-xs py-1 px-2 rounded opacity-0 group-hover/item:opacity-100 md:group-hover/sidebar:hidden pointer-events-none transition-opacity whitespace-nowrap z-50">
                    {opt.label}
                  </div>
                )}
              </a>
            );
          })}
        </nav>
      </aside>
    </>
  );
};
