import { FiltersBar } from "./FiltersBar";
import ProductCard from "./ProductCard";

const mockProducts = [
  {
    id: 1,
    name: "Queso Pera",
    price: 19000,
    stock: 38,
    image: "/src/assets/logo_huerta.png",
  },
  {
    id: 2,
    name: "Leche Fresca",
    price: 4500,
    stock: 20,
    image: "/src/assets/logo_huerta.png",
  },
  {
    id: 3,
    name: "Tomate Orgánico",
    price: 3000,
    stock: 15,
    image: "/src/assets/logo_huerta.png",
  },
   {
    id: 4,
    name: "Tomate de campo",
    price: 1000,
    stock: 5,
    image: "/src/assets/logo_huerta.png",
  },
];

export const ProductsSection = () => {
  return (
    <section className="py-16 px-10   bg-linear-to-b   from-[#FEF5DC] via-white to-[#FEF5DC]">
      <div className="max-w-330 mx-auto">
      <FiltersBar title={"Nuestros Productos"} />


      <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-4 gap-2 justify-items-center mt-10">
        {mockProducts.map((product) => (
          <ProductCard key={product.id} product={product} />
        ))}
      </div>
      </div>

    </section>
  );
};