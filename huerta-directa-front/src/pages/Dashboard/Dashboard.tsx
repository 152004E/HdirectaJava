import React, { useState, useEffect } from "react";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faMagnifyingGlass,
  faBox,
  faPen,
  faTrash,
  faFileExcel,
  faFilePdf,
  faCloudArrowUp,
  faCartShopping,
  faBagShopping,
  faUser,
  faUpload,
} from "@fortawesome/free-solid-svg-icons";
import { usePageTitle } from "../../hooks/usePageTitle";

// Reusable components
import { Button } from "../../components/GlobalComponents/Button";
import { Modal } from "../../components/GlobalComponents/Modal";

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
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState("");
  const [category, setCategory] = useState("");
  const [isUploadModalOpen, setIsUploadModalOpen] = useState(false);
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

  return (
    <div className="w-full">
      <h1 className="text-3xl font-extrabold mb-6 text-gray-900">Dashboard</h1>

      <div className="grid grid-cols-1 xl:grid-cols-[1fr_360px] gap-8 items-start">
        <section className="w-full">
          {/* Insights Grid */}
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8 mb-12">
            {insights.map((item, idx) => {
              const dashOffset = 226 * (1 - item.percentage / 100);
              const colorClass = 
                item.color === 'sales' ? 'before:bg-[#8dc84b]' : 
                item.color === 'expenses' ? 'before:bg-[#8dc84b]' : 'before:bg-[#8dc84b]';
              
              return (
                <div key={idx} className={`bg-white p-10 rounded-[2rem] shadow-xl hover:-translate-y-1 transition-all duration-300 flex flex-col min-h-[180px] relative overflow-hidden ${colorClass} before:content-[''] before:absolute before:top-0 before:left-0 before:w-full before:h-2`}>
                  <div className="flex items-center justify-between mt-4">
                    <div className="flex flex-col">
                      <h3 className="text-base font-semibold text-gray-500 uppercase tracking-wider">{item.title}</h3>
                      <h1 className="text-3xl font-bold mt-2 text-gray-900">{item.value}</h1>
                    </div>
                    <div className="relative w-[92px] h-[92px] rounded-full flex items-center justify-center font-semibold">
                      <svg className="w-28 h-28 transform -rotate-90">
                        <circle
                          cx="56"
                          cy="56"
                          r="36"
                          fill="none"
                          stroke="#eee"
                          strokeWidth="12"
                          className="origin-center"
                        />
                        <circle
                          cx="56"
                          cy="56"
                          r="36"
                          fill="none"
                          stroke="#004d00"
                          strokeWidth="12"
                          strokeDasharray="226"
                          strokeLinecap="round"
                          style={{ strokeDashoffset: dashOffset }}
                          className="transition-all duration-500"
                        />
                      </svg>
                      <p className="absolute text-gray-800">{item.percentage}%</p>
                    </div>
                  </div>
                  <small className="text-gray-400 mt-4 font-medium">{item.footer}</small>
                </div>
              );
            })}
          </div>

          {/* Product Management */}
          <section className="bg-white p-8 rounded-[1.5rem] shadow-sm mb-8">
            <h2 className="text-2xl font-bold mb-6 text-gray-800">Gestión de Productos</h2>

            <div className="bg-[#8dc84b] text-white p-6 rounded-2xl flex items-center justify-center gap-3 font-bold text-xl shadow-lg shadow-[#8dc84b]/30 mb-8">
              <FontAwesomeIcon icon={faBox} />
              <span>Total de Productos: {products.length}</span>
            </div>

            {/* Filters */}
            <div className="flex flex-wrap gap-4 items-center bg-gray-50/50 p-6 rounded-2xl border border-gray-100 mb-6">
              <div className="flex-1 min-w-[250px]">
                <input
                  type="text"
                  placeholder="Buscar producto..."
                  className="w-full p-4 border-2 border-[#8dc84b] rounded-xl outline-none focus:shadow-lg focus:shadow-[#8dc84b]/20 transition-all text-lg"
                  value={searchTerm}
                  onChange={(e) => setSearchTerm(e.target.value)}
                />
              </div>
              <div className="flex gap-3 items-center">
                <select
                  className="p-4 bg-[#8dc84b] text-white border-none rounded-xl font-semibold cursor-pointer outline-none min-w-[180px] appearance-none"
                  value={category}
                  onChange={(e) => setCategory(e.target.value)}
                >
                  <option value="">Por categoría</option>
                  <option value="frutas">Frutas</option>
                  <option value="verduras-hortalizas">Verduras y Hortalizas</option>
                  <option value="lacteos">Lácteos</option>
                  <option value="carnes-proteinas">Carnes y Proteínas</option>
                  <option value="cereales-granos">Cereales y Granos</option>
                  <option value="legumbres-secas">Legumbres Secas</option>
                  <option value="productos-organicos">Productos Orgánicos</option>
                  <option value="hierbas-especias">Hierbas y Especias</option>
                  <option value="miel-derivados">Miel y Derivados</option>
                  <option value="procesados-artesanales">Procesados Artesanales</option>
                  <option value="bebidas-naturales">Bebidas Naturales</option>
                  <option value="plantas-semillas">Plantas y Semillas</option>
                  <option value="cajas-combos">Cajas Mixtas o Combos</option>
                </select>
                <Button
                  text="Consultar"
                  iconLetf={faMagnifyingGlass}
                  className="bg-[#8dc84b] hover:bg-[#689f38] px-6 py-4 rounded-xl font-bold transition-all hover:scale-105"
                />
              </div>
            </div>

            {/* Product Table */}
            <div className="bg-white rounded-3xl p-6 shadow-sm overflow-x-auto">
              {loading ? (
                <p className="text-center py-10 text-gray-500 font-medium">Cargando productos...</p>
              ) : (
                <table className="w-full border-collapse text-left">
                  <thead>
                    <tr className="border-b-2 border-gray-50 text-gray-400 font-bold uppercase text-[0.8rem]">
                      <th className="py-6 px-4">Nombre</th>
                      <th className="py-6 px-4">Categoría</th>
                      <th className="py-6 px-4">Precio</th>
                      <th className="py-6 px-4">Unidad</th>
                      <th className="py-6 px-4">Descripción</th>
                      <th className="py-6 px-4">Stock</th>
                      <th className="py-6 px-4">Acciones</th>
                    </tr>
                  </thead>
                  <tbody className="divide-y divide-gray-50">
                    {filteredProducts.map((p) => (
                      <tr key={p.idProduct} className="hover:bg-gray-50/50 transition-colors">
                        <td className="py-6 px-4 font-semibold text-gray-800">{p.nameProduct}</td>
                        <td className="py-6 px-4 text-gray-600">{p.category}</td>
                        <td className="py-6 px-4 font-bold text-[#004d00]">${p.price.toLocaleString()}</td>
                        <td className="py-6 px-4 text-gray-500">{p.unit}</td>
                        <td className="py-6 px-4 text-gray-500 max-w-xs truncate">
                          {p.descriptionProduct.length > 50 
                            ? p.descriptionProduct.substring(0, 50) + "..." 
                            : p.descriptionProduct}
                        </td>
                        <td className="py-6 px-4">
                          <span
                            className={`px-3 py-1.5 rounded-lg text-sm font-medium ${
                              p.stock > 0 ? "bg-green-50 text-green-700" : "bg-red-50 text-red-700"
                            }`}
                          >
                            {p.stock > 0 ? `${p.stock} disponibles` : "Sin stock"}
                          </span>
                        </td>
                        <td className="py-6 px-4">
                          <div className="flex gap-2">
                            <button className="w-10 h-10 rounded-xl flex items-center justify-center bg-[#8dc84b] text-white hover:scale-110 transition-transform">
                              <FontAwesomeIcon icon={faPen} />
                            </button>
                            <button className="w-10 h-10 rounded-xl flex items-center justify-center bg-[#004d00] text-white hover:scale-110 transition-transform">
                              <FontAwesomeIcon icon={faTrash} />
                            </button>
                          </div>
                        </td>
                      </tr>
                    ))}
                    {filteredProducts.length === 0 && (
                      <tr>
                        <td colSpan={7} className="text-center py-20 text-gray-400 text-lg italic">
                          No se encontraron productos
                        </td>
                      </tr>
                    )}
                  </tbody>
                </table>
              )}
            </div>

            {/* Export Actions */}
            <div className="flex flex-wrap gap-4 mt-8 justify-center">
              <Button 
                text="Exportar a Excel" 
                iconLetf={faFileExcel} 
                onClick={handleExportExcel} 
                className="bg-[#8dc84b] hover:bg-[#689f38] px-6 py-4 rounded-xl text-white font-bold" 
              />
              <Button 
                text="Exportar a PDF" 
                iconLetf={faFilePdf} 
                onClick={handleExportPdf} 
                className="bg-[#004d00] hover:bg-[#003300] px-6 py-4 rounded-xl text-white font-bold" 
              />
              <Button 
                text="Carga Masiva" 
                iconLetf={faCloudArrowUp} 
                onClick={() => setIsUploadModalOpen(true)} 
                className="bg-[#ffa000] hover:bg-[#e69000] px-6 py-4 rounded-xl text-white font-bold" 
              />
            </div>
          </section>
        </section>

        {/* Right Section Widgets */}
        <aside className="flex flex-col gap-8 self-end xl:self-start">
          <div className="bg-white p-8 rounded-[1.5rem] shadow-sm">
            <h2 className="text-xl font-bold mb-4 text-gray-800 border-b border-gray-50 pb-2">Actualizaciones</h2>
            <div className="flex flex-col gap-4">
              <div className="flex gap-4">
                <div className="flex-1">
                  <p className="text-[0.95rem] text-gray-700">
                    <b className="font-bold">Sistema</b> funcionando correctamente
                  </p>
                  <small className="text-gray-400">Hace 1 hora</small>
                </div>
              </div>
            </div>
          </div>

          <div className="bg-white p-8 rounded-[1.5rem] shadow-sm">
            <h2 className="text-xl font-bold mb-4 text-gray-800 border-b border-gray-50 pb-2">Analíticas</h2>
            <div className="flex flex-col gap-3">
              <div className="flex items-center gap-4 p-4 bg-gray-50/50 rounded-2xl hover:bg-gray-50 transition-all hover:scale-[1.02]">
                <div className="w-10 h-10 rounded-full bg-[#8dc84b] text-white flex items-center justify-center">
                  <FontAwesomeIcon icon={faCartShopping} />
                </div>
                <div className="flex-1 flex justify-between items-center">
                  <div>
                    <h3 className="text-xs font-bold uppercase tracking-tight text-gray-500">Ordenes</h3>
                    <small className="text-gray-400">Ultimas 24 horas</small>
                  </div>
                  <div className="text-right">
                    <span className="text-green-600 font-bold block">+39%</span>
                    <p className="font-bold text-lg text-gray-800">3849</p>
                  </div>
                </div>
              </div>
              
              <div className="flex items-center gap-4 p-4 bg-gray-50/50 rounded-2xl hover:bg-gray-50 transition-all hover:scale-[1.02]">
                <div className="w-10 h-10 rounded-full bg-[#004d00] text-white flex items-center justify-center">
                  <FontAwesomeIcon icon={faBagShopping} />
                </div>
                <div className="flex-1 flex justify-between items-center">
                  <div>
                    <h3 className="text-xs font-bold uppercase tracking-tight text-gray-500">Offline Ordenes</h3>
                    <small className="text-gray-400">Ultimas 24 horas</small>
                  </div>
                  <div className="text-right">
                    <span className="text-red-600 font-bold block">-17%</span>
                    <p className="font-bold text-lg text-gray-800">1100</p>
                  </div>
                </div>
              </div>

              <div className="flex items-center gap-4 p-4 bg-gray-50/50 rounded-2xl hover:bg-gray-50 transition-all hover:scale-[1.02]">
                <div className="w-10 h-10 rounded-full bg-[#8dc84b] text-white flex items-center justify-center">
                  <FontAwesomeIcon icon={faUser} />
                </div>
                <div className="flex-1 flex justify-between items-center">
                  <div>
                    <h3 className="text-xs font-bold uppercase tracking-tight text-gray-500">Nuevos Clientes</h3>
                    <small className="text-gray-400">Ultimas 24 horas</small>
                  </div>
                  <div className="text-right">
                    <span className="text-green-600 font-bold block">+67%</span>
                    <p className="font-bold text-lg text-gray-800">849</p>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </aside>
      </div>

      {/* Mass Upload Modal */}
      <Modal
        isOpen={isUploadModalOpen}
        onClose={() => setIsUploadModalOpen(false)}
        title="Carga Masiva de Productos"
        icon={faUpload}
      >
        <form onSubmit={handleUploadSubmit}>
          <div className="form-group">
            <label>Seleccionar archivo de productos:</label>
            <input 
              type="file" 
              accept=".csv,.xlsx,.xls" 
              required 
              onChange={(e) => setUploadFile(e.target.files ? e.target.files[0] : null)}
            />
            <div className="help-text">
              💡 <strong>Formato esperado:</strong><br />
              📋 <em>Nombre | Precio | Categoría | Unidad | Descripción | Imagen | Stock</em><br />
              📁 Formatos: CSV, Excel (.xlsx, .xls)<br />
              ⚠️ <strong>Nota:</strong> El campo Stock es opcional.
            </div>
          </div>
          <Button type="submit" text="Cargar Productos" className="btn-send-email" />
        </form>
        {uploadResult && (
          <div className={`result-message active ${uploadResult.success ? "success" : "error"}`}>
            {uploadResult.message}
          </div>
        )}
      </Modal>
    </div>
  );
};

export default Dashboard;
