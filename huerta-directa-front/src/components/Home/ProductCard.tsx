import {
  faUser,
  faInfoCircle,
  faHeart,
  faStar,
  faCartShopping,
  faBan,
} from "@fortawesome/free-solid-svg-icons";
import { Button } from "../GlobalComponents/Button";

interface Product {
  id: number;
  name: string;
  price: number;
  stock: number;
  image: string;
  etiqueta?: string;
  reviewCount?: number;
  averageRating?: number;
}

interface Props {
  product: Product;
}

const ProductCard = ({ product }: Props) => {
  const hasStock = product.stock && product.stock > 0;
  const isOwner = product.etiqueta === "MI PRODUCTO";

  return (
    <div className="bg-white rounded-2xl shadow-xl p-6 w-70 relative transition-all duration-300 hover:-translate-y-1">

      {/* Favorito */}
      <Button
        text=""
        iconLetf={faHeart}
        className="absolute top-4 right-4 bgtra text-[#8dc84b]! hover:bg-transparent hover:text-red-500! bg-transparent shadow-none p-0"
      />

      {/* Badge */}
      {product.etiqueta && (
        <div className="absolute top-4 left-4 bg-[#8dc84b] text-white text-xs font-semibold px-4 py-1 rounded-full shadow-md">
          {product.etiqueta}
        </div>
      )}

      {/* Imagen */}
      <div className="flex justify-center mt-6">
        <img
          src={product.image}
          alt={product.name}
          className="w-42.5 h-37.5 object-cover rounded-lg"
        />
      </div>

      {/* Línea */}
      <div className="border-t border-gray-300 my-4"></div>

      {/* Nombre */}
      <h3 className="text-[#8dc84b] font-semibold text-lg">
        {product.name}
      </h3>

      {/* Stock */}
      <div className="mt-1">
        {hasStock ? (
          <span className="text-green-600 text-sm font-bold">
            {product.stock} disponibles
          </span>
        ) : (
          <span className="text-red-500 text-sm font-bold">
            Sin stock
          </span>
        )}
      </div>

      {/* Precio + Rating */}
      <div className="flex justify-between items-center mt-3">
        <div className="flex items-center">
          <span className="font-bold text-lg">
            {product.price.toLocaleString()}
          </span>
          <span className="text-gray-400 text-sm ml-1">/ COP</span>
        </div>

        <div className="flex items-center text-sm gap-1">
          <Button
            text=""
            iconLetf={faStar}
            className={`bg-transparent shadow-none p-0 hover:bg-transparent ${
              product.reviewCount && product.reviewCount > 0
                ? "text-yellow-400!"
                : "text-gray-300!"
            }`}
          />

          {product.reviewCount && product.reviewCount > 0 ? (
            <span className="text-gray-700">
              {product.averageRating?.toFixed(1)} ({product.reviewCount})
            </span>
          ) : (
            <span className="text-gray-400">Sin reseñas</span>
          )}
        </div>
      </div>

      {/* Botones */}
      <div className="flex gap-2 mt-4">
        {isOwner ? (
          <Button
            text="Tu producto"
            iconLetf={faUser}
            disabled
            className="flex-1 text-xs py-2 bg-gray-400 hover:bg-gray-400 hover:scale-100"
          />
        ) : !hasStock ? (
          <Button
            text="Sin stock"
            iconLetf={faBan}
            disabled
            className="flex-1 text-xs py-2 bg-gray-500 hover:bg-gray-500 hover:scale-100"
          />
        ) : (
          <Button
            text="Agregar"
            iconLetf={faCartShopping}
            className="flex-1 text-xs py-2"
          />
        )}

        <Button
          to={`/producto/${product.id}`}
          text="Info"
          iconRight={faInfoCircle}
          className="flex-1 text-xs py-2 bg-gray-300 text-gray-800 hover:bg-gray-400"
        />
      </div>
    </div>
  );
};

export default ProductCard;