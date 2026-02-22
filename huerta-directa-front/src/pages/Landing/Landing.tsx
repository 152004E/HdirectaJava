import { Link } from "react-router-dom";
import logo from "../../assets/logo_huerta.png";

export const Landing = () => {
  return (
    <main className="bg-[#FEF5DC] min-h-screen font-sans text-[18px]">

      {/* Navbar */}
      <div className="absolute top-0 left-0 right-0 flex items-center justify-between max-w-300 mx-auto px-6 py-4 bg-[#FEF5DC]">
        
        {/* Logo */}
        <Link
          to="/"
          className="text-[#8dc84b] text-[25px] font-semibold uppercase tracking-[1px] transition-all duration-500 ease-in-out hover:text-[#028602] hover:scale-110 no-underline"
        >
          Huerta directa
        </Link>

        {/* Navbar */}
        <nav>
          <ul className="flex space-x-2">
            <li>
              <Link
                to="/"
                className="block text-[#333128] text-[18px] font-semibold py-5 px-5 transition-colors duration-500 hover:text-[#5aaa37] no-underline"
              >
                Inicio
              </Link>
            </li>
            <li>
              <Link
                to="/quienes-somos"
                className="block text-[#333128] text-[18px] font-semibold py-5 px-5 transition-colors duration-500 hover:text-[#5aaa37] no-underline"
              >
                Quiénes Somos
              </Link>
            </li>
          </ul>
        </nav>
      </div>

      {/* Sección Invitación */}
      <section className="text-center w-full flex flex-col items-center justify-center px-6 py-24 mt-20">
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

          <Link
            to="/login"
            className="inline-block bg-[#78d64b] text-white font-bold py-4 px-10 rounded-full text-[1.1rem] uppercase tracking-[1px] transition-all duration-500 hover:bg-[#5aaa37] hover:scale-105 hover:shadow-lg no-underline"
          >
            Registrarse
          </Link>

        </div>
      </section>

    </main>
  );
};