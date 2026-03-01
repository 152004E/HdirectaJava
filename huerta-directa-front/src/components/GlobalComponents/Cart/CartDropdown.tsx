import { useEffect } from "react";
import { Button } from "../Button";
import logo from "../../../assets/logo_huerta.png";
import {
  faCartShopping,
  faCircleXmark,
  faCreditCard,
  faTrashCan,
} from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";

interface Props {
  open: boolean;
  onClose: () => void;
}

export const CartDropdown = ({ open, onClose }: Props) => {
  useEffect(() => {
    if (open) {
      document.body.style.overflow = "hidden";
    } else {
      document.body.style.overflow = "auto";
    }

    return () => {
      document.body.style.overflow = "auto";
    };
  }, [open]);

  return (
    <>
      {/* OVERLAY */}
      <div
        onClick={onClose}
        className={`
          fixed inset-0
          bg-black/40 backdrop-blur-sm
          transition-opacity duration-300
          z-40
          ${open ? "opacity-100 visible" : "opacity-0 invisible"}
        `}
      />

      {/* PANEL */}
      <div
        className={`
          fixed top-0 right-0
          h-full w-150
          bg-white
          
          p-6 shadow-2xl
          transition-transform duration-500
          z-50
          ${open ? "translate-x-0" : "translate-x-full"}
        `}
      >
        {/* PANEL LATERAL */}
        <div
          className={`
          fixed top-0 right-0
          h-full
          w-150
          bg-white
          backdrop-blur-md
          
          shadow-2xl
          transition-transform duration-500
          z-50
          ${open ? "translate-x-0" : "translate-x-full"}
        `}
        >
          <div className="p-6 flex items-center justify-between bg-linear-to-r bg-black">
            <h2 className="text-white text-lg font-semibold uppercase tracking-wide flex gap-2 justify-center items-center">
              <FontAwesomeIcon
                icon={faCartShopping}
                className="text-[#8dc84b]"
              />
              <p>tu carrito</p>{" "}
              <span className="bg-[#8dc84b] text-white ml-4 text-xs font-semibold px-3 py-1 rounded-full shadow">
                3 items
              </span>
            </h2>
            <Button
              text={""}
              className="gap-0! bg-transparent! text-xl "
              onClick={onClose}
              iconLetf={faCircleXmark}
            />
          </div>

          <div className="overflow-y-auto h-[70%] ">
            <div className="flex justify-between  px-20 py-2 border-b border-gray-400 items-center my-1 uppercase text-gray-700 font-bold">
              <p>producto</p>
              <p>Cant.</p>
              <p>Total</p>
            </div>
            <div className="p-6 flex flex-col gap-3">
              <div className="flex justify-between items-center border border-gray-400/50 shadow rounded-xl h-20 px-3 duration-500 transition hover:bg-gray-100/70">
                <div className="w-45 flex items-center gap-2">
                  <img src={logo} className="rounded-md w-16" />
                  <div>
                    <p className="font-bold tracking-wide">Manzana</p>
                    <p className="text-gray-500">$1.000 / U</p>
                  </div>
                </div>

                <div className="w-20 text-center">1</div>
                <div className="w-26 text-center">$1.000</div>
              </div>
            </div>
          </div>

          {/* FOOTER FIJO */}
          <div className="absolute p-6 bottom-0 w-full border-t border-gray-400/50 bg-white">
            <div className="flex justify-between items-center mb-4 text-black">
              <span className="text-lg">Subtotal:</span>
              <span className="font-semibold text-[22px]">$1.000</span>
            </div>

            <div className="flex justify-between">
              <Button
                text="Vaciar"
                iconLetf={faTrashCan}
                className="bg-gray-500/40 text-xl hover:bg-gray-500/60 px-4 py-2 rounded-md flex items-center gap-2"
              />

              <Button
                text="Proceder al Pago"
                iconLetf={faCreditCard}
                className="bg-[#8cc63f] text-xl  hover:bg-[#6da82f] px-7 py-3 rounded-md text-white flex items-center gap-2"
              />
            </div>
            <div className="flex justify-center items-center mt-4">
              <p className="text-gray-400">
                Impuestos y envio calculados al final de la compra.
              </p>
            </div>
          </div>
        </div>
      </div>
    </>
  );
};
