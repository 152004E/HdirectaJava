import type { IconDefinition } from "@fortawesome/fontawesome-svg-core";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { Link } from "react-router-dom";

interface ButtonProps {
  to?: string;
  text: string;
  iconLetf?: IconDefinition;
  iconRight?: IconDefinition;
  type?: "button" | "submit";
  onClick?: () => void;
  disabled?: boolean;
  className?: string;
}

export const Button = ({
  to,
  text,
  type = "button",
  iconLetf,
  iconRight,
  onClick,
  disabled,
  className,
}: ButtonProps) => {
  const baseClasses =
    "inline-flex items-center justify-center gap-2 mt-[15px] px-[20px] py-[10px] text-white bg-[#8dc84b] rounded-[5px] transition-all duration-500 ease-in-out hover:scale-105 hover:bg-[#5aaa37] disabled:opacity-50 disabled:cursor-not-allowed";

  const finalClasses = `${baseClasses} ${className ?? ""}`;

  if (to) {
    return (
      <Link to={to} className={finalClasses}>
        {iconLetf && <FontAwesomeIcon icon={iconLetf} />}
        <span>{text}</span>
        {iconRight && <FontAwesomeIcon icon={iconRight} />}
      </Link>
    );
  }

  return (
    <button
      type={type}
      onClick={onClick}
      disabled={disabled}
      className={finalClasses}
    >
      {iconLetf && <FontAwesomeIcon icon={iconLetf} />}
      <span>{text}</span>
      {iconRight && <FontAwesomeIcon icon={iconRight} />}
    </button>
  );
};