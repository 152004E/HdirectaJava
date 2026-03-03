import React, { useState, useEffect } from "react";
import { faEnvelope, faPaperPlane } from "@fortawesome/free-solid-svg-icons";
import { Modal } from "../GlobalComponents/Modal";
import { Button } from "../GlobalComponents/Button";

interface ProductInfo {
  id: number;
  name: string;
  producer: string;
  price: number;
  stock: number;
  status: "Aprobado" | "Pendiente" | "Rechazado";
}

interface NotifyProducerModalProps {
  isOpen: boolean;
  onClose: () => void;
  product: ProductInfo | null;
  onSend?: (messageData: any) => void;
}

export const NotifyProducerModal: React.FC<NotifyProducerModalProps> = ({
  isOpen,
  onClose,
  product,
  onSend,
}) => {
  const [formData, setFormData] = useState({
    asunto: "",
    mensaje: "",
  });

  useEffect(() => {
    if (product) {
      setFormData({
        asunto: `Actualización sobre su producto: ${product.name}`,
        mensaje: `Estimado/a ${product.producer},\n\nNos dirigimos a usted en relación a su producto "${product.name}"...\n\nAtentamente,\nEquipo Administrativo`,
      });
    }
  }, [product]);

  const handleSend = () => {
    if (onSend) {
      onSend({ productId: product?.id, producer: product?.producer, ...formData });
    }
    // Simulate sending email
    alert(`Mensaje enviado a ${product?.producer}`);
    onClose();
  };

  return (
    <Modal
      isOpen={isOpen}
      onClose={onClose}
      title="Notificar al Productor"
      icon={faEnvelope}
    >
      <div className="flex flex-col gap-6 p-6">
        <p className="text-gray-500 italic border-l-4 border-[#8dc84b] pl-4 bg-green-50/50 py-2">
          Redactando mensaje para: <b className="text-[#004d00]">{product?.producer}</b> sobre el producto <b className="text-[#8dc84b]">{product?.name}</b>
        </p>

        <div className="flex flex-col gap-4">
          <div className="form-group flex flex-col gap-2">
            <label className="font-bold text-gray-700 text-sm uppercase tracking-wider">
              Asunto
            </label>
            <input
              type="text"
              value={formData.asunto}
              onChange={(e) => setFormData({ ...formData, asunto: e.target.value })}
              className="p-4 bg-gray-50 border-2 border-gray-100 rounded-2xl focus:border-[#8dc84b] outline-none transition-all font-medium"
              placeholder="Ej. Revisión de producto requerida"
            />
          </div>

          <div className="form-group flex flex-col gap-2">
            <label className="font-bold text-gray-700 text-sm uppercase tracking-wider">
              Mensaje
            </label>
            <textarea
              value={formData.mensaje}
              onChange={(e) => setFormData({ ...formData, mensaje: e.target.value })}
              className="p-4 bg-gray-50 border-2 border-gray-100 rounded-2xl focus:border-[#8dc84b] outline-none transition-all font-medium min-h-[200px] resize-y"
              placeholder="Escribe el mensaje aquí..."
            />
          </div>
        </div>

        <div className="flex gap-3 mt-4">
          <Button
            text="Cancelar"
            className="bg-gray-200 text-gray-700 rounded-2xl py-4 flex-1 font-bold hover:bg-gray-300 transition-all border-none"
            onClick={onClose}
          />
          <Button
            text="Enviar Correo"
            iconLetf={faPaperPlane}
            className="bg-[#8dc84b] text-white rounded-2xl py-4 flex-[2] font-bold shadow-xl shadow-[#8dc84b]/20 hover:scale-[1.02] transition-all border-none"
            onClick={handleSend}
          />
        </div>
      </div>
    </Modal>
  );
};
