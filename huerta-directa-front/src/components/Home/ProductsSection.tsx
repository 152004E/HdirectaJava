import { useEffect, useState } from "react";
import { FiltersBar } from "./FiltersBar";
import ProductCard from "./ProductCard";

interface Product {
  idProduct: number;
  nameProduct: string;
  price: number;
  stock: number;
  imageProduct: string;
}

export const ProductsSection = () => {
  const [products, setProducts] = useState<Product[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetch("http://localhost:8085/api/products")
      .then((res) => {
        if (!res.ok) {
          throw new Error("Error al obtener productos");
        }
        return res.json();
      })
      .then((data) => {
        setProducts(data);
        setLoading(false);
      })
      .catch((error) => {
        console.error("Error:", error);
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
                key={product.idProduct}
                product={{
                  id: product.idProduct,
                  name: product.nameProduct,
                  price: product.price,
                  stock: product.stock,
                  image: `http://localhost:8085/uploads/productos/${product.imageProduct}`,
                }}
              />
            ))}
          </div>
        )}
      </div>
    </section>
  );
};
