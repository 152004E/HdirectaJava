import { useState, useRef, useEffect } from "react";
import { CartDropdown } from "./CartDropdown";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faCartShopping } from "@fortawesome/free-solid-svg-icons";
import { useCart } from "../../../contexts/CartContext";

export const CartButton = () => {
  const [open, setOpen] = useState(false);
  const containerRef = useRef<HTMLDivElement>(null);

  // 👇 Traemos el carrito
  const { items } = useCart();

  // cerrar al hacer click fuera
  useEffect(() => {
    const handleClickOutside = (e: MouseEvent) => {
      if (
        containerRef.current &&
        !containerRef.current.contains(e.target as Node)
      ) {
        setOpen(false);
      }
    };

    document.addEventListener("mousedown", handleClickOutside);
    return () => document.removeEventListener("mousedown", handleClickOutside);
  }, []);

  return (
    <div ref={containerRef} className="relative">
      {/* BOTÓN */}
      <div
        onClick={() => setOpen(!open)}
        className={`
          relative
          flex items-center justify-center
          p-4
          rounded-xl
          bg-[#FEF5DC]
          transition-all duration-300
          cursor-pointer
          shadow-md
          hover:bg-[#496826]
          hover:text-white
          ${open ? "bg-[#496826] text-white scale-105" : ""}
        `}
      >
        <FontAwesomeIcon
          icon={faCartShopping}
          className="text-[20px] pointer-events-none"
        />

        {/* BADGE DINÁMICO */}
        {items.length > 0 && (
          <span className="absolute -top-2 -right-2 bg-red-500 text-white text-xs px-2.5 py-1 rounded-full">
            {items.length}
          </span>
        )}
      </div>

      <CartDropdown open={open} onClose={() => setOpen(false)} />
    </div>
  );
};