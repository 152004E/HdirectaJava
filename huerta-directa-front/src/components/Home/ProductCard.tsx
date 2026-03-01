import { useState } from "react";
import {
  faUser,
  faInfoCircle,
  faHeart,
  faStar,
  faCartShopping,
  faBan,
  faChevronLeft,
  faChevronRight,
} from "@fortawesome/free-solid-svg-icons";
import { Button } from "../GlobalComponents/Button";
import { useCart } from "../../hooks/useCart";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";

interface Product {
  id: number;
  name: string;
  price: number;
  stock: number;
  image: string;
  category?: string;
  reviewCount?: number;
  averageRating?: number;
  images?: string[];
}

interface Props {
  product: Product;
}

const ProductCard = ({ product }: Props) => {
  const { addItem } = useCart();
  const [currentImageIndex, setCurrentImageIndex] = useState(0);
  const hasStock = product.stock && product.stock > 0;
  const isOwner = false;

  const allImages = [product.image, ...(product.images || [])];

  const handleAddToCart = () => {
    addItem({
      id: product.id,
      nombre: product.name,
      descripcion: "", 
      precio: product.price,
      cantidad: 1,
      subtotal: product.price,
      imagen: product.image,
    });
  };

  const nextImage = (e: React.MouseEvent) => {
    e.stopPropagation();
    e.preventDefault();
    setCurrentImageIndex((prev) => (prev + 1) % allImages.length);
  };

  const prevImage = (e: React.MouseEvent) => {
    e.stopPropagation();
    e.preventDefault();
    setCurrentImageIndex((prev) => (prev - 1 + allImages.length) % allImages.length);
  };

  return (
    <div className="max-w-75 w-full bg-white rounded-3xl shadow-xl overflow-hidden border border-stone-200/60 group transition-transform hover:scale-[1.02]">
      {/* Imagen Slider */}
      <div className="relative h-64 overflow-hidden group/slider">
        <img
          src={allImages[currentImageIndex]}
          alt={product.name}
          className="w-full h-full object-cover transition-transform duration-500 group-hover:scale-110"
        />

        {/* Arrows for multi-image */}
        {allImages.length > 1 && (
          <>
            <button 
              onClick={prevImage}
              className="absolute left-2 top-1/2 -translate-y-1/2 w-8 h-8 rounded-full bg-white/80 backdrop-blur-sm text-[#8bc34a] flex items-center justify-center opacity-0 group-hover/slider:opacity-100 transition-opacity hover:bg-[#8bc34a] hover:text-white shadow-md z-10 cursor-pointer"
            >
              <FontAwesomeIcon icon={faChevronLeft} className="text-xs" />
            </button>
            <button 
              onClick={nextImage}
              className="absolute right-2 top-1/2 -translate-y-1/2 w-8 h-8 rounded-full bg-white/80 backdrop-blur-sm text-[#8bc34a] flex items-center justify-center opacity-0 group-hover/slider:opacity-100 transition-opacity hover:bg-[#8bc34a] hover:text-white shadow-md z-10 cursor-pointer"
            >
              <FontAwesomeIcon icon={faChevronRight} className="text-xs" />
            </button>
            
            <div className="absolute bottom-16 left-1/2 -translate-x-1/2 flex gap-1 z-10">
              {allImages.map((_, idx) => (
                <div 
                  key={idx} 
                  className={`h-1 rounded-full transition-all duration-300 ${idx === currentImageIndex ? 'w-4 bg-[#8bc34a]' : 'w-1 bg-white/60'}`} 
                />
              ))}
            </div>
          </>
        )}

        {/* Favorito */}
        <div className="absolute top-4 right-4 z-10">
          <Button
            text=""
            iconLetf={faHeart}
            className="bg-white/80 backdrop-blur-md px-1 gap-0! py-1.5 rounded-full shadow-none text-[#8bc34a]! hover:text-white!"
          />
        </div>

        {/* Stock badge */}
        <div className="absolute bottom-4 left-4 z-10">
          {hasStock ? (
            <div className="bg-white/80 backdrop-blur-md px-3 py-1 rounded-full flex items-center gap-2 shadow text-[#8bc34a] text-xs font-semibold">
              <span className="w-2 h-2 rounded-full bg-[#8bc34a] animate-pulse"></span>
              {product.stock} disponibles
            </div>
          ) : (
            <div className="bg-white/80 backdrop-blur-md px-3 py-1 rounded-full shadow text-red-500 text-xs font-semibold">
              Sin stock
            </div>
          )}
        </div>

        {/* Categoria badge */}
        {product.category && (
          <div className="absolute bottom-4 right-4 z-10">
            <div className="bg-white/80 backdrop-blur-md px-3 py-1 rounded-full shadow text-[#8bc34a] text-[10px] font-semibold uppercase tracking-wide">
              {product.category}
            </div>
          </div>
        )}
      </div>

      {/* Contenido */}
      <div className="px-6 py-2 space-y-1">
        {/* Nombre + Stock */}
        <div className="">
          <h2 className="text-2xl font-bold text-black/80">{product.name}</h2>
        </div>

        <hr className="border-stone-100" />

        {/* Precio + Rating */}
        <div className="flex items-end justify-between">
          {/* Precio */}
          <div className="space-y-1">
            <span className="text-[10px] uppercase font-bold tracking-widest text-stone-400">
              Precio actual
            </span>

            <div className="flex items-baseline gap-1">
              <span className="text-2xl font-bold text-stone-700">
                {product.price.toLocaleString()}
              </span>
              <span className="text-[15px] font-medium text-stone-400">
                / COP
              </span>
            </div>
          </div>

          {/* Rating */}
          <div className="flex flex-col items-end gap-1">
            <div className="flex">
              {[1, 2, 3, 4, 5].map((star) => {
                const filled =
                  product.reviewCount &&
                  product.averageRating &&
                  star <= Math.round(product.averageRating);

                return (
                  <Button
                    key={star}
                    text=""
                    iconLetf={faStar}
                    className={`bg-transparent! shadow-none! p-0! gap-0! ${
                      filled ? "text-yellow-400!" : "text-gray-300!"
                    }`}
                  />
                );
              })}
            </div>

            <span className="text-[12px] text-stone-400/50 font-medium italic">
              {product.reviewCount && product.reviewCount > 0
                ? `${product.reviewCount} reseñas`
                : "Sin reseñas aún"}
            </span>
          </div>
        </div>

        {/* Botones */}
        <div className="grid grid-cols-5 gap-3 py-2">
          {/* Info */}
          <Button
            to={`/producto/${product.id}`}
            text=""
            iconLetf={faInfoCircle}
            className="col-span-1 flex items-center justify-center bg-stone-300 text-stone-600 rounded-xl hover:bg-stone-200 py-2! gap-0!"
          />

          {/* Principal */}
          {isOwner ? (
            <Button
              text="Tu producto"
              iconLetf={faUser}
              disabled
              className="col-span-4 bg-gray-500 py-3! px-6! rounded-xl flex items-center justify-center gap-3"
            />
          ) : !hasStock ? (
            <Button
              text="Sin stock"
              iconLetf={faBan}
              disabled
              className="col-span-4 bg-gray-500 py-3 px-6 rounded-xl flex items-center justify-center gap-3"
            />
          ) : (
            <Button
              text="Agregar al Carrito"
              iconLetf={faCartShopping}
              onClick={handleAddToCart}
              className="col-span-4 bg-[#8bc34a] hover:bg-[#7cb342] text-white font-bold py-3! px-2! rounded-xl flex items-center justify-center gap-1 shadow-lg shadow-[#8bc34a]/20 active:scale-95"
            />
          )}
        </div>
      </div>
    </div>
  );
};


export default ProductCard;
