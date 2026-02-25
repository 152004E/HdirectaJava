import { HeaderSection } from "../../components/Home/HeaderSection"
import { usePageTitle } from "../../hooks/usePageTitle";

export const HomePage = () => {
    usePageTitle("Pagina Principal ");
  return (
    <main>
      <HeaderSection/>
        
    </main>
  )
}

