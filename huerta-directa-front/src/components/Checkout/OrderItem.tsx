import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faTrash, faPen } from "@fortawesome/free-solid-svg-icons";
import { useCart } from "../../hooks/useCart";

interface Props {
	item: {
		id: number;
		nombre: string;
		precio: number;
		cantidad: number;
		subtotal: number;
		imagen?: string;
	};
}

export const OrderItem = ({ item }: Props) => {
	const { removeItem, updateQuantity } = useCart();

	const formatPrice = (price: number) => {
		return new Intl.NumberFormat("es-CO", {
			style: "currency",
			currency: "COP",
			minimumFractionDigits: 0,
		}).format(price);
	};

	return (
		<div className="border border-gray-200 rounded-lg p-4 hover:border-[#8BC34A] transition-colors">
			<div className="flex gap-4">
				{/* Imagen placeholder */}
				<div className="w-20 h-20 bg-gray-200 rounded-lg flex items-center justify-center flex-shrink-0">
					<span className="text-gray-400 text-sm">Imagen</span>
				</div>

				{/* Info */}
				<div className="flex-1">
					<h3 className="font-semibold text-gray-800">{item.nombre}</h3>
					<p className="text-sm text-gray-600 mt-1">{formatPrice(item.precio)} c/u</p>

					{/* Cantidad */}
					<div className="mt-2 flex items-center gap-2">
						<button
							onClick={() => updateQuantity(item.id, item.cantidad - 1)}
							className="px-2 py-1 bg-gray-200 rounded hover:bg-gray-300 text-sm"
						>
							-
						</button>
						<span className="w-6 text-center text-sm">{item.cantidad}</span>
						<button
							onClick={() => updateQuantity(item.id, item.cantidad + 1)}
							className="px-2 py-1 bg-gray-200 rounded hover:bg-gray-300 text-sm"
						>
							+
						</button>
					</div>
				</div>

				{/* Precio + Acciones */}
				<div className="text-right flex flex-col items-end justify-between">
					<p className="font-bold text-lg text-[#8BC34A]">{formatPrice(item.subtotal)}</p>

					<div className="flex gap-2">
						<button className="p-2 hover:bg-gray-100 rounded transition">
							<FontAwesomeIcon icon={faPen} className="text-blue-500" />
						</button>
						<button
							onClick={() => removeItem(item.id)}
							className="p-2 hover:bg-gray-100 rounded transition"
						>
							<FontAwesomeIcon icon={faTrash} className="text-red-500" />
						</button>
					</div>
				</div>
			</div>
		</div>
	);
};
