import { useState } from "react";
import { ChatModal } from "../../Modals/ChatModal";
import { faRobot } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";

export const FloatingChatButton = () => {
  const [open, setOpen] = useState(false);

  return (
    <>
      <div
        onClick={() => setOpen(true)}
        className="
        relative
        w-15 h-15
        bg-[#8dc84b]
        text-white
        rounded-full
        flex items-center justify-center
        text-2xl
        cursor-pointer
        shadow-lg
        transition-all duration-500
        hover:w-42.5
        group
        overflow-hidden
        z-1000
        "
      >
        {/* ICONO */}
        <span className="absolute transition-all duration-300 group-hover:-translate-x-10">
          <FontAwesomeIcon icon={faRobot} />
        </span>

        {/* TEXTO */}
        <span className="ml-10 opacity-0 group-hover:opacity-100 transition-opacity duration-500 text-sm font-semibold whitespace-nowrap">
          Chat con IA
        </span>
      </div>

      {open && <ChatModal onClose={() => setOpen(false)} />}
    </>
  );
};