import logo from "../../assets/logo_huerta.png";
import { Button } from "../../components/GlobalComponents/Button";
import { Background } from "../../components/GlobalComponents/Background";
import { usePageTitle } from "../../hooks/usePageTitle";

export const Landing = () => {
  usePageTitle("Invitación ");
  return (
    <main className="  m-h-screen  font-sans text-[18px] relative dark:bg-[#1A221C]">
      <Background />

      {/* Sección Invitación */}
      <section className="text-center w-full flex flex-col items-center justify-center  py-24  ">
        <div className="max-w-200 mx-auto">
          <img
            src={logo}
            alt="Logo Huerta Directa"
            className="max-w-50 w-full mx-auto my-12 transition-transform duration-300 ease-in-out hover:scale-110"
          />

          <h2 className="text-[2.8rem] text-[#4caf50] font-bold mb-4">
            ¡Únete a Huerta Directa!
          </h2>

          <p className="text-[1.3rem] text-[#5a4e3c] mb-8">
            Forma parte de nuestra comunidad que apoya al campo y disfruta de
            productos frescos, saludables y sin intermediarios.
          </p>

          <Button
            to="/login"
            text={"Registrarse"}
            className="inline-block bg-[#78d64b] text-white font-bold py-4 px-10 rounded-full text-[1.1rem] uppercase tracking-[1px] transition-all duration-500 hover:bg-[#5aaa37] hover:scale-105 hover:shadow-lg no-underline"
          />
        </div>
      </section>
    </main>
  );
};
