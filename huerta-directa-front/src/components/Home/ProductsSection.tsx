import { useEffect, useState } from "react";
import { FiltersBar } from "./FiltersBar";
import ProductCard from "./ProductCard";

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

export const ProductsSection = () => {
  const [products, setProducts] = useState<Product[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetch("http://localhost:8085/api/products")
      .then(res => res.json())
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
      .catch(err => {
        console.error(err);
        setLoading(false);
      });
  }, []);

  return (
    <section className="py-16 px-10 bg-linear-to-b from-[#FEF5DC] via-white to-[#FEF5DC]">
      <div className="max-w-330 mx-auto">
        <FiltersBar title={"Nuestros Productos"} />

        {loading ? (
          <p className="text-center mt-10">Cargando productos...</p>
        ) : (
          <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-4 gap-2 justify-items-center mt-10">
            {products.map((product) => (
              <ProductCard
                key={product.id}
                product={product}
              />
            ))}
          </div>
        )}
      </div>
    </section>
  );
};
