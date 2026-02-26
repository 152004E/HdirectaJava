import { Outlet } from "react-router-dom";
import { Navbar } from "../components/GlobalComponents/Navbar";
import { Footer } from "../components/GlobalComponents/Footer";

interface MainLayoutProps {
  navbarProps?: React.ComponentProps<typeof Navbar>;
}
const MainLayout = ({ navbarProps }: MainLayoutProps) => {
  return (
    <div className="min-h-screen bg-[#FEF5DC] flex flex-col overflow-x-hidden">

      {/* Navbar centrado */}
      <div className="w-full max-w-330 mx-auto px-4">
        <Navbar {...navbarProps} />
      </div>

      {/* IMPORTANTE: quitamos el max-w */}
      <main className="flex-1 w-full">
        <Outlet />
      </main>

      {/* Footer FULL WIDTH */}
      <Footer />
    </div>
  );
};
export default MainLayout;