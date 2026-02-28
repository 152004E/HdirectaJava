import React from "react";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { 
  faChartLine, 
  faArrowTrendUp, 
  faArrowTrendDown, 
  faUsers, 
  faBoxesStacked, 
  faMoneyBillTrendUp 
} from "@fortawesome/free-solid-svg-icons";
import { usePageTitle } from "../../hooks/usePageTitle";

export const DashboardGraficos: React.FC = () => {
  usePageTitle("Gráficos y Estadísticas");

  const stats = [
    { title: "Ventas Mensuales", value: "$12,450", change: "+14%", trend: "up", icon: faMoneyBillTrendUp, color: "bg-green-500" },
    { title: "Nuevos Clientes", value: "124", change: "+8%", trend: "up", icon: faUsers, color: "bg-blue-500" },
    { title: "Productos en Stock", value: "45", change: "-2%", trend: "down", icon: faBoxesStacked, color: "bg-orange-500" },
  ];

  return (
    <div className="w-full flex flex-col gap-8 animate-fadeIn">
      <div className="flex items-center gap-4">
        <div className="w-12 h-12 rounded-2xl bg-[#8dc84b] text-white flex items-center justify-center shadow-lg shadow-[#8dc84b]/20">
          <FontAwesomeIcon icon={faChartLine} size="lg" />
        </div>
        <h1 className="text-3xl font-black text-gray-800 tracking-tight">Gráficos y Estadísticas</h1>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        {stats.map((stat, idx) => (
          <div key={idx} className="bg-white p-6 rounded-3xl shadow-sm border border-gray-100 flex flex-col gap-4 hover:shadow-md transition-shadow">
            <div className="flex justify-between items-start">
              <div className={`w-12 h-12 rounded-xl ${stat.color} text-white flex items-center justify-center shadow-lg`}>
                <FontAwesomeIcon icon={stat.icon} />
              </div>
              <span className={`flex items-center gap-1 text-sm font-bold ${stat.trend === 'up' ? 'text-green-500' : 'text-red-500'}`}>
                {stat.change}
                <FontAwesomeIcon icon={stat.trend === 'up' ? faArrowTrendUp : faArrowTrendDown} />
              </span>
            </div>
            <div>
              <p className="text-gray-400 font-bold text-xs uppercase tracking-widest">{stat.title}</p>
              <h2 className="text-2xl font-black text-gray-800">{stat.value}</h2>
            </div>
          </div>
        ))}
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
        <div className="bg-white p-8 rounded-4xl shadow-sm border border-gray-100 h-96 flex flex-col items-center justify-center">
          <p className="text-gray-400 font-medium italic">Gráfico de Ventas (Simulado)</p>
          <div className="w-full h-48 mt-4 flex items-end gap-2 px-8">
            {[40, 70, 45, 90, 65, 80, 55, 95].map((h, i) => (
              <div key={i} className="flex-1 bg-[#8dc84b]/20 rounded-t-lg transition-all hover:bg-[#8dc84b]" style={{ height: `${h}%` }}></div>
            ))}
          </div>
        </div>
        <div className="bg-white p-8 rounded-4xl shadow-sm border border-gray-100 h-96 flex flex-col items-center justify-center">
          <p className="text-gray-400 font-medium italic">Distribución de Categorías (Simulado)</p>
          <div className="relative w-48 h-48 mt-4 rounded-full border-12 border-[#8dc84b] flex items-center justify-center">
             <span className="font-black text-2xl text-gray-800">75%</span>
          </div>
        </div>
      </div>
    </div>
  );
};
export default DashboardGraficos;
