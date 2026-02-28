import React, { useState } from "react";
import { Outlet } from "react-router-dom";
import { AdminSidebar } from "../components/GlobalComponents/AdminSidebar";
import { DashboardHeader } from "../components/GlobalComponents/DashboardHeader";

export const AdminDashboardLayout: React.FC = () => {
  const [isSidebarOpen, setIsSidebarOpen] = useState(false);

  const toggleSidebar = () => setIsSidebarOpen(!isSidebarOpen);

  return (
    <div className="relative min-h-screen bg-[#F0F4EF] overflow-x-hidden">
      {/* Top Header Controls */}
      <DashboardHeader userRole="Administrador Global" />

      {/* Sidebar logic extracted to component */}
      <AdminSidebar isOpen={isSidebarOpen} onToggle={toggleSidebar} />

      {/* Main Content Area - Conditional margin on LG screens */}
      <main className={`transition-all duration-400 ${isSidebarOpen ? "lg:ml-80" : "lg:ml-0"}`}>
        <div className="p-4 lg:p-8">
          <Outlet />
        </div>
      </main>
    </div>
  );
};

export default AdminDashboardLayout;
