import { useParams } from "react-router-dom";
import { useEffect, useState } from "react";
import ProductCard from "../../../components/Home/ProductCard";
import { FiltersBar } from "../../../components/Home/FiltersBar";

interface Product {
  id: number;
  name: string;
  price: number;
  stock: number;
  image: string;
  category: string;
}

const mockProducts: Product[] = [
  {
    id: 1,
    name: "Queso Pera",
    price: 19000,
    stock: 38,
    image: "/src/assets/logo_huerta.png",
    category: "lacteos",
  },
  {
    id: 2,
    name: "Leche Fresca",
    price: 4500,
    stock: 20,
    image: "/src/assets/logo_huerta.png",
    category: "lacteos",
  },
  {
    id: 3,
    name: "Tomate Orgánico",
    price: 3000,
    stock: 15,
    image: "/src/assets/logo_huerta.png",
    category: "verduras",
  },
  {
    id: 4,
    name: "Tomate de campo",
    price: 1000,
    stock: 5,
    image: "/src/assets/logo_huerta.png",
    category: "Verduras",
  },
];

const CategoryPage = () => {
  {
    /* useEffect(() => {
  fetch(`http://localhost:8085/api/products/category/${categorySlug}`)
    .then((res) => res.json())
    .then((data) => setProducts(data));
}, [categorySlug]);*/
  }
  const { categorySlug } = useParams();
  const [products, setProducts] = useState<Product[]>([]);

  useEffect(() => {
    if (categorySlug) {
      const filteredProducts = mockProducts.filter(
        (product) => product.category === categorySlug,
      );

      setProducts(filteredProducts);
    }
  }, [categorySlug]);

  return (
    <section className="bg-linear-to-b from-[#FEF5DC] via-white to-[#FEF5DC] ">
      <div className="pb-20 pt-10 max-w-7xl mx-auto px-6">
        <FiltersBar title={categorySlug ?? ""} />


        {products.length === 0 ? (
          <p className="text-center text-gray-500 mt-10">
            No hay productos en esta categoría
          </p>
        ) : (
          <div className="grid grid-cols-1 md:grid-cols-3 lg:grid-cols-4 gap-6 mt-10">
            {products.map((product) => (
              <ProductCard key={product.id} product={product} />
            ))}
          </div>
        )}
      </div>
    </section>
  );
};

export default CategoryPage;
