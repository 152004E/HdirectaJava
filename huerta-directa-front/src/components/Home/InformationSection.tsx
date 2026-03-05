import {
  faCreditCard,
  faStore,
  faTruckMoving,
} from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";

export const InformationSection = () => {
  return (
    <section className="transition-colors! duration-500!  w-full bg-linear-to-b from-[#FEF5DC] via-white to-white py-20 
                        dark:bg-[#1A221C] dark:from-[#1A221C] dark:via-[#1A221C] dark:to-white/20">
      <div className="max-w-330 mx-auto px-6">
        <div
          className="bg-white dark:bg-[#111814] rounded-2xl border
          border-gray-400/10 dark:border-gray-700/40
          shadow-xl px-20 py-10
          transition-colors duration-300"
        >
          <div className="flex justify-between text-center flex-wrap gap-10">
            
            {/* Item 1 */}
            <div className="flex flex-col items-center justify-center flex-1 min-w-62.5
                            p-4 rounded-xl 
                            hover:bg-gray-50 dark:hover:bg-[#1A221C] duration-500 transition-all">
              <FontAwesomeIcon
                icon={faStore}
                className="text-5xl text-[#8dc84b] mb-3"
              />
              <h3 className="text-[17px] font-semibold text-[#333128] dark:text-gray-200 capitalize">
                Venta directa
              </h3>
              <p className="text-sm text-gray-600 dark:text-gray-400">
                Del campo a tu mesa, sin <br /> ningún intermediario.
              </p>
            </div>

            {/* Item 2 */}
            <div className="flex flex-col items-center justify-center flex-1 min-w-62.5
                            p-4 rounded-xl transition
                            hover:bg-gray-50 dark:hover:bg-[#1A221C]">
              <FontAwesomeIcon
                icon={faCreditCard}
                className="text-5xl text-[#8dc84b] mb-3"
              />
              <h3 className="text-[17px] font-semibold text-[#333128] dark:text-gray-200 capitalize">
                Pagos seguros
              </h3>
              <p className="text-sm text-gray-600 dark:text-gray-400">
                Compra fácil y con confianza.
              </p>
            </div>

            {/* Item 3 */}
            <div className="flex flex-col items-center justify-center flex-1 min-w-62.5
                            p-4 rounded-xl transition
                            hover:bg-gray-50 dark:hover:bg-[#1A221C]">
              <FontAwesomeIcon
                icon={faTruckMoving}
                className="text-5xl text-[#8dc84b] mb-3"
              />
              <h3 className="text-[17px] font-semibold text-[#333128] dark:text-gray-200 capitalize">
                Entrega a domicilio
              </h3>
              <p className="text-sm text-gray-600 dark:text-gray-400">
                Recibe fresco y rápido.
              </p>
            </div>

          </div>
        </div>
      </div>
    </section>
  );
};