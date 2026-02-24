import { Outlet } from "react-router-dom";
import { Navbar } from "../components/GlobalComponents/Navbar";

interface MainLayoutProps {
  navbarProps?: React.ComponentProps<typeof Navbar>;
}

const MainLayout = ({ navbarProps }: MainLayoutProps) => {
  return (
    <div className="min-h-screen bg-[#FEF5DC] overflow-x-hidden">
      <div className="w-full max-w-300 mx-auto px-4">
        <Navbar {...navbarProps} />
        
        <Outlet />
      </div>
    </div>
  );
};

export default MainLayout;