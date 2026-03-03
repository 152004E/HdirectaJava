import React, { useState } from "react";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faUserShield, faUserPlus } from "@fortawesome/free-solid-svg-icons";
import { usePageTitle } from "../../hooks/usePageTitle";
import { Button } from "../../components/GlobalComponents/Button";

export const AdminRegister: React.FC = () => {
  usePageTitle("Registrar Administrador");

  const [formData, setFormData] = useState({
    nombreCompleto: "",
    email: "",
    password: "",
    confirmPassword: "",
    rolNivel: "Nivel 1 (Básico)",
  });

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (formData.password !== formData.confirmPassword) {
      alert("Las contraseñas no coinciden");
      return;
    }
    // Lógica para enviar el registro a la base de datos
    alert(`Administrador ${formData.nombreCompleto} registrado con éxito.`);
    
    // Resetear formulario
    setFormData({
      nombreCompleto: "",
      email: "",
      password: "",
      confirmPassword: "",
      rolNivel: "Nivel 1 (Básico)",
    });
  };

  return (
    <div className="w-full">
      <div className="flex justify-between items-center mb-8">
        <h1 className="text-3xl font-extrabold text-[#004d00]">Registrar Administrador</h1>
      </div>

      <section className="bg-white p-8 rounded-3xl shadow-sm border border-gray-100 max-w-3xl mx-auto">
        <div className="flex items-center gap-4 mb-8 pb-4 border-b border-gray-100">
          <div className="w-14 h-14 rounded-2xl bg-[#004d00] text-white flex items-center justify-center text-2xl shadow-lg shadow-green-900/20">
            <FontAwesomeIcon icon={faUserShield} />
          </div>
          <div>
            <h2 className="text-2xl font-bold text-gray-800">Nuevo Perfil Administrativo</h2>
            <p className="text-gray-500 text-sm">Crea una nueva cuenta con privilegios de gestión global.</p>
          </div>
        </div>

        <form onSubmit={handleSubmit} className="flex flex-col gap-6">
          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            <div className="flex flex-col gap-2">
              <label htmlFor="nombreCompleto" className="text-sm font-bold text-gray-700">Nombre Completo</label>
              <input
                type="text"
                id="nombreCompleto"
                name="nombreCompleto"
                value={formData.nombreCompleto}
                onChange={handleChange}
                placeholder="Ej. Juan Pérez"
                className="w-full p-4 border-2 border-gray-100 rounded-xl outline-none focus:border-[#8dc84b] transition-all bg-gray-50 focus:bg-white"
                required
              />
            </div>

            <div className="flex flex-col gap-2">
              <label htmlFor="email" className="text-sm font-bold text-gray-700">Correo Electrónico Corp.</label>
              <input
                type="email"
                id="email"
                name="email"
                value={formData.email}
                onChange={handleChange}
                placeholder="juan@huertadirecta.com"
                className="w-full p-4 border-2 border-gray-100 rounded-xl outline-none focus:border-[#8dc84b] transition-all bg-gray-50 focus:bg-white"
                required
              />
            </div>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            <div className="flex flex-col gap-2">
              <label htmlFor="password" className="text-sm font-bold text-gray-700">Contraseña Segura</label>
              <input
                type="password"
                id="password"
                name="password"
                value={formData.password}
                onChange={handleChange}
                placeholder="••••••••"
                className="w-full p-4 border-2 border-gray-100 rounded-xl outline-none focus:border-[#8dc84b] transition-all bg-gray-50 focus:bg-white"
                required
              />
            </div>

            <div className="flex flex-col gap-2">
              <label htmlFor="confirmPassword" className="text-sm font-bold text-gray-700">Confirmar Contraseña</label>
              <input
                type="password"
                id="confirmPassword"
                name="confirmPassword"
                value={formData.confirmPassword}
                onChange={handleChange}
                placeholder="••••••••"
                className="w-full p-4 border-2 border-gray-100 rounded-xl outline-none focus:border-[#8dc84b] transition-all bg-gray-50 focus:bg-white"
                required
              />
            </div>
          </div>



          <div className="flex justify-end pt-4 border-t border-gray-100">
            <Button
              text="Registrar en el Sistema"
              iconLetf={faUserPlus}
              className="bg-[#8dc84b] text-white py-4 px-8 rounded-xl text-lg w-full md:w-auto hover:bg-[#7ab13e]"
              type="submit"
            />
          </div>
        </form>
      </section>
    </div>
  );
};

export default AdminRegister;
