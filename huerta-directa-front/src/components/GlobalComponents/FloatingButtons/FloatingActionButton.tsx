import { faUserGroup } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { useNavigate } from "react-router-dom";

interface Props {
 
  label: string;
  to: string;
}

export const FloatingActionButton = ({  label, to }: Props) => {
  const navigate = useNavigate();

  return (
    <div
      onClick={() => navigate(to)}
      className="group fixed bottom-2 right-6 text-white
      w-15 h-15 
      bg-[#8dc84b] 
      rounded-full 
      flex items-center justify-center 
      text-2xl 
      cursor-pointer 
      shadow-lg 
      transition-all duration-500!
      hover:w-50
      overflow-hidden 
      z-30"
    >
      {/* Icono */}
      <span className="absolute  transition-all duration-300 group-hover:-translate-x-15">
        <FontAwesomeIcon icon={faUserGroup} />
      </span>

      {/* Texto */}
      <span className="ml-10 opacity-0 group-hover:opacity-100 transition-opacity duration-300 text-sm font-semibold whitespace-nowrap text-white">
        {label}
      </span>
    </div>
  );
};