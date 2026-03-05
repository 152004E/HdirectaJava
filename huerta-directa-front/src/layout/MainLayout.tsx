import { Outlet } from "react-router-dom";
import { Navbar } from "../components/GlobalComponents/Navbar";
import { Footer } from "../components/GlobalComponents/Footer";
import { FloatingActionButton } from "../components/GlobalComponents/FloatingButtons/FloatingActionButton";
import { FloatingChatButton } from "../components/GlobalComponents/FloatingButtons/FloatingChatButton";
import { CartButton } from "../components/GlobalComponents/Cart/CartButton";
import { useEffect, useState } from "react";

interface MainLayoutProps {
  navbarProps?: React.ComponentProps<typeof Navbar>;
}

const MainLayout = ({ navbarProps }: MainLayoutProps) => {
  const [showFloatingCart, setShowFloatingCart] = useState(false);

  useEffect(() => {
    const handleScroll = () => {
      if (window.scrollY > 120) {
        setShowFloatingCart(true);
      } else {
        setShowFloatingCart(false);
      }
    };

    window.addEventListener("scroll", handleScroll);
    return () => window.removeEventListener("scroll", handleScroll);
  }, []);

  return (
    <div className="min-h-screen flex flex-col overflow-x-hidden transition-colors! duration-500! 
    bg-[#FEF5DC] dark:bg-[#1A221C]"> 

      {/* Navbar centrado */}
      <div className="w-full max-w-330 mx-auto px-4">
        <Navbar {...navbarProps} />
      </div>

      {/* CONTENIDO */}
      <main className="flex-1 w-full">
        <Outlet />

        {/* BOTONES FLOTANTES */}
        <div className="fixed bottom-20 right-6 flex flex-col gap-4 z-30">
          <FloatingActionButton label="Quiénes Somos" to="/QuienesSomos" />

          {showFloatingCart && <CartButton />}

          <FloatingChatButton />
        </div>
      </main>

      {/* Footer */}
      <Footer />
    </div>
  );
};

export default MainLayout;