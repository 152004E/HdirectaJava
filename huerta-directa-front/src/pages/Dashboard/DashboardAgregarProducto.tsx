import React, { useState } from "react";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faBoxOpen, faCloudArrowUp, faUpload, faStar, faChevronLeft, faChevronRight } from "@fortawesome/free-solid-svg-icons";
import { Button } from "../../components/GlobalComponents/Button";
import { usePageTitle } from "../../hooks/usePageTitle";

export const DashboardAgregarProducto: React.FC = () => {
  usePageTitle("Agregar Producto");

  const [formData, setFormData] = useState({
    name: "",
    price: "",
    category: "Frutas",
    description: "",
    stock: "",
  });
  const [images, setImages] = useState<{ file: File; preview: string }[]>([]);
  const [currentImageIndex, setCurrentImageIndex] = useState(0);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement | HTMLTextAreaElement>) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleImageChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.files) {
      const selectedFiles = Array.from(e.target.files);
      const remainingSlots = 5 - images.length;
      const filesToProcess = selectedFiles.slice(0, remainingSlots);

      if (filesToProcess.length === 0 && selectedFiles.length > 0) {
        alert("Ya has seleccionado el máximo de 5 imágenes.");
        return;
      }

      filesToProcess.forEach(file => {
        const reader = new FileReader();
        reader.onload = (e) => {
          if (e.target?.result) {
            setImages(prev => {
              const newImages = [...prev, { file, preview: e.target!.result as string }];
              // If it's the first image being added, ensure preview starts at 0
              if (prev.length === 0) setCurrentImageIndex(0);
              return newImages;
            });
          }
        };
        reader.readAsDataURL(file);
      });
    }
  };

  const removeImage = (indexToRemove: number, e: React.MouseEvent) => {
    e.preventDefault();
    setImages(prev => {
      const newImages = prev.filter((_, index) => index !== indexToRemove);
      // Adjust current index if we delete the currently viewed image or one before it
      if (currentImageIndex >= newImages.length) {
        setCurrentImageIndex(Math.max(0, newImages.length - 1));
      }
      return newImages;
    });
  };

  const nextImage = () => {
    if (images.length > 1) {
      setCurrentImageIndex((prev) => (prev + 1) % images.length);
    }
  };

  const prevImage = () => {
    if (images.length > 1) {
      setCurrentImageIndex((prev) => (prev - 1 + images.length) % images.length);
    }
  };

  return (
    <div className="w-full flex flex-col gap-8 animate-fadeIn">
      <div className="flex items-center gap-4">
        <div className="w-12 h-12 rounded-2xl bg-[#ffa000] text-white flex items-center justify-center shadow-lg shadow-[#ffa000]/20">
          <FontAwesomeIcon icon={faBoxOpen} size="lg" />
        </div>
        <h1 className="text-3xl font-black text-gray-800 tracking-tight">Agregar Nuevo Producto</h1>
      </div>

      <div className="grid grid-cols-1 xl:grid-cols-3 gap-8">
        
        {/* FORMULARIO */}
        <div className="xl:col-span-2 bg-white p-8 md:p-10 rounded-4xl shadow-sm border border-gray-100">
          <form className="grid grid-cols-1 md:grid-cols-2 gap-8">
            <div className="flex flex-col gap-2">
              <label className="font-bold text-gray-700 uppercase tracking-widest text-xs">Nombre del Producto</label>
              <input type="text" name="name" value={formData.name} onChange={handleChange} className="p-4 bg-gray-50 border-2 border-gray-100 rounded-2xl outline-none focus:border-[#8dc84b] transition-all" placeholder="Ej: Tomate Orgánico" />
            </div>
            
            <div className="flex flex-col gap-2">
              <label className="font-bold text-gray-700 uppercase tracking-widest text-xs">Precio (COP)</label>
              <input type="number" name="price" value={formData.price} onChange={handleChange} className="p-4 bg-gray-50 border-2 border-gray-100 rounded-2xl outline-none focus:border-[#8dc84b] transition-all" placeholder="0" />
            </div>

            <div className="flex flex-col gap-2">
              <label className="font-bold text-gray-700 uppercase tracking-widest text-xs">Categoría</label>
              <select name="category" value={formData.category} onChange={handleChange} className="p-4 bg-gray-50 border-2 border-gray-100 rounded-2xl outline-none focus:border-[#8dc84b] transition-all appearance-none cursor-pointer">
                <option value="Frutas">Frutas</option>
                <option value="Verduras y Hortalizas">Verduras y Hortalizas</option>
                <option value="Lácteos">Lácteos</option>
                <option value="Carnes y Proteinas">Carnes y Proteinas</option>
                <option value="Cereales y Granos">Cereales y Granos</option>
                <option value="Productos Orgánicos">Productos Orgánicos</option>
                <option value="Miel y Derivados">Miel y Derivados</option>
                <option value="Bebidas naturales">Bebidas naturales</option>
                <option value="Cajas mixtas y combos">Cajas mixtas y combos</option>
              </select>
            </div>

            <div className="flex flex-col gap-2">
              <label className="font-bold text-gray-700 uppercase tracking-widest text-xs">Stock Inicial</label>
              <input type="number" name="stock" value={formData.stock} onChange={handleChange} className="p-4 bg-gray-50 border-2 border-gray-100 rounded-2xl outline-none focus:border-[#8dc84b] transition-all" placeholder="0" />
            </div>

            <div className="flex flex-col gap-2 md:col-span-2">
              <label className="font-bold text-gray-700 uppercase tracking-widest text-xs">Descripción</label>
              <textarea rows={4} name="description" value={formData.description} onChange={handleChange} className="p-4 bg-gray-50 border-2 border-gray-100 rounded-2xl outline-none focus:border-[#8dc84b] transition-all resize-none" placeholder="Describe tu producto..."></textarea>
            </div>

            <div className="md:col-span-2">
              <label className="font-bold text-gray-700 uppercase tracking-widest text-xs block mb-4">
                Imágenes del Producto ({images.length}/5)
              </label>
              <label className={`w-full ${images.length > 0 ? 'min-h-30 p-6' : 'h-48'} border-4 border-dashed border-gray-100 rounded-3xl flex flex-col items-center justify-center gap-4 transition-all cursor-pointer group relative ${images.length >= 5 ? 'opacity-50 cursor-not-allowed' : 'hover:border-[#8dc84b] hover:bg-green-50/30'}`}>
                <input 
                  type="file" 
                  accept="image/*" 
                  multiple 
                  onChange={handleImageChange} 
                  className="hidden" 
                  disabled={images.length >= 5}
                />
                
                {images.length > 0 ? (
                  <div className="w-full flex flex-col gap-2">
                    <p className="text-gray-500 font-bold mb-2 text-center text-sm">Archivos seleccionados:</p>
                    <ul className="flex flex-col gap-2 w-full max-w-md mx-auto">
                      {images.map((img, index) => (
                        <li key={index} className="flex justify-between items-center bg-white border border-gray-100 p-3 rounded-xl shadow-sm z-10">
                          <span className="text-sm font-medium text-gray-700 truncate max-w-[80%]">{img.file.name}</span>
                          <button 
                            onClick={(e) => removeImage(index, e)}
                            className="text-red-400 hover:text-red-600 font-bold text-xs p-2"
                          >
                            Eliminar
                          </button>
                        </li>
                      ))}
                    </ul>
                    {images.length < 5 && (
                      <div className="mt-4 flex items-center justify-center gap-2 text-gray-400 font-bold group-hover:text-[#8dc84b] transition-colors">
                        <FontAwesomeIcon icon={faUpload} />
                        <span>Añadir más imágenes</span>
                      </div>
                    )}
                  </div>
                ) : (
                  <>
                    <FontAwesomeIcon icon={faUpload} className="text-4xl text-gray-300 group-hover:text-[#8dc84b] transition-all" />
                    <p className="text-gray-400 font-bold group-hover:text-[#8dc84b] transition-colors">Cargar imágenes (PNG, JPG) - Máx 5</p>
                  </>
                )}
              </label>
            </div>

            <div className="md:col-span-2 pt-4">
              <Button text="Publicar Producto" iconLetf={faCloudArrowUp} className="w-full py-5 rounded-2xl shadow-xl shadow-[#8dc84b]/20 bg-[#8dc84b] text-white font-black text-lg" />
            </div>
          </form>
        </div>

        {/* LIVE PREVIEW CARD */}
        <div className="xl:col-span-1">
          <div className="sticky top-8 flex flex-col gap-4">
            <h3 className="text-xl font-bold text-gray-800 px-2 border-l-4 border-[#8dc84b]">Vista Previa</h3>
            
            <div className="bg-white rounded-4xl overflow-hidden shadow-sm border border-gray-100 group hover:shadow-2xl hover:shadow-[#8dc84b]/20 transition-all duration-300 flex flex-col h-full max-w-sm mx-auto w-full">
              <div className="relative h-64 overflow-hidden group/slider">
                {images.length > 0 ? (
                  <>
                    <img src={images[currentImageIndex].preview} alt={`Product ${currentImageIndex + 1}`} className="w-full h-full object-cover transition-transform duration-700" />
                    
                    {images.length > 1 && (
                      <>
                        <button 
                          onClick={prevImage}
                          className="absolute left-2 top-1/2 -translate-y-1/2 w-8 h-8 rounded-full bg-white/80 backdrop-blur-sm text-gray-800 flex items-center justify-center opacity-0 group-hover/slider:opacity-100 transition-opacity hover:bg-white hover:text-[#8dc84b] shadow-md z-10 cursor-pointer"
                        >
                          <FontAwesomeIcon icon={faChevronLeft} className="text-sm" />
                        </button>
                        <button 
                          onClick={nextImage}
                          className="absolute right-2 top-1/2 -translate-y-1/2 w-8 h-8 rounded-full bg-white/80 backdrop-blur-sm text-gray-800 flex items-center justify-center opacity-0 group-hover/slider:opacity-100 transition-opacity hover:bg-white hover:text-[#8dc84b] shadow-md z-10 cursor-pointer"
                        >
                          <FontAwesomeIcon icon={faChevronRight} className="text-sm" />
                        </button>
                        
                        <div className="absolute bottom-3 left-1/2 -translate-x-1/2 flex gap-1.5 z-10">
                          {images.map((_, idx) => (
                            <div 
                              key={idx} 
                              className={`h-1.5 rounded-full transition-all duration-300 ${idx === currentImageIndex ? 'w-4 bg-white shadow-sm' : 'w-1.5 bg-white/50'}`} 
                            />
                          ))}
                        </div>
                      </>
                    )}
                  </>
                ) : (
                  <div className="w-full h-full bg-gray-50 border-b border-gray-100 flex items-center justify-center">
                    <FontAwesomeIcon icon={faBoxOpen} className="text-6xl text-gray-200" />
                  </div>
                )}
                
                <div className="absolute top-4 left-4 bg-white/90 backdrop-blur-sm px-4 py-2 rounded-2xl flex items-center gap-2 shadow-sm z-10">
                  <span className="text-sm font-bold text-[#004d00]">
                    {formData.category || "Categoría"}
                  </span>
                </div>
              </div>

              <div className="p-8 flex flex-col grow">
                <div className="flex items-center gap-2 mb-3">
                  <div className="flex text-[#ffa000] text-sm">
                    <FontAwesomeIcon icon={faStar} />
                    <FontAwesomeIcon icon={faStar} />
                    <FontAwesomeIcon icon={faStar} />
                    <FontAwesomeIcon icon={faStar} />
                    <FontAwesomeIcon icon={faStar} />
                  </div>
                  <span className="text-sm font-bold text-gray-400">(0)</span>
                </div>

                <div className="mb-4">
                  <h3 className="text-2xl font-black text-[#004d00] leading-tight mb-2 group-hover:text-[#8dc84b] transition-colors">{formData.name || "Nombre del Producto"}</h3>
                  <p className="text-gray-500 font-medium text-sm">Por Mi Huerta</p>
                </div>
                
                <p className="text-gray-500 text-sm leading-relaxed mb-6 line-clamp-2">
                  {formData.description || "Agrega una descripción para tu producto para que los compradores puedan conocer los detalles."}
                </p>

                <div className="mt-auto">
                  <div className="flex justify-between items-end mb-6 bg-gray-50 p-4 rounded-2xl">
                    <div>
                      <span className="text-gray-400 text-xs font-bold uppercase tracking-wider block mb-1">Precio</span>
                      <span className="text-2xl font-black text-[#8dc84b]">
                        ${Number(formData.price || 0).toLocaleString()}
                      </span>
                    </div>
                    <div className="text-right">
                      <span className="text-gray-400 text-xs font-bold uppercase tracking-wider block mb-1">Stock Disp.</span>
                      <span className="text-lg font-bold text-gray-700">{formData.stock || 0} un.</span>
                    </div>
                  </div>
                  

                </div>
              </div>
            </div>

          </div>
        </div>

      </div>
    </div>
  );
};

export default DashboardAgregarProducto;
