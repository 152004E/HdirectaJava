import { FloatingActionButton } from "../../components/GlobalComponents/FloatingButtons/FloatingActionButton";
import { FloatingChatButton } from "../../components/GlobalComponents/FloatingButtons/FloatingChatButton";
import { HeaderSection } from "../../components/Home/HeaderSection";
import { InformationSection } from "../../components/Home/InformationSection";
import { usePageTitle } from "../../hooks/usePageTitle";

export const HomePage = () => {
  usePageTitle("Pagina Principal ");
  return (
    <main>
      <HeaderSection />
      <div className="fixed bottom-20 right-6 flex flex-col gap-4 z-1000 ">
        <FloatingActionButton label="Quiénes Somos" to="/quienes-somos" />

        <FloatingChatButton />
      </div>
      <InformationSection/>
    </main>
  );
};
