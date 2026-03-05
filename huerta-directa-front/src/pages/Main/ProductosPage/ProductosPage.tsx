import { faBoxOpen } from "@fortawesome/free-solid-svg-icons";
import { FiltersBar } from "../../../components/Home/FiltersBar";
import { useEffect, useState } from "react";
import ProductCard from "../../../components/Home/ProductCard";

interface Product {
  id: number;
  name: string;
  price: number;
  stock: number;
  image: string;
  category?: string;
  reviewCount?: number;
  averageRating?: number;
}

export const ProductosPage = () => {
  const [products, setProducts] = useState<Product[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetch("http://localhost:8085/api/products")
      .then((res) => res.json())
      .then((data) => {
        const mappedProducts: Product[] = data.map((p: any) => ({
          id: p.idProduct,
          name: p.nameProduct,
          image: `http://localhost:8085/uploads/productos/${p.imageProduct}`,
          category: p.category,
          price: p.price,
          stock: p.stock,
          reviewCount: p.reviewCount,
          averageRating: p.averageRating,
        }));

        setProducts(mappedProducts);
        setLoading(false);
      })
      .catch((err) => {
        console.error(err);
        setLoading(false);
      });
  }, []);
  return (
    <main>
      <section className="py-16 px-10 bg-linear-to-b from-[#FEF5DC] via-white to-[#FEF5DC]  dark:bg-[#1A221C]
      dark:from-[#1A221C]
      dark:via-white/20
      dark:to-[#1A221C]">
        <div className="max-w-330 mx-auto">
          <FiltersBar title="Todos Los Productos" icon={faBoxOpen} />

          {loading ? (
            <p className="text-center mt-10">Cargando productos...</p>
          ) : (
            <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-4 gap-2 justify-items-center mt-10">
              {products.map((product) => (
                <ProductCard key={product.id} product={product} />
              ))}
            </div>
          )}
        </div>
      </section>
    </main>
  );
};
