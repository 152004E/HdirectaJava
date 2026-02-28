import React from "react";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faChartPie,
  faChartLine,
  faMoneyBillTrendUp,
  faUsersViewfinder,
} from "@fortawesome/free-solid-svg-icons";
import { usePageTitle } from "../../hooks/usePageTitle";

export const AdminStats: React.FC = () => {
  usePageTitle("Estadísticas Globales");

  const stats = [
    { title: "Ingresos Totales", value: "$1,250,540", increment: "+15%", icon: faMoneyBillTrendUp, color: "text-[#004d00]", bg: "bg-[#e8f5e9]" },
    { title: "Usuarios Activos", value: "3,450", increment: "+5%", icon: faUsersViewfinder, color: "text-[#8dc84b]", bg: "bg-[#ddfacc]" },
    { title: "Tasa de Conversión", value: "8.4%", increment: "+1.2%", icon: faChartPie, color: "text-blue-600", bg: "bg-blue-50" },
    { title: "Crecimiento Mensual", value: "+24%", increment: "+4%", icon: faChartLine, color: "text-purple-600", bg: "bg-purple-50" },
  ];

  return (
    <div className="w-full">
      <div className="flex justify-between items-center mb-8">
        <h1 className="text-3xl font-extrabold text-[#004d00]">Estadísticas Globales</h1>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
        {stats.map((stat, idx) => (
          <div key={idx} className="bg-white p-6 rounded-3xl shadow-sm border border-gray-100 flex items-center gap-4 hover:-translate-y-1 transition-all duration-300">
            <div className={`w-14 h-14 rounded-2xl flex items-center justify-center text-2xl ${stat.bg} ${stat.color}`}>
              <FontAwesomeIcon icon={stat.icon} />
            </div>
            <div>
              <p className="text-sm font-bold text-gray-400">{stat.title}</p>
              <div className="flex items-end gap-2">
                <h3 className="text-2xl font-black text-gray-800">{stat.value}</h3>
                <span className="text-sm font-bold text-green-500 mb-1">{stat.increment}</span>
              </div>
            </div>
          </div>
        ))}
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
        {/* Placeholder for complex charts */}
        <div className="bg-white p-8 rounded-3xl shadow-sm border border-gray-100 min-h-[400px] flex flex-col">
          <h2 className="text-xl font-bold text-gray-800 mb-6">Tráfico Mensual</h2>
          <div className="flex-1 rounded-2xl bg-gray-50 border-2 border-dashed border-gray-200 flex items-center justify-center">
            <p className="text-gray-400 font-medium">Gráfico de Líneas (Ej. Recharts / Chart.js)</p>
          </div>
        </div>

        <div className="bg-white p-8 rounded-3xl shadow-sm border border-gray-100 min-h-[400px] flex flex-col">
          <h2 className="text-xl font-bold text-gray-800 mb-6">Ventas por Categoría</h2>
          <div className="flex-1 rounded-2xl bg-gray-50 border-2 border-dashed border-gray-200 flex items-center justify-center">
            <p className="text-gray-400 font-medium">Gráfico Circular</p>
          </div>
        </div>
      </div>
    </div>
  );
};

export default AdminStats;
