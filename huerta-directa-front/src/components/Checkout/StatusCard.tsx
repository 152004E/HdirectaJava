import { faCreditCard } from "@fortawesome/free-solid-svg-icons";
import { useCart } from "../../hooks/useCart";
import { Button } from "../GlobalComponents/Button.tsx";

export const StatusCard = () => {
    const {  } = useCart();


    return (
        <div className="bg-white border-2 border-[#8BC34A] rounded-lg p-6 sticky top-24 h-fit">
            <h2 className="text-xl font-bold text-gray-800 mb-4">APROBADO</h2>

            {/* Totales */}
            <div className="space-y-3 mb-6 pb-6 border-b border-gray-200">
                <div className="flex justify-between text-gray-700">
                    <span>Su Pedido A Sido Registrado Exitosamente</span>
                </div>


                <div className="flex justify-between text-gray-700">
                    <span>Envío</span>
                    <span className="font-medium">$5.000</span>
                </div>

                <div className="flex justify-between text-gray-700">
                    <span>IVA (19%)</span>
                </div>
            </div>

            {/* Total Grande */}
            <div className="flex justify-between mb-6">
                <span className="text-xl font-bold text-gray-800">Total</span>
            </div>

            {/* Botón Pagar */}
            <Button
                to="/payment/MercadoPayment"
                text="Gracias por su compra"
                iconLetf={faCreditCard}
                className="w-full py-3"
            />


            {/* Info de seguridad */}
            <p className="text-center text-xs text-gray-500 mt-4">
                🔒 Pago seguro - Tus datos están protegidos
            </p>
        </div>
    );
};

