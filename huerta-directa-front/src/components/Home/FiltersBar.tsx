import { useState } from "react";
import { Button } from "../GlobalComponents/Button";
import { faFilter, faXmark } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { FiltersPanel } from "./FiltersPanel";


interface FiltersBarProps {
  title: string;
}

export const FiltersBar = ({ title }: FiltersBarProps) => {
  const [showPanel, setShowPanel] = useState(false);
  const [activeFilters, setActiveFilters] = useState<string[]>([]);

  const applyFilters = (filters: string[]) => {
    setActiveFilters(filters);
    setShowPanel(false);
  };

  const removeFilter = (filterToRemove: string) => {
    setActiveFilters((prev) =>
      prev.filter((f) => f !== filterToRemove)
    );
  };

  const clearAll = () => {
    setActiveFilters([]);
  };

  return (
    <div className="relative">
      <div className="flex items-center justify-between border border-gray-300/60 p-4 shadow-lg rounded-2xl bg-white">

        {/* LEFT SIDE */}
        <div className="flex items-center gap-4 flex-wrap flex-1">

          <h1 className="text-3xl font-bold text-[#8dc84b] whitespace-nowrap">
            {title}
          </h1>

          {activeFilters.map((filter, index) => (
            <span
              key={index}
              className="flex items-center gap-2 bg-gray-200 px-3 py-1 rounded-full text-sm"
            >
              {filter}
              <FontAwesomeIcon
                icon={faXmark}
                onClick={() => removeFilter(filter)}
                className="cursor-pointer text-gray-600 hover:text-red-500"
              />
            </span>
          ))}

          {activeFilters.length > 0 && (
            <button
              onClick={clearAll}
              className="text-green-600 text-sm font-semibold hover:underline"
            >
              Limpiar todos
            </button>
          )}
        </div>

        {/* BUTTON */}
        <Button
          text="Filtros"
          iconLetf={faFilter}
          onClick={() => setShowPanel(!showPanel)}
          className="px-5 py-2"
        />
      </div>

      {/* PANEL */}
      {showPanel && (
        <FiltersPanel onApply={applyFilters} />
      )}
    </div>
  );
};