import { FloatingActionButton } from "../../components/GlobalComponents/FloatingButtons/FloatingActionButton";
import { FloatingChatButton } from "../../components/GlobalComponents/FloatingButtons/FloatingChatButton";
import CategoriesSection from "../../components/Home/CategoriesSection";
import { HeaderSection } from "../../components/Home/HeaderSection";
import { HeroSlider } from "../../components/Home/HeroSlider";
import { InformationSection } from "../../components/Home/InformationSection";
import { OffersSection } from "../../components/Home/OffersSection";
import { ProductsSection } from "../../components/Home/ProductsSection";
import { usePageTitle } from "../../hooks/usePageTitle";

export const HomePage = () => {
  usePageTitle("Pagina Principal ");
  return (
    <main>
      <HeaderSection />
      <div className="fixed bottom-20 right-6 flex flex-col gap-4 z-1000 ">
        <FloatingActionButton label="Quiénes Somos" to="/QuienesSomos" />

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
