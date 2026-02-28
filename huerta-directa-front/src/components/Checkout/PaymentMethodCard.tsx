import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faCreditCard } from "@fortawesome/free-solid-svg-icons";
import { usePayment } from "../../hooks/usePayment";

export const PaymentMethodCard = () => {
	const { paymentMethod, setPaymentMethod } = usePayment();

	const paymentMethods = [
		{ id: "credit_card", name: "Tarjeta de Crédito", icon: "💳" },
		{ id: "debit_card", name: "Tarjeta de Débito", icon: "💳" },
		{ id: "transfer", name: "Transferencia Bancaria", icon: "🏦" },
	];

	return (
		<div className="bg-white rounded-lg shadow-lg p-6 border-l-4 border-[#8BC34A]">
			<div className="flex items-center gap-2 mb-4">
				<FontAwesomeIcon icon={faCreditCard} className="text-gray-600" />
				<h2 className="text-lg font-semibold text-gray-800">Método de Pago</h2>
			</div>

			{/* Método seleccionado */}
			<div className="mb-6 p-4 bg-[#f0f8e6] rounded-lg border border-[#8BC34A]">
				<p className="text-sm text-gray-600 mb-1">Método de pago seleccionado</p>
				<div className="flex items-center justify-between">
					<p className="font-bold text-gray-800">
						{paymentMethods.find((m) => m.id === paymentMethod)?.icon}{" "}
						{paymentMethods.find((m) => m.id === paymentMethod)?.name}
					</p>
					<button className="text-sm text-[#8BC34A] hover:text-[#7CB342] font-medium">
						Cambiar
					</button>
				</div>
			</div>

			{/* Opciones de pago */}
			<div className="space-y-3">
				{paymentMethods.map((method) => (
					<label
						key={method.id}
						className="flex items-center gap-3 p-3 border border-gray-200 rounded-lg cursor-pointer hover:bg-gray-50 transition"
					>
						<input
							type="radio"
							name="payment"
							value={method.id}
							checked={paymentMethod === method.id}
							onChange={() => setPaymentMethod(method.id)}
							className="w-4 h-4"
						/>
						<span className="text-lg">{method.icon}</span>
						<span className="font-medium text-gray-800">{method.name}</span>
					</label>
				))}
			</div>

			{/* Nota de seguridad */}
			<p className="text-xs text-gray-500 mt-4 text-center">
				✓ Tus datos de pago son encriptados y seguros
			</p>
		</div>
	);
};
