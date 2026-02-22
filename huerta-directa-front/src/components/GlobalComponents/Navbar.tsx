import { Link } from "react-router-dom";
import { faPlus } from "@fortawesome/free-solid-svg-icons";
import { Button } from "./Button";
import { ProfileMenu } from "./ProfileMenu";


export const Navbar = () => {
  return (
    <header className="w-full bg-[#e6dcc5] px-10 py-4 flex items-center justify-between shadow-sm">

      {/* Logo */}
      <Link
        to="/"
        className="text-[#8dc84b] text-[26px] font-bold tracking-wide hover:scale-105 transition"
      >
        HUERTA DIRECTA
      </Link>

      {/* Navegación */}
      <nav className="flex items-center gap-10">

        <Link
          to="/categorias"
          className="text-[#1f1f1f] font-semibold text-[18px] hover:text-[#5aaa37] transition"
        >
          Categorías
        </Link>

        <Link
          to="/productos"
          className="text-[#1f1f1f] font-semibold text-[18px] hover:text-[#5aaa37] transition"
        >
          Productos
        </Link>

        {/* Botón reutilizado */}
        <Button
          text="Agrega productos"
          to="/crear-producto"
          iconLetf={faPlus}
          className="bg-[#78d64b] hover:bg-[#5aaa37] rounded-lg px-5 py-2"
        />

        {/* Profile reutilizado */}
        <ProfileMenu
          userName="Productos"
          userRole="Cliente"
          onLogout={() => console.log("Cerrar sesión")}
        />

      </nav>
    </header>
  );
};