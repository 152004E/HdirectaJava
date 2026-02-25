import React, { useState } from "react";
import { Outlet, useLocation } from "react-router-dom";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faBars,
  faXmark,
  faHouse,
  faChartLine,
  faEnvelope,
  faFileLines,
  faBoxesStacked,
  faGear,
  faRightFromBracket,
} from "@fortawesome/free-solid-svg-icons";
import logo from "../assets/logo_huerta.png";
import { Background } from "../components/GlobalComponents/Background";
import { ProfileMenu } from "../components/GlobalComponents/ProfileMenu";

export const DashboardLayout: React.FC = () => {
  const [isSidebarOpen, setIsSidebarOpen] = useState(false);
  const location = useLocation();

  const toggleSidebar = () => setIsSidebarOpen(!isSidebarOpen);

  const sidebarOptions = [
    { label: "Dashboard", icon: faHouse, link: "/dashboard" },
    { label: "Gráficos", icon: faChartLine, link: "/DashBoardGraficos" },
    { label: "Área Social", icon: faEnvelope, link: "/MensajesAreaSocial" },
    { label: "Ordenes", icon: faFileLines, link: "/misOrdenes" },
    { label: "Agregar Producto", icon: faBoxesStacked, link: "/DashBoardAgregarProducto" },
    { label: "Mi Perfil", icon: faGear, link: "/actualizacionUsuario" },
    { label: "Pagina Principal", icon: faRightFromBracket, link: "/" },
  ];

  return (
    <div className="relative min-h-screen bg-[#FEF5DC] overflow-x-hidden">
      <Background />

      {/* Top Header Controls */}
      <div className="fixed top-5 right-5 lg:right-8 z-[1001] flex items-center gap-4">
        <ProfileMenu 
          userName="Admin User" 
          userRole="Administrador" 
          onLogout={() => console.log("Logout triggered")} 
        />
      </div>

      {/* Sidebar Toggle Button */}
      <button
        className="fixed left-5 top-[100px] z-[1001] bg-[#8dc84b] text-white w-[45px] h-[45px] rounded-full flex items-center justify-center text-xl shadow-lg hover:scale-110 transition-transform lg:hidden"
        onClick={toggleSidebar}
      >
        <FontAwesomeIcon icon={isSidebarOpen ? faXmark : faBars} />
      </button>

      {/* Overlay */}
      {isSidebarOpen && (
        <div
          className="fixed inset-0 bg-black/30 backdrop-blur-[2px] z-[999] lg:hidden"
          onClick={toggleSidebar}
        />
      )}

      {/* Dynamic Sidebar */}
      <aside
        className={`fixed top-0 h-screen bg-white z-[1000] shadow-2xl transition-all duration-400 ease-[cubic-bezier(0.175,0.885,0.32,1.1)] p-8 flex flex-col w-[320px] ${
          isSidebarOpen ? "left-0" : "-left-[340px] lg:left-0"
        }`}
      >
        <div className="flex items-center gap-4 mb-10 pb-4 border-b border-gray-100">
          <img src={logo} alt="Logo" className="w-[45px]" />
          <h2 className="text-xl font-extrabold">
            Huerta<span className="text-[#8dc84b]">Directa</span>
          </h2>
        </div>

        <nav className="flex flex-col gap-2 flex-1">
          {sidebarOptions.map((opt, idx) => (
            <a
              key={idx}
              href={opt.link}
              className={`flex items-center gap-5 p-4 rounded-xl transition-all font-medium relative ${
                location.pathname === opt.link
                  ? "bg-[#ddfacc] text-[#8dc84b] before:content-[''] before:absolute before:left-0 before:top-[20%] before:h-[60%] before:w-[5px] before:bg-[#8dc84b] before:rounded-r-full"
                  : "text-gray-500 hover:bg-[#ddfacc] hover:text-[#8dc84b]"
              } ${idx === sidebarOptions.length - 1 ? "mt-auto border-t border-gray-100 pt-6" : ""}`}
            >
              <FontAwesomeIcon icon={opt.icon} className="text-2xl w-[25px]" />
              <h3 className="text-base m-0">{opt.label}</h3>
            </a>
          ))}
        </nav>
      </aside>

      {/* Main Content Area */}
      <main className={`transition-all duration-400 ${isSidebarOpen ? "lg:ml-[320px]" : "lg:ml-[320px]"}`}>
        <div className="p-4 lg:p-8">
          <Outlet />
        </div>
      </main>
    </div>
  );
};

export default DashboardLayout;
