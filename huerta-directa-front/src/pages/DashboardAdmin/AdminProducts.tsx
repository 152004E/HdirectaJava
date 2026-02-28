import React, { useState } from "react";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faMagnifyingGlass, faEye, faTrash, faCheck, faBan, faBorderAll, faListUl } from "@fortawesome/free-solid-svg-icons";
import { usePageTitle } from "../../hooks/usePageTitle";
import { NotifyProducerModal } from "../../components/Modals/NotifyProducerModal";

interface ProductInfo {
  id: number;
  name: string;
  producer: string;
  price: number;
  stock: number;
  status: "Aprobado" | "Pendiente" | "Rechazado";
}

export const AdminProducts: React.FC = () => {
  usePageTitle("Gestión de Productos");

  const [products] = useState<ProductInfo[]>([
    { id: 101, name: "Tomates Orgánicos", producer: "Huerta del Sol", price: 1200, stock: 50, status: "Aprobado" },
    { id: 102, name: "Lechuga Crespa", producer: "Verde Vida", price: 800, stock: 30, status: "Pendiente" },
    { id: 103, name: "Zanahorias Premium", producer: "Raíces Sanas", price: 950, stock: 0, status: "Rechazado" },
    { id: 104, name: "Manzanas Rojas", producer: "Frutales del Valle", price: 2500, stock: 120, status: "Aprobado" },
  ]);

  const [searchTerm, setSearchTerm] = useState("");
  const [viewMode, setViewMode] = useState<"list" | "grid">("list");
  const [isNotifyModalOpen, setIsNotifyModalOpen] = useState(false);
  const [selectedProduct, setSelectedProduct] = useState<ProductInfo | null>(null);

  const handleNotifyProducer = (product: ProductInfo) => {
    setSelectedProduct(product);
    setIsNotifyModalOpen(true);
  };

  const filteredProducts = products.filter(p => 
    p.name.toLowerCase().includes(searchTerm.toLowerCase()) || 
    p.producer.toLowerCase().includes(searchTerm.toLowerCase())
  );

  return (
    <div className="w-full">
      <div className="flex justify-between items-center mb-8">
        <h1 className="text-3xl font-extrabold text-[#004d00]">Gestión de Productos</h1>
      </div>

      <section className="bg-white p-8 rounded-3xl shadow-sm mb-8 border border-gray-100">
        <div className="flex flex-col md:flex-row justify-between items-start md:items-center gap-4 mb-8">
          <div className="relative flex-1 max-w-md w-full">
            <FontAwesomeIcon icon={faMagnifyingGlass} className="absolute left-4 top-1/2 -translate-y-1/2 text-gray-400" />
            <input
              type="text"
              placeholder="Buscar por producto o productor..."
              className="w-full pl-12 pr-4 py-3 border-2 border-gray-100 rounded-xl outline-none focus:border-[#8dc84b] transition-all bg-gray-50 focus:bg-white"
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
            />
          </div>
          
          <div className="flex bg-gray-100 p-1 rounded-xl">
            <button
              onClick={() => setViewMode("list")}
              className={`flex items-center gap-2 px-4 py-2 rounded-lg font-bold transition-all ${
                viewMode === "list" ? "bg-white text-[#004d00] shadow-sm" : "text-gray-500 hover:text-gray-700"
              }`}
            >
              <FontAwesomeIcon icon={faListUl} /> Lista
            </button>
            <button
              onClick={() => setViewMode("grid")}
              className={`flex items-center gap-2 px-4 py-2 rounded-lg font-bold transition-all ${
                viewMode === "grid" ? "bg-white text-[#004d00] shadow-sm" : "text-gray-500 hover:text-gray-700"
              }`}
            >
              <FontAwesomeIcon icon={faBorderAll} /> Tarjetas
            </button>
          </div>
        </div>

        {viewMode === "list" ? (
          <div className="overflow-x-auto">
            <table className="w-full">
              <thead>
              <tr className="border-b border-gray-100 text-gray-400 font-bold text-xs uppercase tracking-wider">
                <th className="py-4 px-4 text-left pb-6">Producto</th>
                <th className="py-4 px-4 text-left pb-6">Productor</th>
                <th className="py-4 px-4 text-left pb-6">Precio</th>
                <th className="py-4 px-4 text-left pb-6">Stock</th>
                <th className="py-4 px-4 text-left pb-6">Estado</th>
                <th className="py-4 px-4 text-center pb-6">Acciones</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-gray-50">
              {filteredProducts.map((product) => (
                <tr key={product.id} className="hover:bg-gray-50/50 transition-colors">
                  <td className="py-5 px-4 font-bold text-gray-800">{product.name}</td>
                  <td className="py-5 px-4 text-gray-600 font-medium">{product.producer}</td>
                  <td className="py-5 px-4 font-bold text-[#8dc84b]">${product.price.toLocaleString()}</td>
                  <td className="py-5 px-4 text-gray-600">{product.stock} un.</td>
                  <td className="py-5 px-4">
                    <span className={`px-3 py-1 rounded-full text-xs font-bold ${
                      product.status === "Aprobado" ? "bg-green-50 text-green-600" :
                      product.status === "Pendiente" ? "bg-yellow-50 text-yellow-600" :
                      "bg-red-50 text-red-600"
                    }`}>
                      {product.status}
                    </span>
                  </td>
                  <td className="py-5 px-4">
                    <div className="flex justify-center gap-2">
                      <button 
                        className="w-10 h-10 rounded-xl bg-gray-50 text-gray-400 hover:bg-blue-500 hover:text-white transition-all cursor-pointer"
                        title="Notificar al Autor"
                        onClick={() => handleNotifyProducer(product)}
                      >
                        <FontAwesomeIcon icon={faEye} />
                      </button>
                      <button className="w-10 h-10 rounded-xl bg-gray-50 text-gray-400 hover:bg-green-500 hover:text-white transition-all cursor-pointer" title="Aprobar">
                        <FontAwesomeIcon icon={faCheck} />
                      </button>
                      <button className="w-10 h-10 rounded-xl bg-gray-50 text-gray-400 hover:bg-yellow-500 hover:text-white transition-all cursor-pointer" title="Rechazar">
                        <FontAwesomeIcon icon={faBan} />
                      </button>
                      <button className="w-10 h-10 rounded-xl bg-gray-50 text-gray-400 hover:bg-red-500 hover:text-white transition-all cursor-pointer" title="Eliminar">
                        <FontAwesomeIcon icon={faTrash} />
                      </button>
                    </div>
                  </td>
                </tr>
              ))}
              {filteredProducts.length === 0 && (
                <tr>
                  <td colSpan={6} className="py-8 text-center text-gray-500">No hay productos.</td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
        ) : (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
            {filteredProducts.map((product) => (
              <div key={product.id} className="bg-white border-2 border-gray-100 rounded-3xl p-6 flex flex-col gap-4 hover:border-[#8dc84b] hover:shadow-lg hover:-translate-y-1 transition-all duration-300">
                <div className="flex justify-between items-start">
                  <span className={`px-3 py-1 rounded-full text-xs font-bold ${
                    product.status === "Aprobado" ? "bg-green-50 text-green-600" :
                    product.status === "Pendiente" ? "bg-yellow-50 text-yellow-600" :
                    "bg-red-50 text-red-600"
                  }`}>
                    {product.status}
                  </span>
                  <p className="text-gray-500 text-sm font-medium">Cod: #{product.id}</p>
                </div>
                
                <div>
                  <h3 className="text-xl font-bold text-gray-800 line-clamp-1" title={product.name}>{product.name}</h3>
                  <p className="text-gray-500 text-sm mt-1">{product.producer}</p>
                </div>
                
                <div className="flex justify-between items-end mt-auto pt-4 border-t border-gray-50">
                  <div>
                    <p className="text-xs text-gray-400 font-bold uppercase tracking-wider mb-1">Precio Unit.</p>
                    <p className="text-xl font-black text-[#8dc84b]">${product.price.toLocaleString()}</p>
                  </div>
                  <div className="text-right">
                    <p className="text-xs text-gray-400 font-bold uppercase tracking-wider mb-1">Stock Disp.</p>
                    <p className="text-lg font-bold text-gray-700">{product.stock} un.</p>
                  </div>
                </div>

                <div className="grid grid-cols-4 gap-2 mt-4">
                  <button 
                    className="h-10 rounded-xl bg-gray-50 text-gray-400 hover:bg-blue-500 hover:text-white transition-all cursor-pointer flex items-center justify-center col-span-1"
                    title="Notificar al Autor"
                    onClick={() => handleNotifyProducer(product)}
                  >
                    <FontAwesomeIcon icon={faEye} />
                  </button>
                  <button className="h-10 rounded-xl bg-gray-50 text-gray-400 hover:bg-green-500 hover:text-white transition-all cursor-pointer flex items-center justify-center col-span-1" title="Aprobar">
                    <FontAwesomeIcon icon={faCheck} />
                  </button>
                  <button className="h-10 rounded-xl bg-gray-50 text-gray-400 hover:bg-yellow-500 hover:text-white transition-all cursor-pointer flex items-center justify-center col-span-1" title="Rechazar">
                    <FontAwesomeIcon icon={faBan} />
                  </button>
                  <button className="h-10 rounded-xl bg-gray-50 text-gray-400 hover:bg-red-500 hover:text-white transition-all cursor-pointer flex items-center justify-center col-span-1" title="Eliminar">
                    <FontAwesomeIcon icon={faTrash} />
                  </button>
                </div>
              </div>
            ))}
            {filteredProducts.length === 0 && (
              <div className="col-span-full py-12 text-center border-2 border-dashed border-gray-200 rounded-3xl">
                <p className="text-gray-500 font-medium text-lg">No se encontraron productos con esos términos.</p>
              </div>
            )}
          </div>
        )}
      </section>

      {/* Modal de Notificación */}
      <NotifyProducerModal
        isOpen={isNotifyModalOpen}
        onClose={() => setIsNotifyModalOpen(false)}
        product={selectedProduct}
        onSend={(data) => console.log("Notificación a enviar:", data)}
      />
    </div>
  );
};

export default AdminProducts;
