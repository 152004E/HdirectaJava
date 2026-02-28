import React, { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faMagnifyingGlass,
  faUsers,
  faPen,
  faGear,
  faFileExcel,
  faFilePdf,
  faUserSlash,
  faUserCheck,
  faChartPie,
  faBoxesStacked,
} from "@fortawesome/free-solid-svg-icons";
import { usePageTitle } from "../../hooks/usePageTitle";

// Reusable components
import { Button } from "../../components/GlobalComponents/Button";
import { EditUserModal } from "../../components/Modals/EditUserModal";

interface UserInfo {
  id: number;
  fullName: string;
  email: string;
  role: string;
  status: "Active" | "Inactive";
  registrationDate: string;
}

interface AdminInsightItem {
  title: string;
  value: string;
  percentage: number;
  footer: string;
  color: "primary" | "secondary" | "accent";
  icon: any;
}

export const DashboardAdmin: React.FC = () => {
  usePageTitle("Admin Dashboard");

  const [users, setUsers] = useState<UserInfo[]>([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState("");
  const [isEditModalOpen, setIsEditModalOpen] = useState(false);
  const [selectedUser, setSelectedUser] = useState<UserInfo | null>(null);

  useEffect(() => {
    // Simulating API fetch for users
    setTimeout(() => {
      setUsers([
        { id: 1, fullName: "Juan Pérez", email: "juan@example.com", role: "Cliente", status: "Active", registrationDate: "2024-02-01" },
        { id: 2, fullName: "María García", email: "maria@example.com", role: "Productor", status: "Active", registrationDate: "2024-02-15" },
        { id: 3, fullName: "Carlos López", email: "carlos@example.com", role: "Cliente", status: "Inactive", registrationDate: "2024-01-20" },
        { id: 4, fullName: "Ana Martínez", email: "ana@example.com", role: "Productor", status: "Active", registrationDate: "2024-02-25" },
      ]);
      setLoading(false);
    }, 1000);
  }, []);

  const adminInsights: AdminInsightItem[] = [
    {
      title: "Usuarios Totales",
      value: "1,284",
      percentage: 12,
      footer: "Más que el mes pasado",
      color: "primary",
      icon: faUsers,
    },
    {
      title: "Productos Activos",
      value: "456",
      percentage: 5,
      footer: "Nuevos esta semana",
      color: "secondary",
      icon: faBoxesStacked,
    },
    {
      title: "Ventas Totales",
      value: "$125,400",
      percentage: 18,
      footer: "Crecimiento mensual",
      color: "accent",
      icon: faChartPie,
    },
  ];

  const filteredUsers = users.filter(
    (u) =>
      u.fullName.toLowerCase().includes(searchTerm.toLowerCase()) ||
      u.email.toLowerCase().includes(searchTerm.toLowerCase())
  );

  const handleEditUser = (user: UserInfo) => {
    setSelectedUser(user);
    setIsEditModalOpen(true);
  };

  const handleSaveUser = (updatedUser: UserInfo) => {
    setUsers(users.map((u) => (u.id === updatedUser.id ? updatedUser : u)));
    setIsEditModalOpen(false);
  };

  return (
    <div className="w-full">
      <div className="flex justify-between items-center mb-8">
        <h1 className="text-3xl font-extrabold text-[#004d00]">Panel de Administración</h1>
      </div>

      {/* Insights Grid */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8 mb-12">
        {adminInsights.map((item, idx) => (
          <div key={idx} className="bg-white p-8 rounded-4xl shadow-xl hover:-translate-y-1 transition-all duration-300 relative overflow-hidden border-t-4 border-[#8dc84b]">
            <div className="flex items-center justify-between">
              <div>
                <h3 className="text-sm font-bold text-gray-400 uppercase tracking-widest">{item.title}</h3>
                <h1 className="text-3xl font-black mt-2 text-gray-900">{item.value}</h1>
              </div>
              <div className={`w-14 h-14 rounded-2xl flex items-center justify-center text-white shadow-lg ${
                item.color === 'primary' ? 'bg-[#004d00]' : item.color === 'secondary' ? 'bg-[#8dc84b]' : 'bg-[#004d00]'
              }`}>
                <FontAwesomeIcon icon={item.icon} size="lg" />
              </div>
            </div>
            <div className="mt-6 flex items-center gap-2">
              <span className="text-green-600 font-bold text-sm">+{item.percentage}%</span>
              <small className="text-gray-400 font-medium">{item.footer}</small>
            </div>
          </div>
        ))}
      </div>

      {/* Quick Access Grid */}
      <h2 className="text-2xl font-bold text-gray-800 mb-6">Accesos Rápidos</h2>
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-12">
        <Link to="/DashboardAdmin/Usuarios" className="bg-[#004d00] p-6 rounded-3xl shadow-lg hover:shadow-[#004d00]/30 hover:-translate-y-1 transition-all duration-300 flex items-center gap-4 group cursor-pointer text-white">
          <div className="w-12 h-12 rounded-2xl bg-white/20 flex items-center justify-center group-hover:scale-110 transition-transform">
            <FontAwesomeIcon icon={faUsers} size="lg" />
          </div>
          <div>
            <h3 className="font-bold text-lg">Gestionar Usuarios</h3>
            <p className="text-white/70 text-sm">Ver y editar información de usuarios</p>
          </div>
        </Link>
        <Link to="/DashboardAdmin/Productos" className="bg-[#8dc84b] p-6 rounded-3xl shadow-lg hover:shadow-[#8dc84b]/30 hover:-translate-y-1 transition-all duration-300 flex items-center gap-4 group cursor-pointer text-[#004d00]">
          <div className="w-12 h-12 rounded-2xl bg-white/40 flex items-center justify-center group-hover:scale-110 transition-transform">
            <FontAwesomeIcon icon={faBoxesStacked} size="lg" />
          </div>
          <div>
            <h3 className="font-bold text-lg">Gestionar Productos</h3>
            <p className="text-[#004d00]/70 text-sm">Aprobar, editar o eliminar productos</p>
          </div>
        </Link>
        <Link to="/DashboardAdmin/configuracion" className="bg-[#004d00] p-6 rounded-3xl shadow-lg hover:shadow-[#004d00]/30 hover:-translate-y-1 transition-all duration-300 flex items-center gap-4 group cursor-pointer text-white">
          <div className="w-12 h-12 rounded-2xl bg-white/20 flex items-center justify-center group-hover:scale-110 transition-transform">
            <FontAwesomeIcon icon={faGear} size="lg" />
          </div>
          <div>
            <h3 className="font-bold text-lg">Configuración</h3>
            <p className="text-white/70 text-sm">Configuraciones del sistema</p>
          </div>
        </Link>
      </div>

      {/* User Management Section */}
      <section className="bg-white p-8 rounded-3xl shadow-sm mb-8 border border-gray-100">
        <div className="flex flex-col md:flex-row justify-between items-start md:items-center gap-4 mb-8">
          <h2 className="text-2xl font-bold text-gray-800">Gestión de Usuarios</h2>
          
          <div className="flex gap-4 w-full md:w-auto">
            <div className="relative flex-1 md:w-80">
              <FontAwesomeIcon icon={faMagnifyingGlass} className="absolute left-4 top-1/2 -translate-y-1/2 text-gray-400" />
              <input
                type="text"
                placeholder="Buscar usuarios..."
                className="w-full pl-12 pr-4 py-3 border-2 border-gray-100 rounded-xl outline-none focus:border-[#8dc84b] transition-all"
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
              />
            </div>
            <Button text="Exportar" iconLetf={faFileExcel} className="bg-[#8dc84b] text-white rounded-xl py-" />
            <Button text="Reporte General" iconLetf={faFilePdf} className="bg-[#004d00] text-white rounded-xl py-3" />
          </div>
        </div>

        <div className="overflow-x-auto">
          {loading ? (
            <div className="flex flex-col items-center py-20 gap-4">
              <div className="w-12 h-12 border-4 border-[#8dc84b] border-t-transparent rounded-full animate-spin"></div>
              <p className="text-gray-500 font-medium font-outfit">Cargando base de usuarios...</p>
            </div>
          ) : (
            <table className="w-full">
              <thead>
                <tr className="border-b border-gray-100 text-gray-400 font-bold text-xs uppercase tracking-wider">
                  <th className="py-4 px-4 text-left font-extrabold pb-6">Usuario</th>
                  <th className="py-4 px-4 text-left font-extrabold pb-6">Rol</th>
                  <th className="py-4 px-4 text-left font-extrabold pb-6">Registro</th>
                  <th className="py-4 px-4 text-left font-extrabold pb-6">Estado</th>
                  <th className="py-4 px-4 text-center font-extrabold pb-6">Acciones</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-gray-50">
                {filteredUsers.map((user) => (
                  <tr key={user.id} className="hover:bg-gray-50/50 transition-colors group">
                    <td className="py-5 px-4">
                      <div className="flex flex-col">
                        <span className="font-bold text-gray-800">{user.fullName}</span>
                        <span className="text-sm text-gray-400">{user.email}</span>
                      </div>
                    </td>
                    <td className="py-5 px-4 font-medium text-gray-600">
                      <span className={`px-3 py-1 rounded-full text-xs font-bold ${
                        user.role === 'Productor' ? 'bg-orange-50 text-orange-600' : 'bg-blue-50 text-blue-600'
                      }`}>
                        {user.role}
                      </span>
                    </td>
                    <td className="py-5 px-4 text-sm text-gray-500">{user.registrationDate}</td>
                    <td className="py-5 px-4">
                      <span className={`flex items-center gap-1.5 text-xs font-black uppercase ${
                        user.status === 'Active' ? 'text-green-500' : 'text-red-400'
                      }`}>
                        <div className={`w-2 h-2 rounded-full ${user.status === 'Active' ? 'bg-green-500' : 'bg-red-400'}`} />
                        {user.status === 'Active' ? 'Activo' : 'Inactivo'}
                      </span>
                    </td>
                    <td className="py-5 px-4">
                      <div className="flex justify-center gap-2">
                        <button 
                          onClick={() => handleEditUser(user)}
                          className="w-10 h-10 rounded-xl bg-gray-50 text-gray-400 hover:bg-[#004d00] hover:text-white transition-all duration-300 flex items-center justify-center border-none cursor-pointer"
                        >
                          <FontAwesomeIcon icon={faPen} />
                        </button>
                        <button className="w-10 h-10 rounded-xl bg-gray-50 text-gray-400 hover:bg-red-500 hover:text-white transition-all duration-300 flex items-center justify-center border-none cursor-pointer">
                          <FontAwesomeIcon icon={user.status === 'Active' ? faUserSlash : faUserCheck} />
                        </button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          )}
        </div>
      </section>

      {/* Edit User Modal */}
      <EditUserModal
        isOpen={isEditModalOpen}
        onClose={() => setIsEditModalOpen(false)}
        user={selectedUser}
        onSave={handleSaveUser}
      />
    </div>
  );
};

export default DashboardAdmin;
