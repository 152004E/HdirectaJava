import React, { useState } from "react";
import { faSave, faRotateLeft } from "@fortawesome/free-solid-svg-icons";
import { usePageTitle } from "../../hooks/usePageTitle";
import { Button } from "../../components/GlobalComponents/Button";

export const AdminConfig: React.FC = () => {
  usePageTitle("Configuración del Sistema");

  const [commissionRate, setCommissionRate] = useState("5.0");
  const [maintenanceMode, setMaintenanceMode] = useState(false);
  const [autoApproveProducers, setAutoApproveProducers] = useState(false);

  // Perfil del Administrador
  const [adminProfile, setAdminProfile] = useState({
    nombreCompleto: "Administrador Global",
    email: "admin@huertadirecta.com",
    password: "",
    celular: "+57 300 123 4567",
    direccion: "Carrera 45 # 104-56, Bogotá"
  });

  const handleProfileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setAdminProfile({
      ...adminProfile,
      [e.target.name]: e.target.value
    });
  };

  const handleSave = () => {
    // Simulando guardado
    alert("Configuraciones guardadas con éxito.");
  };

  return (
    <div className="w-full">
      <div className="flex justify-between items-center mb-8">
        <h1 className="text-3xl font-extrabold text-[#004d00]">Configuración Global</h1>
        <div className="flex gap-4">
          <Button text="Restaurar" iconLetf={faRotateLeft} className="bg-gray-200 text-gray-600 rounded-xl py-2 px-4 shadow-none hover:bg-gray-300" />
          <Button text="Guardar Cambios" iconLetf={faSave} className="bg-[#8dc84b] text-white rounded-xl py-2 px-4" onClick={handleSave} />
        </div>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
        <section className="bg-white p-8 rounded-3xl shadow-sm border border-gray-100 flex flex-col gap-6">
          <h2 className="text-xl font-bold text-gray-800 border-b border-gray-100 pb-4">Finanzas</h2>
          
          <div>
            <label className="block text-sm font-bold text-gray-600 mb-2">Comisión Plataforma (%)</label>
            <input 
              type="number" 
              step="0.1"
              value={commissionRate}
              onChange={(e) => setCommissionRate(e.target.value)}
              className="w-full p-4 border-2 border-gray-100 rounded-xl outline-none focus:border-[#8dc84b] transition-all"
            />
            <p className="text-xs text-gray-400 mt-2">Porcentaje que se cobra a los productores por cada venta.</p>
          </div>
        </section>

        <section className="bg-white p-8 rounded-3xl shadow-sm border border-gray-100 flex flex-col gap-6">
          <h2 className="text-xl font-bold text-gray-800 border-b border-gray-100 pb-4">General</h2>
          
          <div className="flex items-center justify-between">
            <div>
              <p className="font-bold text-gray-700">Modo Mantenimiento</p>
              <p className="text-sm text-gray-400">Desactiva el acceso a los usuarios no administradores.</p>
            </div>
            <label className="relative inline-flex items-center cursor-pointer">
              <input 
                type="checkbox" 
                className="sr-only peer" 
                checked={maintenanceMode}
                onChange={() => setMaintenanceMode(!maintenanceMode)}
              />
              <div className="w-11 h-6 bg-gray-200 peer-focus:outline-none rounded-full peer peer-checked:after:translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-[2px] after:left-[2px] after:bg-white after:border-gray-300 after:border after:rounded-full after:h-5 after:w-5 after:transition-all peer-checked:bg-[#004d00]"></div>
            </label>
          </div>

          <div className="flex items-center justify-between pt-4 border-t border-gray-50">
            <div>
              <p className="font-bold text-gray-700">Auto-aprobar Productores</p>
              <p className="text-sm text-gray-400">Las solicitudes de nuevos productores no requerirán revisión manual.</p>
            </div>
            <label className="relative inline-flex items-center cursor-pointer">
              <input 
                type="checkbox" 
                className="sr-only peer" 
                checked={autoApproveProducers}
                onChange={() => setAutoApproveProducers(!autoApproveProducers)}
              />
              <div className="w-11 h-6 bg-gray-200 peer-focus:outline-none rounded-full peer peer-checked:after:translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-[2px] after:left-[2px] after:bg-white after:border-gray-300 after:border after:rounded-full after:h-5 after:w-5 after:transition-all peer-checked:bg-[#8dc84b]"></div>
            </label>
          </div>
        </section>
      </div>

      <div className="mt-8">
        <section className="bg-white p-8 rounded-3xl shadow-sm border border-gray-100 flex flex-col gap-6 w-full">
          <h2 className="text-xl font-bold text-gray-800 border-b border-gray-100 pb-4">Mi Perfil Administrativo</h2>
          
          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            <div className="flex flex-col gap-2">
              <label htmlFor="nombreCompleto" className="text-sm font-bold text-gray-600">Nombre Completo</label>
              <input 
                type="text" 
                id="nombreCompleto"
                name="nombreCompleto"
                value={adminProfile.nombreCompleto}
                onChange={handleProfileChange}
                className="w-full p-4 border-2 border-gray-100 rounded-xl outline-none focus:border-[#8dc84b] transition-all bg-gray-50 focus:bg-white"
              />
            </div>

            <div className="flex flex-col gap-2">
              <label htmlFor="email" className="text-sm font-bold text-gray-600">Correo Electrónico</label>
              <input 
                type="email" 
                id="email"
                name="email"
                value={adminProfile.email}
                onChange={handleProfileChange}
                className="w-full p-4 border-2 border-gray-100 rounded-xl outline-none focus:border-[#8dc84b] transition-all bg-gray-50 focus:bg-white"
              />
            </div>

            <div className="flex flex-col gap-2">
              <label htmlFor="password" className="text-sm font-bold text-gray-600">Nueva Contraseña</label>
              <input 
                type="password" 
                id="password"
                name="password"
                placeholder="Deja en blanco para no cambiar"
                value={adminProfile.password}
                onChange={handleProfileChange}
                className="w-full p-4 border-2 border-gray-100 rounded-xl outline-none focus:border-[#8dc84b] transition-all bg-gray-50 focus:bg-white"
              />
            </div>

            <div className="flex flex-col gap-2">
              <label htmlFor="celular" className="text-sm font-bold text-gray-600">Número de Celular</label>
              <input 
                type="tel" 
                id="celular"
                name="celular"
                value={adminProfile.celular}
                onChange={handleProfileChange}
                className="w-full p-4 border-2 border-gray-100 rounded-xl outline-none focus:border-[#8dc84b] transition-all bg-gray-50 focus:bg-white"
              />
            </div>

            <div className="flex flex-col gap-2 md:col-span-2">
              <label htmlFor="direccion" className="text-sm font-bold text-gray-600">Dirección de Vivienda</label>
              <input 
                type="text" 
                id="direccion"
                name="direccion"
                value={adminProfile.direccion}
                onChange={handleProfileChange}
                className="w-full p-4 border-2 border-gray-100 rounded-xl outline-none focus:border-[#8dc84b] transition-all bg-gray-50 focus:bg-white"
              />
            </div>
          </div>
        </section>
      </div>
    </div>
  );
};

export default AdminConfig;
