import React from "react";
import { faPen } from "@fortawesome/free-solid-svg-icons";
import { Modal } from "../GlobalComponents/Modal";
import { Button } from "../GlobalComponents/Button";

interface Product {
  idProduct: number;
  nameProduct: string;
  category: string;
  price: number;
  unit: string;
  descriptionProduct: string;
  stock: number;
}

interface EditProductModalProps {
  isOpen: boolean;
  onClose: () => void;
  product: Product | null;
  onSave?: (updatedProduct: any) => void;
}

export const EditProductModal: React.FC<EditProductModalProps> = ({
  isOpen,
  onClose,
  product,
  onSave,
}) => {
  const [formData, setFormData] = React.useState({
    nameProduct: "",
    price: 0,
    category: "",
    stock: 0,
    descriptionProduct: "",
  });

  React.useEffect(() => {
    if (product) {
      setFormData({
        nameProduct: product.nameProduct,
        price: product.price,
        category: product.category,
        stock: product.stock,
        descriptionProduct: product.descriptionProduct,
      });
    }
  }, [product]);

  const handleUpdate = () => {
    if (onSave) onSave({ ...product, ...formData });
    onClose();
  };

  return (
    <Modal
      isOpen={isOpen}
      onClose={onClose}
      title="Editar Producto"
      icon={faPen}
    >
      <div className="p-6 flex flex-col gap-6">
        <p className="text-gray-500 italic">
          Actualiza la información de <b>{product?.nameProduct}</b>
        </p>

        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          <div className="flex flex-col gap-2">
            <label className="font-bold text-gray-700">Nombre del Producto</label>
            <input
              type="text"
              value={formData.nameProduct}
              onChange={(e) => setFormData({ ...formData, nameProduct: e.target.value })}
              className="p-3 border-2 border-gray-100 rounded-xl focus:border-[#8dc84b] outline-none transition-all"
            />
          </div>
          <div className="flex flex-col gap-2">
            <label className="font-bold text-gray-700">Precio</label>
            <input
              type="number"
              value={formData.price}
              onChange={(e) => setFormData({ ...formData, price: Number(e.target.value) })}
              className="p-3 border-2 border-gray-100 rounded-xl focus:border-[#8dc84b] outline-none transition-all"
            />
          </div>
          <div className="flex flex-col gap-2">
            <label className="font-bold text-gray-700">Categoría</label>
            <select
              value={formData.category}
              onChange={(e) => setFormData({ ...formData, category: e.target.value })}
              className="p-3 border-2 border-gray-100 rounded-xl focus:border-[#8dc84b] outline-none transition-all"
            >
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
          </div>
          <div className="flex flex-col gap-2">
            <label className="font-bold text-gray-700">Stock</label>
            <input
              type="number"
              value={formData.stock}
              onChange={(e) => setFormData({ ...formData, stock: Number(e.target.value) })}
              className="p-3 border-2 border-gray-100 rounded-xl focus:border-[#8dc84b] outline-none transition-all"
            />
          </div>
        </div>

        <div className="flex flex-col gap-2">
          <label className="font-bold text-gray-700">Descripción</label>
          <textarea
            value={formData.descriptionProduct}
            onChange={(e) => setFormData({ ...formData, descriptionProduct: e.target.value })}
            rows={3}
            className="p-3 border-2 border-gray-100 rounded-xl focus:border-[#8dc84b] outline-none transition-all resize-none"
          />
        </div>

        <Button
          text="Guardar Cambios"
          className="bg-[#8dc84b] text-white rounded-xl py-4 font-bold shadow-lg shadow-[#8dc84b]/20 hover:scale-[1.02] transition-all border-none"
          onClick={handleUpdate}
        />
      </div>
    </Modal>
  );
};
