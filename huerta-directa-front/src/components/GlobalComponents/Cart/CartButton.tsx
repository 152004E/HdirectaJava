import { useState, useRef, useEffect } from "react";

import { CartDropdown } from "./CartDropdown";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faCartShopping } from "@fortawesome/free-solid-svg-icons";

export const CartButton = () => {
  const [open, setOpen] = useState(false);
  const containerRef = useRef<HTMLDivElement>(null);

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
        p-2
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
        className="w-7 h-7 pointer-events-none"
      />

      {/* Badge opcional */}
      <span className="absolute -top-2 -right-2 bg-red-500 text-white text-xs px-2 rounded-full">
        1
      </span>
    </div>

    <CartDropdown open={open} />
  </div>
);
};
