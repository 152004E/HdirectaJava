import {
  faCreditCard,
  faStore,
  faTruckMoving,
} from "@fortawesome/free-solid-svg-icons";

import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";

export const InformationSection = () => {
  return (
    <section className="w-[80%] mx-auto  bg-white rounded-xl shadow-xl px-20 py-5">
      <div className="flex justify-between text-center">
        {/* Item 1 */}
        <div className="flex flex-col items-center justify-center">
          <FontAwesomeIcon
            icon={faStore}
            className="text-5xl text-[#8dc84b] mb-2"
          />
          <h3 className="text-[17px] font-semibold text-[#333128] capitalize">
            Venta directa
          </h3>
          <p className="text-sm text-gray-600">
            Del campo a tu mesa, sin <br /> ningun intermediarios.
          </p>
        </div>

        {/* Item 2 */}
        <div className="flex flex-col items-center justify-center">
          <FontAwesomeIcon
            icon={faCreditCard}
            className="text-5xl text-[#8dc84b] mb-2"
          />
          <h3 className="text-[17px] font-semibold text-[#333128] capitalize">
            Pagos seguros
          </h3>
          <p className="text-sm text-gray-600">Compra fácil y con confianza.</p>
        </div>

        {/* Item 3 */}
        <div className="flex flex-col items-center justify-center">
          <FontAwesomeIcon
            icon={faTruckMoving}
            className="text-5xl text-[#8dc84b] mb-2"
          />
          <h3 className="text-[17px] font-semibold text-[#333128] capitalize">
            Entrega a domicilio
          </h3>
          <p className="text-sm text-gray-600">Recibe fresco y rápido.</p>
        </div>
      </div>
    </section>
  );
};
