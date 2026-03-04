

import CategoriesSection from "../../components/Home/CategoriesSection";
import { HeaderSection } from "../../components/Home/HeaderSection";
import { HeroSlider } from "../../components/Home/HeroSlider";
import { InformationSection } from "../../components/Home/InformationSection";
import { OffersSection } from "../../components/Home/OffersSection";
import { ProductsSection } from "../../components/Home/ProductsSection";
import { usePageTitle } from "../../hooks/usePageTitle";

export const HomePage = () => {
  usePageTitle("Pagina Principal");



  return (
    <main>
      <HeaderSection />

     

      <InformationSection />
      <OffersSection />
      <HeroSlider />
      <ProductsSection />
      <CategoriesSection />
    </main>
  );
};