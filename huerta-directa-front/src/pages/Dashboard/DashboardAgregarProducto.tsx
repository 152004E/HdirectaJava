import React from "react";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faBoxOpen, faCloudArrowUp, faUpload } from "@fortawesome/free-solid-svg-icons";
import { Button } from "../../components/GlobalComponents/Button";
import { usePageTitle } from "../../hooks/usePageTitle";

export const DashboardAgregarProducto: React.FC = () => {
  usePageTitle("Agregar Producto");

  return (
    <div className="w-full flex flex-col gap-8 animate-fadeIn max-w-4xl mx-auto">
      <div className="flex items-center gap-4">
        <div className="w-12 h-12 rounded-2xl bg-[#ffa000] text-white flex items-center justify-center shadow-lg shadow-[#ffa000]/20">
          <FontAwesomeIcon icon={faBoxOpen} size="lg" />
        </div>
        <h1 className="text-3xl font-black text-gray-800 tracking-tight">Agregar Nuevo Producto</h1>
      </div>

      <div className="bg-white p-10 rounded-4xl shadow-sm border border-gray-100">
        <form className="grid grid-cols-1 md:grid-cols-2 gap-8">
          <div className="flex flex-col gap-2">
            <label className="font-bold text-gray-700 uppercase tracking-widest text-xs">Nombre del Producto</label>
            <input type="text" className="p-4 bg-gray-50 border-none rounded-2xl outline-none focus:ring-2 focus:ring-[#8dc84b]/30 transition-all" placeholder="Ej: Tomate Orgánico" />
          </div>
          
          <div className="flex flex-col gap-2">
            <label className="font-bold text-gray-700 uppercase tracking-widest text-xs">Precio (COP)</label>
            <input type="number" className="p-4 bg-gray-50 border-none rounded-2xl outline-none focus:ring-2 focus:ring-[#8dc84b]/30 transition-all" placeholder="0" />
          </div>

          <div className="flex flex-col gap-2">
            <label className="font-bold text-gray-700 uppercase tracking-widest text-xs">Categoría</label>
            <select className="p-4 bg-gray-50 border-none rounded-2xl outline-none focus:ring-2 focus:ring-[#8dc84b]/30 transition-all appearance-none cursor-pointer">
              <option value="frutas">Frutas</option>
              <option value="verduras">Verduras</option>
              <option value="lacteos">Lácteos</option>
            </select>
          </div>

          <div className="flex flex-col gap-2">
            <label className="font-bold text-gray-700 uppercase tracking-widest text-xs">Stock Inicial</label>
            <input type="number" className="p-4 bg-gray-50 border-none rounded-2xl outline-none focus:ring-2 focus:ring-[#8dc84b]/30 transition-all" placeholder="0" />
          </div>

          <div className="flex flex-col gap-2 md:col-span-2">
            <label className="font-bold text-gray-700 uppercase tracking-widest text-xs">Descripción</label>
            <textarea rows={4} className="p-4 bg-gray-50 border-none rounded-2xl outline-none focus:ring-2 focus:ring-[#8dc84b]/30 transition-all resize-none" placeholder="Describe tu producto..."></textarea>
          </div>

          <div className="md:col-span-2">
            <label className="font-bold text-gray-700 uppercase tracking-widest text-xs block mb-4">Imagen del Producto</label>
            <div className="w-full h-48 border-4 border-dashed border-gray-100 rounded-3xl flex flex-col items-center justify-center gap-4 hover:border-[#8dc84b] hover:bg-green-50/30 transition-all cursor-pointer group">
              <FontAwesomeIcon icon={faUpload} className="text-4xl text-gray-300 group-hover:text-[#8dc84b] transition-all" />
              <p className="text-gray-400 font-bold">Cargar imagen (PNG, JPG)</p>
            </div>
          </div>

          <div className="md:col-span-2 pt-4">
            <Button text="Publicar Producto" iconLetf={faCloudArrowUp} className="w-full py-5 rounded-2xl shadow-xl shadow-[#8dc84b]/20 bg-[#8dc84b] text-white font-black text-lg" />
          </div>
        </form>
      </div>
    </div>
  );
};

export default DashboardAgregarProducto;
