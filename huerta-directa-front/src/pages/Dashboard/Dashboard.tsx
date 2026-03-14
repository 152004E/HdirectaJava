import React, { useState, useEffect } from "react";
import {
  faCloudArrowUp,

  //faUpload,
} from "@fortawesome/free-solid-svg-icons";
import { usePageTitle } from "../../hooks/usePageTitle";

// Reusable components
import { Button } from "../../components/GlobalComponents/Button";
import { Modal } from "../../components/GlobalComponents/Modal";
import { EditProductModal } from "../../components/Modals/EditProductModal";
import { InsightsGrid } from "../../components/Dashboard/PanelDeControl/InsightsGrid";
import { DashboardAside } from "../../components/Dashboard/PanelDeControl/DashboardAside";
import { ProductManager } from "../../components/Dashboard/PanelDeControl/ProductManager";
import ProductCard from "../../components/Home/ProductCard";
import { favoriteService } from "../../services/favoriteService";
import { API_URL } from "../../config/api";

import type { Product } from "../../types/Product";

interface InsightItem {
  title: string;
  value: string;
  percentage: number;
  footer: string;
  color: string;
}

export const Dashboard: React.FC = () => {
  usePageTitle("Dashboard");

  const [products, setProducts] = useState<Product[]>([]);
  const [_, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState("");
  const [category, setCategory] = useState("");
  const [isUploadModalOpen, setIsUploadModalOpen] = useState(false);
  const [isEditModalOpen, setIsEditModalOpen] = useState(false);
  const [selectedProduct, setSelectedProduct] = useState<Product | null>(null);
  const [uploadFile, setUploadFile] = useState<File | null>(null);
  const [uploadResult, setUploadResult] = useState<{
    success: boolean;
    message: string;
  } | null>(null);
  const [viewMode, setViewMode] = useState<"list" | "grid">("list");

  interface FavoriteProduct {
    id: number;
    name: string;
    price: number;
    stock: number;
    image: string;
    category: string;
    isFavorite: boolean;
  }
  const [favoriteProducts, setFavoriteProducts] = useState<FavoriteProduct[]>([]);

  useEffect(() => {
    fetchProducts();
    fetchFavorites();
  }, []);

  const fetchFavorites = async () => {
    try {
      const data = await favoriteService.getFavorites();
      const mapped = data.map((p: { idProduct: number; nameProduct: string; price: number; stock: number; imageProduct: string; category: string }) => ({
        id: p.idProduct,
        name: p.nameProduct,
        price: p.price,
        stock: p.stock,
        image: `${API_URL}/uploads/productos/${p.imageProduct}`,
        category: p.category,
        isFavorite: true,
      }));
      setFavoriteProducts(mapped);
    } catch (error) {
      console.error("Error fetching favorites:", error);
    }
  };

  const fetchProducts = async () => {
    try {
      const response = await fetch("/api/products/mis-Productos");
      if (response.ok) {
        const data = await response.json();
        setProducts(data);
      }
    } catch (error) {
      console.error("Error fetching products:", error);
    } finally {
      setLoading(false);
    }
  };

  const insights: InsightItem[] = [
    {
      title: "Ventas",
      value: "$25,000",
      percentage: 81,
      footer: "Ultimas 24 horas",
      color: "sales",
    },
    {
      title: "Gastos",
      value: "$15,567",
      percentage: 62,
      footer: "Ultimas 24 horas",
      color: "expenses",
    },
    {
      title: "Ingresos",
      value: "$10,240",
      percentage: 31,
      footer: "Ultimas 24 horas",
      color: "income",
    },
  ];

  const filteredProducts = products.filter(
    (p) =>
      p.nameProduct.toLowerCase().includes(searchTerm.toLowerCase()) &&
      (category === "" || p.category === category),
  );

  const handleExportExcel = () => {
    const params = new URLSearchParams();
    if (searchTerm) params.append("buscar", searchTerm);
    if (category) params.append("categoria", category);
    window.location.href = `/exportar_productos_excel?${params.toString()}`;
  };

  const handleExportPdf = () => {
    const params = new URLSearchParams();
    if (searchTerm) params.append("buscar", searchTerm);
    if (category) params.append("categoria", category);
    window.location.href = `/exportar_productos_pdf?${params.toString()}`;
  };

  const handleUploadSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!uploadFile) return;

    const formData = new FormData();
    formData.append("archivo", uploadFile);

    try {
      const response = await fetch("/api/users/upload-products", {
        method: "POST",
        body: formData,
      });
      const result = await response.json();
      if (response.ok && result.success) {
        setUploadResult({ success: true, message: "¡Carga masiva exitosa!" });
        fetchProducts(); // Refresh list
        setTimeout(() => {
          setIsUploadModalOpen(false);
          setUploadResult(null);
        }, 2000);
      } else {
        setUploadResult({
          success: false,
          message: result.message || "Error al cargar productos",
        });
      }
    } catch (error) {
      setUploadResult({
        success: false,
        message: "Error de conexión con el servidor",
      });
    }
  };

  const handleEditProduct = (product: Product) => {
    setSelectedProduct(product);
    setIsEditModalOpen(true);
  };

  const handleSaveProduct = (updatedProduct: Product) => {
    setProducts(
      products.map((p) =>
        p.idProduct === updatedProduct.idProduct ? updatedProduct : p,
      ),
    );
    setIsEditModalOpen(false);
  };

  return (
    <div className="w-full">
      <h1 className="text-3xl font-extrabold mb-6 text-gray-900 dark:text-white">
        Dashboard
      </h1>

      <div className="grid grid-cols-1 xl:grid-cols-[1fr_360px] gap-8 items-start">
        <section className="w-full">
          {/* Insights Grid */}
          <InsightsGrid insights={insights} />

          {/* Product Management */}
          <ProductManager
            products={products}
            filteredProducts={filteredProducts}
            searchTerm={searchTerm}
            setSearchTerm={setSearchTerm}
            category={category}
            setCategory={setCategory}
            viewMode={viewMode}
            setViewMode={setViewMode}
            handleEditProduct={handleEditProduct}
            handleExportExcel={handleExportExcel}
            handleExportPdf={handleExportPdf}
            setIsUploadModalOpen={setIsUploadModalOpen}
          />

          {/* Favorite Products Section */}
          <div className="mt-10 bg-white dark:bg-zinc-900 rounded-3xl p-8 shadow-xl border border-stone-200/60 dark:border-zinc-700">
            <h2 className="text-2xl font-bold text-gray-900 dark:text-white mb-6">Mis productos favoritos</h2>
            {favoriteProducts.length === 0 ? (
              <p className="text-gray-500 italic">No tienes productos en favoritos aún.</p>
            ) : (
              <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 xl:grid-cols-4 gap-6">
                {favoriteProducts.map((fp) => (
                  <ProductCard key={fp.id} product={fp} />
                ))}
              </div>
            )}
          </div>
        </section>

        <DashboardAside productsCount={products.length} />
      </div>

      {/* Mass Upload Modal */}
      <Modal
        isOpen={isUploadModalOpen}
        onClose={() => setIsUploadModalOpen(false)}
        title="Carga Masiva"
        icon={faCloudArrowUp}
      >
        <div className="p-8">
          <form onSubmit={handleUploadSubmit} className="flex flex-col gap-6">
            <div className="flex flex-col gap-2">
              <label className="font-bold text-gray-700">
                Seleccionar Archivo
              </label>
              <input
                type="file"
                onChange={(e) =>
                  setUploadFile(e.target.files ? e.target.files[0] : null)
                }
                className="p-4 bg-gray-50 border-2 border-dashed border-gray-200 rounded-2xl text-center cursor-pointer hover:border-[#8dc84b] transition-all"
              />
            </div>
            <Button
              type="submit"
              text="Subir Archivo"
              className="w-full py-4 rounded-2xl shadow-xl shadow-[#8dc84b]/20"
            />
          </form>
          {uploadResult && (
            <div
              className={`mt-4 p-4 rounded-xl text-center font-bold ${uploadResult.success ? "bg-green-100 text-green-700" : "bg-red-100 text-red-700"}`}
            >
              {uploadResult.message}
            </div>
          )}
        </div>
      </Modal>

      {/* Edit Product Modal */}
      <EditProductModal
        isOpen={isEditModalOpen}
        onClose={() => setIsEditModalOpen(false)}
        product={selectedProduct}
        onSave={handleSaveProduct}
      />
    </div>
  );
};

export default Dashboard;
