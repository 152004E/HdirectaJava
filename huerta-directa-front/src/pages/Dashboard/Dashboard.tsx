import React, { useState, useEffect } from "react";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faMagnifyingGlass,
  //faBox,
  faPen,
  faTrash,
  faFileExcel,
  faFilePdf,
  faCloudArrowUp,
  faCartShopping,
  faBagShopping,
  faUser,
  //faUpload,
} from "@fortawesome/free-solid-svg-icons";
import { usePageTitle } from "../../hooks/usePageTitle";

// Reusable components
import { Button } from "../../components/GlobalComponents/Button";
import { Modal } from "../../components/GlobalComponents/Modal";
import { EditProductModal } from "../../components/Modals/EditProductModal";

interface Product {
  idProduct: number;
  nameProduct: string;
  category: string;
  price: number;
  unit: string;
  descriptionProduct: string;
  stock: number;
}

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
  const [_,setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState("");
  const [category, setCategory] = useState("");
  const [isUploadModalOpen, setIsUploadModalOpen] = useState(false);
  const [isEditModalOpen, setIsEditModalOpen] = useState(false);
  const [selectedProduct, setSelectedProduct] = useState<Product | null>(null);
  const [uploadFile, setUploadFile] = useState<File | null>(null);
  const [uploadResult, setUploadResult] = useState<{ success: boolean; message: string } | null>(null);

  useEffect(() => {
    fetchProducts();
  }, []);

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
      (category === "" || p.category === category)
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
        setUploadResult({ success: false, message: result.message || "Error al cargar productos" });
      }
    } catch (error) {
      setUploadResult({ success: false, message: "Error de conexión con el servidor" });
    }
  };

  const handleEditProduct = (product: Product) => {
    setSelectedProduct(product);
    setIsEditModalOpen(true);
  };

  const handleSaveProduct = (updatedProduct: Product) => {
    setProducts(products.map((p) => (p.idProduct === updatedProduct.idProduct ? updatedProduct : p)));
    setIsEditModalOpen(false);
  };

  return (
    <div className="w-full">
      <h1 className="text-3xl font-extrabold mb-6 text-gray-900">Dashboard</h1>

      <div className="grid grid-cols-1 xl:grid-cols-[1fr_360px] gap-8 items-start">
        <section className="w-full">
          {/* Insights Grid */}
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8 mb-12">
            {insights.map((item, idx) => {
              const colorClass = 
                item.color === 'sales' ? 'before:bg-[#8dc84b]' : 
                item.color === 'expenses' ? 'before:bg-[#8dc84b]' : 'before:bg-[#8dc84b]';
              
              return (
                <div key={idx} className={`bg-white p-10 rounded-4xl shadow-xl hover:-translate-y-1 transition-all duration-300 flex flex-col min-h-45 relative overflow-hidden ${colorClass} before:content-[''] before:absolute before:top-0 before:left-0 before:w-full before:h-2`}>
                  <div className="flex items-center justify-between mt-4">
                    <div className="flex flex-col">
                      <h3 className="text-base font-semibold text-gray-400 uppercase tracking-widest">{item.title}</h3>
                      <h1 className="text-3xl font-black mt-2 text-gray-900 tracking-tight">{item.value}</h1>
                    </div>
                    
                    <div className="relative w-16 h-16 rounded-full flex items-center justify-center font-extrabold text-sm">
                      <svg className="w-16 h-16 transform -rotate-90">
                        <circle cx="32" cy="32" r="26" fill="none" stroke="#f3f4f6" strokeWidth="7" />
                        <circle
                          cx="32"
                          cy="32"
                          r="26"
                          fill="none"
                          stroke={item.color === 'expenses' ? "#ff5252" : "#8dc84b"}
                          strokeWidth="7"
                          strokeDasharray="163"
                          strokeLinecap="round"
                          style={{ 
                            strokeDashoffset: 163 * (1 - item.percentage / 100),
                            transition: "stroke-dashoffset 1.5s ease-out"
                          }}
                        />
                      </svg>
                      <p className="absolute text-gray-800 tracking-tighter">{item.percentage}%</p>
                    </div>
                  </div>
                  <div className="flex items-center gap-2 mt-4">
                    <span className={`text-[10px] font-black px-2 py-0.5 rounded-full uppercase tracking-wider ${item.color === 'expenses' ? 'bg-red-50 text-red-500' : 'bg-green-50 text-green-600'}`}>
                      {item.color === 'expenses' ? 'Alto' : 'Estable'}
                    </span>
                    <small className="text-gray-400 font-bold text-[11px]">{item.footer}</small>
                  </div>
                </div>
              );
            })}
          </div>

          {/* Product Management */}
          <section className="bg-white p-8 rounded-3xl shadow-sm mb-8 border border-gray-100">
            <div className="flex flex-col md:flex-row justify-between items-center gap-4 mb-8">
              <h2 className="text-2xl font-bold text-gray-800">Gestión de Productos</h2>
              <div className="flex gap-3">
                <Button text="Exportar Excel" iconLetf={faFileExcel} onClick={handleExportExcel} className="bg-[#8dc84b] text-white px-4 py-2 rounded-xl" />
                <Button text="Exportar PDF" iconLetf={faFilePdf} onClick={handleExportPdf} className="bg-[#004d00] text-white px-4 py-2 rounded-xl" />
                <Button text="Carga Masiva" iconLetf={faCloudArrowUp} onClick={() => setIsUploadModalOpen(true)} className="bg-orange-500 text-white px-4 py-2 rounded-xl" />
              </div>
            </div>

            <div className="flex flex-wrap gap-4 items-center bg-gray-50/50 p-6 rounded-2xl border border-gray-100 mb-6 font-poppins">
              <div className="flex-1 min-w-[250px] relative">
                <FontAwesomeIcon icon={faMagnifyingGlass} className="absolute left-4 top-1/2 -translate-y-1/2 text-gray-400" />
                <input
                  type="text"
                  placeholder="Buscar producto..."
                  className="w-full pl-12 pr-4 py-3 border-2 border-transparent rounded-xl outline-none focus:border-[#8dc84b] bg-white transition-all shadow-sm"
                  value={searchTerm}
                  onChange={(e) => setSearchTerm(e.target.value)}
                />
              </div>
              <select
                className="p-3 bg-white border-2 border-gray-100 rounded-xl font-semibold outline-none min-w-[180px]"
                value={category}
                onChange={(e) => setCategory(e.target.value)}
              >
                <option value="">Todas las categorías</option>
                <option value="frutas">Frutas</option>
                <option value="verduras-hortalizas">Verduras y Hortalizas</option>
                <option value="lacteos">Lácteos</option>
                <option value="carnes-proteinas">Carnes y Proteinas</option>
                <option value="cereales-granos">Cereales y Granos</option>
                <option value="productos-organicos">Productos Orgánicos</option>
                <option value="miel-derivados">Miel y Derivados</option>
                <option value="bebidas">Bebidas naturales</option>
                <option value="cajas-mixtas-combos">Cajas mixtas y combos</option>
              </select>
            </div>

            <div className="overflow-x-auto">
              <table className="w-full text-left">
                <thead>
                  <tr className="border-b border-gray-100 text-gray-400 font-bold uppercase text-xs tracking-wider">
                    <th className="py-4 px-4">Producto</th>
                    <th className="py-4 px-4">Categoría</th>
                    <th className="py-4 px-4">Precio</th>
                    <th className="py-4 px-4">Stock</th>
                    <th className="py-4 px-4 text-center">Acciones</th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-gray-50">
                  {filteredProducts.map((p) => (
                    <tr key={p.idProduct} className="hover:bg-gray-50/50 transition-colors">
                      <td className="py-5 px-4 font-semibold text-gray-800">{p.nameProduct}</td>
                      <td className="py-5 px-4 text-gray-600 text-sm">{p.category}</td>
                      <td className="py-5 px-4 font-bold text-[#004d00]">${p.price.toLocaleString()}</td>
                      <td className="py-5 px-4">
                        <span className={`px-2 py-1 rounded-lg text-xs font-bold ${p.stock > 0 ? 'bg-green-50 text-green-600' : 'bg-red-50 text-red-500'}`}>
                          {p.stock} {p.unit}
                        </span>
                      </td>
                      <td className="py-5 px-4">
                        <div className="flex justify-center gap-2">
                          <button onClick={() => handleEditProduct(p)} className="w-9 h-9 rounded-lg bg-gray-50 text-[#8dc84b] hover:bg-[#8dc84b] hover:text-white transition-all flex items-center justify-center border-none cursor-pointer">
                            <FontAwesomeIcon icon={faPen} />
                          </button>
                          <button className="w-9 h-9 rounded-lg bg-gray-50 text-red-400 hover:bg-red-500 hover:text-white transition-all flex items-center justify-center border-none cursor-pointer">
                            <FontAwesomeIcon icon={faTrash} />
                          </button>
                        </div>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </section>
        </section>

        <aside className="flex flex-col gap-8">
          <div className="bg-white p-8 rounded-3xl shadow-sm border border-gray-100">
            <h2 className="text-xl font-bold mb-4 text-gray-800">Resumen</h2>
            <div className="flex flex-col gap-4">
              <div className="flex items-center gap-4 p-4 bg-gray-50/50 rounded-2xl hover:bg-gray-100 transition-all cursor-pointer">
                <div className="w-10 h-10 rounded-full bg-[#8dc84b] text-white flex items-center justify-center">
                  <FontAwesomeIcon icon={faCartShopping} />
                </div>
                <div className="flex-1">
                  <h3 className="text-xs font-bold text-gray-400">Ordenes Totales</h3>
                  <p className="font-bold text-lg text-gray-800">3,849</p>
                  
                </div>
                
              </div>
            </div>
            
          </div>
          <div className="bg-white p-8 rounded-3xl shadow-sm border border-gray-100">
            <h2 className="text-xl font-bold mb-4 text-gray-800">Usuarios</h2>
            <div className="flex flex-col gap-4">
              <div className="flex items-center gap-4 p-4 bg-gray-50/50 rounded-2xl hover:bg-gray-100 transition-all cursor-pointer">
                <div className="w-10 h-10 rounded-full bg-[#8dc84b] text-white flex items-center justify-center">
                  <FontAwesomeIcon icon={faUser} />
                </div>
                <div className="flex-1">
                  <h3 className="text-xs font-bold text-gray-400">Usuarios Totales</h3>
                  <p className="font-bold text-lg text-gray-800">100</p>
                  
                </div>
                
              </div>
            </div>
            
          </div>
          <div className="bg-white p-8 rounded-3xl shadow-sm border border-gray-100">
            <h2 className="text-xl font-bold mb-4 text-gray-800">Tus Productos</h2>
            <div className="flex flex-col gap-4">
              <div className="flex items-center gap-4 p-4 bg-gray-50/50 rounded-2xl hover:bg-gray-100 transition-all cursor-pointer">
                <div className="w-10 h-10 rounded-full bg-[#8dc84b] text-white flex items-center justify-center">
                  <FontAwesomeIcon icon={faBagShopping} />
                </div>
                <div className="flex-1">
                  <h3 className="text-xs font-bold text-gray-400">Productos Totales</h3>
                  <p className="font-bold text-lg text-gray-800">100</p>
                  
                </div>
                
              </div>
            </div>
            
          </div>
        </aside>
        
      </div>

      {/* Mass Upload Modal */}
      <Modal isOpen={isUploadModalOpen} onClose={() => setIsUploadModalOpen(false)} title="Carga Masiva" icon={faCloudArrowUp}>
        <div className="p-8">
          <form onSubmit={handleUploadSubmit} className="flex flex-col gap-6">
            <div className="flex flex-col gap-2">
              <label className="font-bold text-gray-700">Seleccionar Archivo</label>
              <input type="file" onChange={(e) => setUploadFile(e.target.files ? e.target.files[0] : null)} className="p-4 bg-gray-50 border-2 border-dashed border-gray-200 rounded-2xl text-center cursor-pointer hover:border-[#8dc84b] transition-all" />
            </div>
            <Button type="submit" text="Subir Archivo" className="w-full py-4 rounded-2xl shadow-xl shadow-[#8dc84b]/20" />
          </form>
          {uploadResult && (
            <div className={`mt-4 p-4 rounded-xl text-center font-bold ${uploadResult.success ? 'bg-green-100 text-green-700' : 'bg-red-100 text-red-700'}`}>
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
