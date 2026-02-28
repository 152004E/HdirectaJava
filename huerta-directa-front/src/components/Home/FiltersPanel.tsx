import { useState } from "react";

interface FiltersPanelProps {
  onApply: (filters: string[]) => void;
}

export const FiltersPanel = ({ onApply }: FiltersPanelProps) => {
  const [selectedOrder, setSelectedOrder] = useState("Más nuevo");
  const [selectedCategory, setSelectedCategory] = useState("Quesos");

  const apply = () => {
    const filters = [selectedOrder, selectedCategory];
    onApply(filters);
  };

  return (
    <div className="absolute right-0 mt-4 w-80 bg-white shadow-2xl rounded-2xl p-6 z-50">

      <h2 className="text-xl font-bold mb-4">Filtros</h2>

      <div className="mb-6">
        <h3 className="text-sm font-semibold text-gray-500 mb-2">
          ORDENAR POR
        </h3>

        {["Más reseñas", "Más nuevo", "Precio: Menor a Mayor", "Precio: Mayor a Menor"].map((option) => (
          <label key={option} className="flex items-center gap-2 mb-2 cursor-pointer">
            <input
              type="radio"
              name="order"
              checked={selectedOrder === option}
              onChange={() => setSelectedOrder(option)}
            />
            {option}
          </label>
        ))}
      </div>

      <div className="mb-6">
        <h3 className="text-sm font-semibold text-gray-500 mb-2">
          CATEGORÍA
        </h3>

        {["Quesos", "Leches", "Yogures", "Mantequillas"].map((cat) => (
          <label key={cat} className="flex items-center gap-2 mb-2 cursor-pointer">
            <input
              type="radio"
              name="category"
              checked={selectedCategory === cat}
              onChange={() => setSelectedCategory(cat)}
            />
            {cat}
          </label>
        ))}
      </div>

      <button
        onClick={apply}
        className="w-full bg-gray-300 hover:bg-gray-400 transition rounded-xl py-2 font-semibold"
      >
        Aplicar
      </button>
    </div>
  );
};