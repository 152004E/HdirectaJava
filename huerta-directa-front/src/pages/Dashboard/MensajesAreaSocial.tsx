import React from "react";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faEnvelope, faPaperPlane, faUserCircle, faCircleDot } from "@fortawesome/free-solid-svg-icons";
import { usePageTitle } from "../../hooks/usePageTitle";

export const MensajesAreaSocial: React.FC = () => {
  usePageTitle("Área Social");

  const messages = [
    { id: 1, user: "Carlos Ruiz", text: "Hola, ¿tienes disponibilidad de tomates orgánicos?", time: "10:30 AM", status: "unread" },
    { id: 2, user: "Ana López", text: "El pedido #1234 llegó en excelente estado.", time: "Ayer", status: "read" },
    { id: 3, user: "Mercado Local", text: "Evento de productores este fin de semana.", time: "Lun", status: "read" },
  ];

  return (
    <div className="w-full h-[calc(100vh-140px)] flex flex-col gap-6 animate-fadeIn">
      <div className="flex items-center gap-4">
        <div className="w-12 h-12 rounded-2xl bg-[#004d00] text-white flex items-center justify-center shadow-lg shadow-[#004d00]/20">
          <FontAwesomeIcon icon={faEnvelope} size="lg" />
        </div>
        <h1 className="text-3xl font-black text-gray-800 tracking-tight">Área Social</h1>
      </div>

      <div className="flex-1 bg-white rounded-4xl shadow-sm border border-gray-100 overflow-hidden flex">
        {/* Sidebar de mensajes */}
        <div className="w-80 border-r border-gray-50 flex flex-col">
          <div className="p-6 border-b border-gray-50">
            <h2 className="font-black text-gray-800 uppercase tracking-widest text-xs">Mensajes Recientes</h2>
          </div>
          <div className="flex-1 overflow-y-auto">
            {messages.map((msg) => (
              <div key={msg.id} className={`p-6 border-b border-gray-50 cursor-pointer hover:bg-gray-50 transition-colors relative ${msg.status === 'unread' ? 'bg-green-50/30' : ''}`}>
                <div className="flex items-center gap-3 mb-1">
                  <FontAwesomeIcon icon={faUserCircle} className="text-gray-300 text-2xl" />
                  <span className="font-bold text-gray-800 text-sm">{msg.user}</span>
                  <span className="ml-auto text-[10px] text-gray-400 uppercase font-bold">{msg.time}</span>
                </div>
                <p className="text-xs text-gray-500 truncate">{msg.text}</p>
                {msg.status === 'unread' && (
                   <FontAwesomeIcon icon={faCircleDot} className="absolute right-4 top-1/2 -translate-y-1/2 text-[#8dc84b] text-[10px]" />
                )}
              </div>
            ))}
          </div>
        </div>

        {/* Chat Area */}
        <div className="flex-1 flex flex-col bg-gray-50/30">
          <div className="p-6 bg-white border-b border-gray-50 flex items-center gap-4">
            <FontAwesomeIcon icon={faUserCircle} className="text-gray-300 text-4xl" />
            <div>
              <h3 className="font-bold text-gray-800">Carlos Ruiz</h3>
              <p className="text-xs text-green-500 font-bold uppercase tracking-tight">En línea</p>
            </div>
          </div>
          
          <div className="flex-1 p-8 flex flex-col gap-4 justify-end">
            <div className="bg-white p-4 rounded-2xl rounded-bl-none shadow-sm max-w-md self-start border border-gray-100">
               <p className="text-sm text-gray-700">Hola, ¿tienes disponibilidad de tomates orgánicos?</p>
            </div>
            <div className="bg-[#8dc84b] p-4 rounded-2xl rounded-br-none shadow-md max-w-md self-end text-white font-medium">
               <p className="text-sm">¡Hola Carlos! Sí, tenemos stock recién cosechado.</p>
            </div>
          </div>

          <div className="p-6 bg-white border-t border-gray-50">
             <div className="relative">
                <input 
                  type="text" 
                  placeholder="Escribe un mensaje..."
                  className="w-full pl-6 pr-14 py-4 bg-gray-50 border-none rounded-2xl outline-none focus:ring-2 focus:ring-[#8dc84b]/20 transition-all font-medium"
                />
                <button className="absolute right-2 top-1/2 -translate-y-1/2 w-10 h-10 bg-[#8dc84b] text-white rounded-xl flex items-center justify-center hover:scale-105 transition-all">
                  <FontAwesomeIcon icon={faPaperPlane} />
                </button>
             </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default MensajesAreaSocial;
