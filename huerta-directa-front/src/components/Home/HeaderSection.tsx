import { Button } from "../GlobalComponents/Button";
import ImagenHover_modified from "../../assets/image/ImagenHover-modified.png" 
import rigthImg from "../../assets/image/rigth.png" 
import { faCircleInfo } from "@fortawesome/free-solid-svg-icons";

export const HeaderSection = () => {

  return (
    <section className="max-w-300 mx-auto flex items-center py-4 transition-colors! duration-500! ">
      {/* Texto */}
      <div className="flex-1">
        <h1 className="text-[#333128] text-[55px] mb-6 leading-none dark:text-white">
          Frutas y verduras
        </h1>

        <p className="text-[#333128] mb-11 text-lg dark:text-white/70">
          En Huerta Directa, te conectamos con productos campesinos frescos y de
          calidad, cosechados directamente por manos locales. Disfruta de
          alimentos 100% naturales, sin intermediarios y al mejor precio. ¡Comer
          sano y apoyar al campo nunca fue tan fácil!
        </p>

        <Button
          text="Información"
          to="/quienes-somos"
          iconRight={faCircleInfo}
          className="py-3.75 px-6.25 bg-[#8dc84b] hover:bg-[#004d00] text-white rounded-[50px] transition-all duration-500"
        />
      </div>

      {/* Imagen con efecto flip */}
      <div className="flex-1 flex justify-center">
        <div className="relative w-100 h-100 perspective">
          <div className="flip-card-inner">
            {/* Imagen frontal */}
            <img
              src={rigthImg}
              alt="Imagen original"
              className="absolute w-full h-full backface-hidden"
            />

            {/* Imagen trasera */}
            <img
              src={ImagenHover_modified}
              alt="Imagen hover"
              className="absolute w-full h-full backface-hidden rotate-y-180"
            />
          </div>
        </div>
      </div>
    </section>
  );
};
