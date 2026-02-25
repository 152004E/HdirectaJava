import React, { useState } from "react";
import { Outlet } from "react-router-dom";
import { Background } from "../components/GlobalComponents/Background";
import { Sidebar } from "../components/GlobalComponents/Sidebar";
import { DashboardHeader } from "../components/GlobalComponents/DashboardHeader";

export const DashboardLayout: React.FC = () => {
  const [isSidebarOpen, setIsSidebarOpen] = useState(false);

  const toggleSidebar = () => setIsSidebarOpen(!isSidebarOpen);

  return (
    <div className="relative min-h-screen bg-[#FEF5DC] overflow-x-hidden">
      <Background />

      {/* Top Header Controls */}
      <DashboardHeader />

      {/* Sidebar logic extracted to component */}
      <Sidebar isOpen={isSidebarOpen} onToggle={toggleSidebar} />

      {/* Main Content Area - Conditional margin on LG screens */}
      <main className={`transition-all duration-400 ${isSidebarOpen ? "lg:ml-[320px]" : "lg:ml-0"}`}>
        <div className="p-4 lg:p-8">
          <Outlet />
        </div>
      </main>
    </div>
  );
};


export default DashboardLayout;
