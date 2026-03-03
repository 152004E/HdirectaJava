import { useEffect, useState } from "react";

import { FloatingActionButton } from "../../components/GlobalComponents/FloatingButtons/FloatingActionButton";
import { FloatingChatButton } from "../../components/GlobalComponents/FloatingButtons/FloatingChatButton";
import { CartButton } from "../../components/GlobalComponents/Cart/CartButton";

import CategoriesSection from "../../components/Home/CategoriesSection";
import { HeaderSection } from "../../components/Home/HeaderSection";
import { HeroSlider } from "../../components/Home/HeroSlider";
import { InformationSection } from "../../components/Home/InformationSection";
import { OffersSection } from "../../components/Home/OffersSection";
import { ProductsSection } from "../../components/Home/ProductsSection";
import { usePageTitle } from "../../hooks/usePageTitle";

export const HomePage = () => {
  usePageTitle("Pagina Principal");

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
    <main>
      <HeaderSection />

      {/* BOTONES FLOTANTES */}
      <div className="fixed bottom-20 right-6 flex flex-col gap-4 z-30">

        <FloatingActionButton label="Quiénes Somos" to="/QuienesSomos" />

        {showFloatingCart && <CartButton />}

        <FloatingChatButton />

      </div>

      <InformationSection />
      <OffersSection />
      <HeroSlider />
      <ProductsSection />
      <CategoriesSection />
    </main>
  );
};