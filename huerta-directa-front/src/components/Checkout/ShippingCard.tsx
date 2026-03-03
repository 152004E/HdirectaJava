import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faTruck, faMapMarkerAlt } from "@fortawesome/free-solid-svg-icons";
import { usePayment } from "../../hooks/usePayment";

export const ShippingCard = () => {
	const { shippingMethod, setShippingMethod } = usePayment();

	const shippingOptions = [
		{ id: "standard", name: "Envío Estándar", time: "2-3 días", price: 5000 },
		{ id: "express", name: "Envío Express", time: "24 horas", price: 15000 },
		{ id: "pickup", name: "Recoger en Tienda", time: "Inmediato", price: 0 },
	];

	return (
		<div className="bg-white rounded-lg shadow-lg p-6 border-l-4 border-[#8BC34A]">
			<div className="flex items-center gap-2 mb-4">
				<FontAwesomeIcon icon={faTruck} className="text-gray-600" />
				<h2 className="text-lg font-semibold text-gray-800">Método de Envío</h2>
			</div>

			{/* Dirección de envío */}
			<div className="mb-6 p-4 bg-gray-50 rounded-lg">
				<div className="flex items-center gap-2 mb-2">
					<FontAwesomeIcon icon={faMapMarkerAlt} className="text-gray-600" />
					<span className="font-medium text-gray-800">Envío a:</span>
				</div>
				<p className="text-sm text-gray-600">Juan Pérez</p>
				<p className="text-sm text-gray-600">Av. Siempre Viva 123, Lima</p>
				<button className="text-xs text-[#8BC34A] hover:text-[#7CB342] mt-2 font-medium">
					[Editar dirección]
				</button>
			</div>

			{/* Opciones de envío */}
			<div className="space-y-3">
				{shippingOptions.map((option) => (
					<label
						key={option.id}
						className="flex items-start gap-3 p-3 border border-gray-200 rounded-lg cursor-pointer hover:bg-gray-50 transition"
					>
						<input
							type="radio"
							name="shipping"
							value={option.id}
							checked={shippingMethod === option.id}
							onChange={() => setShippingMethod(option.id as any)}
							className="mt-1"
						/>
						<div className="flex-1">
							<p className="font-medium text-gray-800">{option.name}</p>
							<p className="text-sm text-gray-600">{option.time}</p>
						</div>
						<span className="font-bold text-gray-800">
							{option.price === 0 ? "Gratis" : `$${option.price.toLocaleString()}`}
						</span>
					</label>
				))}
			</div>
		</div>
	);
};

