import { useState } from "react";
import { ChatModal } from "../../Modals/ChatModal";
import { faRobot } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";

export const FloatingChatButton = () => {
  const [open, setOpen] = useState(false);

  return (
    <>
      {/* CONTENEDOR FIJO (NO CAMBIA TAMAÑO) */}
      <div className="relative w-15 h-15 z-30">
        {/* BOTÓN EXPANDIBLE */}
        <div
          onClick={() => setOpen(true)}
          className="
          group
            absolute right-0 top-0
            h-15
            w-15
            bg-[#8dc84b]
            text-white
            rounded-full
            flex items-center
            shadow-lg
            cursor-pointer
            overflow-hidden
            transition-all duration-300
            hover:w-42
          "
        >
          {/* ICONO */}
          <div className="w-15 pl-3.5 flex justify-center text-2xl">
            <FontAwesomeIcon icon={faRobot} />
          </div>

          {/* TEXTO */}
          
          <span className="opacity-0  group-hover:opacity-100 transition-opacity duration-300 text-sm font-semibold whitespace-nowrap pr-5">
            Chat con IA
          </span>
        </div>
      </div>

      {/* MODAL */}
      {open && <ChatModal onClose={() => setOpen(false)} />}
    </>
  );
};