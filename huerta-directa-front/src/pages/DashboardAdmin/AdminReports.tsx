import React, { useState } from "react";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faFileExcel, faFilePdf, faDownload } from "@fortawesome/free-solid-svg-icons";
import { usePageTitle } from "../../hooks/usePageTitle";
import { Button } from "../../components/GlobalComponents/Button";

interface ReportInfo {
  id: string;
  date: string;
  type: string;
  amount: number;
}

export const AdminReports: React.FC = () => {
  usePageTitle("Reportes de Ventas");

  const [reports] = useState<ReportInfo[]>([
    { id: "REP-001", date: "2024-03-01", type: "Venta Mayorista", amount: 45000 },
    { id: "REP-002", date: "2024-03-02", type: "Suscripción", amount: 15000 },
    { id: "REP-003", date: "2024-03-03", type: "Venta Minorista", amount: 3500 },
    { id: "REP-004", date: "2024-03-04", type: "Venta Mayorista", amount: 125000 },
  ]);

  return (
    <div className="w-full">
      <div className="flex justify-between items-center mb-8">
        <h1 className="text-3xl font-extrabold text-[#004d00]">Reportes de Ventas</h1>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 gap-8 mb-8">
        <div className="bg-white p-8 rounded-3xl shadow-sm border border-gray-100 flex flex-col items-center justify-center text-center hover:-translate-y-1 transition-all duration-300">
          <div className="w-16 h-16 rounded-2xl bg-green-50 text-green-600 flex items-center justify-center text-3xl mb-4">
            <FontAwesomeIcon icon={faFileExcel} />
          </div>
          <h3 className="text-xl font-bold text-gray-800 mb-2">Reporte Mensual General</h3>
          <p className="text-gray-500 mb-6 text-sm">Descargar el desglose completo de transacciones en formato .xlsx</p>
          <Button text="Descargar Excel" iconLetf={faDownload} className="bg-[#8dc84b] text-white rounded-xl py-3 w-full" />
        </div>

        <div className="bg-white p-8 rounded-3xl shadow-sm border border-gray-100 flex flex-col items-center justify-center text-center hover:-translate-y-1 transition-all duration-300">
          <div className="w-16 h-16 rounded-2xl bg-red-50 text-red-600 flex items-center justify-center text-3xl mb-4">
            <FontAwesomeIcon icon={faFilePdf} />
          </div>
          <h3 className="text-xl font-bold text-gray-800 mb-2">Resumen Ejecutivo</h3>
          <p className="text-gray-500 mb-6 text-sm">Descargar el resumen para stakeholders en formato .pdf</p>
          <Button text="Descargar PDF" iconLetf={faDownload} className="bg-[#004d00] text-white rounded-xl py-3 w-full" />
        </div>
      </div>

      <section className="bg-white p-8 rounded-3xl shadow-sm border border-gray-100">
        <h2 className="text-2xl font-bold text-gray-800 mb-6">Últimas Transacciones Registradas</h2>
        <div className="overflow-x-auto">
          <table className="w-full">
            <thead>
              <tr className="border-b border-gray-100 text-gray-400 font-bold text-xs uppercase tracking-wider">
                <th className="py-4 px-4 text-left pb-6">ID Reporte</th>
                <th className="py-4 px-4 text-left pb-6">Fecha</th>
                <th className="py-4 px-4 text-left pb-6">Tipo</th>
                <th className="py-4 px-4 text-left pb-6">Monto</th>
                <th className="py-4 px-4 text-center pb-6">Archivo</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-gray-50">
              {reports.map((rep) => (
                <tr key={rep.id} className="hover:bg-gray-50/50 transition-colors">
                  <td className="py-5 px-4 font-bold text-gray-800">{rep.id}</td>
                  <td className="py-5 px-4 text-sm text-gray-500">{rep.date}</td>
                  <td className="py-5 px-4 text-gray-600 font-medium">{rep.type}</td>
                  <td className="py-5 px-4 font-bold text-[#8dc84b]">${rep.amount.toLocaleString()}</td>
                  <td className="py-5 px-4 text-center">
                    <button className="w-10 h-10 rounded-xl bg-gray-50 text-gray-400 hover:bg-[#004d00] hover:text-white transition-all cursor-pointer">
                      <FontAwesomeIcon icon={faDownload} />
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </section>
    </div>
  );
};

export default AdminReports;
