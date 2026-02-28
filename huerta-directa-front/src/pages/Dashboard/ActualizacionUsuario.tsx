import React from "react";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faGear, faUser, faEnvelope, faLock, faCamera } from "@fortawesome/free-solid-svg-icons";
import { Button } from "../../components/GlobalComponents/Button";
import { usePageTitle } from "../../hooks/usePageTitle";

export const ActualizacionUsuario: React.FC = () => {
  usePageTitle("Mi Perfil");

  return (
    <div className="w-full flex flex-col gap-8 animate-fadeIn max-w-4xl mx-auto">
      <div className="flex items-center gap-4">
        <div className="w-12 h-12 rounded-2xl bg-gray-800 text-white flex items-center justify-center shadow-lg">
          <FontAwesomeIcon icon={faGear} size="lg" />
        </div>
        <h1 className="text-3xl font-black text-gray-800 tracking-tight">Mi Perfil</h1>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-[1fr_2fr] gap-8">
        {/* Profile Card */}
        <div className="flex flex-col gap-6">
          <div className="bg-white p-8 rounded-4xl shadow-sm border border-gray-100 flex flex-col items-center text-center gap-4">
            <div className="relative group">
              <div className="w-32 h-32 rounded-3xl bg-gray-100 flex items-center justify-center text-gray-300 relative overflow-hidden">
                <FontAwesomeIcon icon={faUser} size="4x" />
              </div>
              <button className="absolute -bottom-2 -right-2 w-10 h-10 bg-[#8dc84b] text-white rounded-xl flex items-center justify-center shadow-lg hover:scale-110 transition-all">
                <FontAwesomeIcon icon={faCamera} size="sm" />
              </button>
            </div>
            <div>
              <h3 className="text-xl font-bold text-gray-800">Productor Agrícola</h3>
              <p className="text-sm text-[#8dc84b] font-bold uppercase tracking-tight">Activo</p>
            </div>
            <div className="w-full pt-4 border-t border-gray-50 text-left flex flex-col gap-3">
              <div className="flex items-center gap-3 text-gray-500">
                <FontAwesomeIcon icon={faEnvelope} className="w-4" />
                <span className="text-sm">productor@ejemplo.com</span>
              </div>
            </div>
          </div>
        </div>

        {/* Update Form */}
        <div className="bg-white p-10 rounded-4xl shadow-sm border border-gray-100 flex flex-col gap-8">
          <h2 className="text-xl font-black text-gray-800 border-b border-gray-50 pb-4">Actualizar Información</h2>
          
          <form className="flex flex-col gap-6">
            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
              <div className="flex flex-col gap-2">
                <label className="font-bold text-gray-700 uppercase tracking-widest text-[10px]">Nombre Completo</label>
                <div className="relative">
                  <FontAwesomeIcon icon={faUser} className="absolute left-4 top-1/2 -translate-y-1/2 text-gray-300" />
                  <input type="text" className="w-full pl-12 pr-4 py-4 bg-gray-50 border-none rounded-2xl outline-none focus:ring-2 focus:ring-[#8dc84b]/30 transition-all" defaultValue="Juan Pérez" />
                </div>
              </div>
              <div className="flex flex-col gap-2">
                <label className="font-bold text-gray-700 uppercase tracking-widest text-[10px]">Correo Electrónico</label>
                <div className="relative">
                  <FontAwesomeIcon icon={faEnvelope} className="absolute left-4 top-1/2 -translate-y-1/2 text-gray-300" />
                  <input type="email" className="w-full pl-12 pr-4 py-4 bg-gray-50 border-none rounded-2xl outline-none focus:ring-2 focus:ring-[#8dc84b]/30 transition-all" defaultValue="productor@ejemplo.com" />
                </div>
              </div>
            </div>

            <div className="flex flex-col gap-2 pt-4 border-t border-gray-50">
              <h3 className="font-bold text-gray-400 uppercase tracking-widest text-[10px] mb-2">Cambiar Contraseña</h3>
              <div className="relative">
                <FontAwesomeIcon icon={faLock} className="absolute left-4 top-1/2 -translate-y-1/2 text-gray-300" />
                <input type="password" placeholder="Contraseña actual" className="w-full pl-12 pr-4 py-4 bg-gray-50 border-none rounded-2xl outline-none focus:ring-2 focus:ring-red-100 transition-all" />
              </div>
              <div className="relative mt-2">
                <FontAwesomeIcon icon={faLock} className="absolute left-4 top-1/2 -translate-y-1/2 text-gray-300" />
                <input type="password" placeholder="Nueva contraseña" className="w-full pl-12 pr-4 py-4 bg-gray-50 border-none rounded-2xl outline-none focus:ring-2 focus:ring-[#8dc84b]/30 transition-all" />
              </div>
            </div>

            <Button text="Guardar Cambios" className="mt-4 bg-[#8dc84b] text-white py-5 rounded-2xl font-black shadow-xl shadow-[#8dc84b]/20" />
          </form>
        </div>
      </div>
    </div>
  );
};

export default ActualizacionUsuario;
