import { useState } from "react";

interface Props {
  onClose: () => void;
}

export const ChatModal = ({ onClose }: Props) => {
  const [showSettings, setShowSettings] = useState(false);

  return (
    <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-2000 animate-fadeIn">
      {/* CONTENEDOR */}
      <div className="bg-white w-[90%] max-w-200 h-[90%] max-h-175 rounded-xl shadow-2xl flex flex-col overflow-hidden">
        
        {/* HEADER */}
        <div className="bg-[#8dc84b] text-white px-8 py-6 flex justify-between items-center">
          <div className="flex items-center gap-4">
            <div className="w-12 h-12 bg-white/20 rounded-full flex items-center justify-center text-xl">
              🤖
            </div>
            <div>
              <h1 className="text-xl font-semibold">Asistente Huerta</h1>
              <p className="text-sm opacity-90">Powered by Groq</p>
            </div>
          </div>

          <div className="flex items-center gap-3">
            <button
              onClick={() => setShowSettings(!showSettings)}
              className="bg-white/20 hover:bg-white/30 px-3 py-2 rounded-lg text-lg transition"
            >
              ⚙️
            </button>

            <button
              onClick={onClose}
              className="bg-white/20 hover:bg-white/30 px-3 py-2 rounded-lg text-lg transition"
            >
              ❌
            </button>
          </div>
        </div>

        {/* SETTINGS */}
        {showSettings && (
          <div className="bg-yellow-100 border-b-2 border-yellow-400 px-8 py-6">
            <label className="block font-semibold mb-2 text-yellow-800">
              Groq API Key
            </label>
            <input
              type="password"
              placeholder="Ingresa tu clave aquí"
              className="w-full p-3 border-2 border-yellow-400 rounded-lg mb-3"
            />
            <button className="bg-[#8dc84b] text-white px-6 py-2 rounded-lg font-semibold">
              Guardar Configuración
            </button>
          </div>
        )}

        {/* MENSAJES */}
        <div className="flex-1 overflow-y-auto bg-gray-100 p-8">
          <div className="flex flex-col items-center justify-center h-full text-center">
            <div className="text-5xl mb-4">👋</div>
            <h2 className="text-2xl font-semibold mb-2">
              ¡Hola! Soy Huerta-IA
            </h2>
            <p className="text-gray-600">
              Configura tu Groq API Key en ⚙️
            </p>
          </div>
        </div>

        {/* INPUT */}
        <div className="bg-white border-t-2 border-gray-200 p-6">
          <div className="flex gap-4">
            <textarea
              rows={1}
              placeholder="Escribe tu mensaje..."
              className="flex-1 border-2 border-gray-200 rounded-lg p-3 resize-none focus:outline-none focus:border-[#8dc84b]"
            />
            <button className="bg-[#8dc84b] px-6 rounded-lg text-xl">
              📤
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};