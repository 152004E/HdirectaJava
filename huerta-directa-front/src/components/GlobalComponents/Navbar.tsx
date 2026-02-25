import { Link } from "react-router-dom";
import { faCarrot } from "@fortawesome/free-solid-svg-icons";
import { Button } from "./Button";
import { ProfileMenu } from "./ProfileMenu";

interface NavbarProp {
  className?: string;
  showInicio?: boolean;
  showCategorias?: boolean;
  showProductos?: boolean;
  showQuienesSomos?: boolean;
  showAddProduct?: boolean;
  showProfile?: boolean;
}

export const Navbar = ({
  className,
  showInicio = false,
  showCategorias = false,
  showProductos = false,
  showQuienesSomos = false,
  showAddProduct = false,
  showProfile = false,
}: NavbarProp) => {

  const baseClasses =
    "w-full bg-transparent px-10 py-4 flex items-center justify-between text-[15px]";

  return (
    <header className={`${baseClasses} ${className ?? ""} relative z-50`}>
      
      
      {/* Logo */}
      <Link
        to="/"
        className="text-[#8dc84b] text-[23px] font-bold tracking-wide hover:scale-105 transition"
      >
        HUERTA DIRECTA
      </Link>

      {/* Navegación */}
      <nav className="flex items-center gap-5">

        {showInicio && (
          <Link
            to="/"
            className="text-[#1f1f1f] font-semibold  hover:text-[#5aaa37] transition"
          >
            Inicio
          </Link>
        )}

        {showCategorias && (
          <Link
            to="/categorias"
            className="text-[#1f1f1f] font-semibold  hover:text-[#5aaa37] transition"
          >
            Categorías
          </Link>
        )}

        {showProductos && (
          <Link
            to="/productos"
            className="text-[#1f1f1f] font-semibold hover:text-[#5aaa37] transition"
          >
            Productos
          </Link>
        )}

        {showQuienesSomos && (
          <Link
            to="/quienes-somos"
            className="text-[#1f1f1f] font-semibold  hover:text-[#5aaa37] transition"
          >
            Quiénes Somos
          </Link>
        )}

        {showAddProduct && (
          <Button
            text="Agrega productos"
            to="/crear-producto"
            iconRight={faCarrot}
            className="bg-[#78d64b] hover:bg-[#5aaa37] rounded-lg px-5 py-2"
          />
        )}

        {showProfile && (
          <ProfileMenu
            userName="Productos"
            userRole="Cliente"
            onLogout={() => console.log("Cerrar sesión")}
          />
        )}

      </nav>
    </header>
  );
};