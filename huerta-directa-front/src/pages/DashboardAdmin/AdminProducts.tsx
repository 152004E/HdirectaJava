import React, { useState } from "react";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faMagnifyingGlass, faEye, faTrash, faCheck, faBan } from "@fortawesome/free-solid-svg-icons";
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
        <div className="mb-8 relative max-w-md">
          <FontAwesomeIcon icon={faMagnifyingGlass} className="absolute left-4 top-1/2 -translate-y-1/2 text-gray-400" />
          <input
            type="text"
            placeholder="Buscar por producto o productor..."
            className="w-full pl-12 pr-4 py-3 border-2 border-gray-100 rounded-xl outline-none focus:border-[#8dc84b] transition-all"
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
          />
        </div>

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
