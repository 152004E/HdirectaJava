import { Link } from "react-router-dom";
import {
  faAppleWhole,
  faCarrot,
  faCow,
  faDrumstickBite,
  faBowlFood,
  faCookie,
  faPlateWheat,
  faGlassWater,
  faBoxOpen,
  faEnvelope,
} from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";

const categories = [
  { name: "Frutas", icon: faAppleWhole, link: "/Frutas" },
  { name: "Verduras y hortalizas", icon: faCarrot, link: "/VerdurasYHortalizas" },
  { name: "Lácteos", icon: faCow, link: "/Lacteos" },
  { name: "Carnes y proteínas", icon: faDrumstickBite, link: "/CarnesYl" },
  { name: "Cereales y granos", icon: faBowlFood, link: "/Cereales" },
  { name: "Productos orgánicos", icon: faCookie, link: "/Organicos" },
  { name: "Miel y derivados", icon: faPlateWheat, link: "/MielYDerivados" },
  { name: "Bebidas naturales", icon: faGlassWater, link: "/BebidasNaturales" },
  { name: "Cajas mixtas o combos", icon: faBoxOpen, link: "/CajasMixtas" },
];

const CategoriesSection = () => {
  return (
    <section 
      id="categorias"
      className=" py-20  bg-linear-to-b  from-[#FEF5DC] via-white to-[#FEF5DC]"
    >
      <div className="max-w-300 mx-auto grid md:grid-cols-2 gap-5 px-6">

        {/* Lado izquierdo */}
        <div>
          <h1 className="text-4xl font-bold mb-6">Categorías</h1>

          <p className="text-gray-700 text-lg mb-8 leading-relaxed">
            Los productos de mejor calidad y más frescos del mercado, de la
            granja a tu mesa. Explora tu producto favorito en la categoría
            indicada y descubre los maravillosos precios que maneja el campo
            para tu bolsillo.
          </p>

          <Link
            to="/QuienesSomos"
            className="inline-block bg-[#8dc84b] text-white font-semibold px-6 py-3 rounded-lg transition-all duration-500 ease-in-out hover:bg-green-800"
          >
            Saber más
          </Link>
        </div>

        {/* Lado derecho */}
        <div className="grid grid-cols-2 sm:grid-cols-3 gap-8">
          {categories.map((category, index) => (
            <Link key={index} to={category.link}>
              <div className="bg-white rounded-3xl shadow-xl p-8 text-center border border-gray-400/10 h-55 w-47 flex flex-col justify-center items-center transition-all duration-500 ease-in-out hover:-translate-y-2 hover:shadow-2xl">

                <div className="w-16 h-16 flex items-center justify-center rounded-full bg-[#8dc84b] mb-3  text-2xl">
                  <FontAwesomeIcon icon={category.icon} />
                </div>

                <h5 className="font-semibold text-lg">
                  {category.name}
                </h5>
              </div>
            </Link>
          ))}
        </div>
      </div>

      {/* Botón flotante */}
      <Link
        to="/QuienesSomos"
        className="fixed h-13.75 bg-[#8bc34a] p-4 right-6.5 bottom-25 rounded-full hover:w-50 cursor-pointer group transition-all duration-500 ease-in-out"
      >
        <div className="flex justify-center gap-3 items-center">
          <FontAwesomeIcon icon={faEnvelope} className="text-2xl text-white" />
          <p className="hidden group-hover:block text-white whitespace-nowrap transition-all duration-500 ease-in-out">
            Conócenos
          </p>
        </div>
      </Link>
    </section>
  );
};

export default CategoriesSection;