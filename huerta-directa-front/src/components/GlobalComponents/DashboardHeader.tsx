import React from "react";
import { ProfileMenu } from "./ProfileMenu";

interface DashboardHeaderProps {
  userName?: string;
  userRole?: string;
}

export const DashboardHeader: React.FC<DashboardHeaderProps> = ({
  userName = "User",
  userRole = "Administrador",
}) => {
  return (
    <div className="fixed top-5 right-5 lg:right-8 z-[1001] flex items-center gap-4">
      <ProfileMenu 
        userName={userName} 
        userRole={userRole} 
        onLogout={() => console.log("Logout triggered")} 
      />
    </div>
  );
};
