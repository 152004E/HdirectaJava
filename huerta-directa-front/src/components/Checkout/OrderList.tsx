import { useCart } from "../../hooks/useCart";
import { OrderItem } from "./OrderItem";

export const OrderList = () => {
	const { items } = useCart();

	if (items.length === 0) {
		return (
			<div className="text-center py-8">
				<p className="text-gray-600">No hay productos en el carrito</p>
			</div>
		);
	}

	return (
		<div className="space-y-4">
			{items.map((item) => (
				<OrderItem key={item.id} item={item} />
			))}
		</div>
	);
};
