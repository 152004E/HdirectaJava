import f1 from "../../assets/image/oferts/f1.png";
import f2 from "../../assets/image/oferts/f2.png";
import f3 from "../../assets/image/oferts/f3.png";
import f4 from "../../assets/image/oferts/f4.png";

const offers = [
  { img: f1, title: "Oferta", type: "Premium" },
  { img: f2, title: "Oferta", type: "Premium" },
  { img: f3, title: "Oferta", type: "Premium" },
  { img: f4, title: "Oferta", type: "Premium" },
];

export const OffersSection = () => {
  return (
    <section
      className="w-full py-20 transition-colors! duration-500! 
      bg-linear-to-b from-white via-white to-[#FEF5DC]
      dark:bg-[#1A221C] dark:from-white/20 dark:via-[#1A221C] dark:to-[#1A221C]"
    >
      <div className="max-w-330 mx-auto px-6">
        <div className="text-center mb-12">
          <h2 className="text-3xl font-bold text-[#8dc84b]">
            Ofertas del Día
          </h2>

          <p className="text-gray-500 dark:text-gray-400 mt-2">
            Aprovecha nuestros descuentos especiales en productos frescos
            seleccionados para hoy
          </p>
        </div>

        <div className="flex justify-between gap-8 flex-wrap">
          {offers.map((offer, index) => (
            <div
              key={index}
              className="
                w-70
                flex flex-col
                items-center
                text-center
                border
                border-gray-400/10
                dark:border-gray-700/40
                p-9
                rounded-2xl
                shadow-xl
                transition-all duration-500 ease-in-out
                hover:-translate-y-3
                hover:shadow-2xl
                bg-white
                dark:bg-[#111814]
                dark:hover:bg-[#1A221C]
                gap-4
              "
            >
              <img
                src={offer.img}
                alt={offer.title}
                className="w-35 mx-auto"
              />

              <h3 className="font-bold text-lg text-[#333128] dark:text-gray-200">
                {offer.title}
              </h3>

              <p className="text-[#8dc84b] font-semibold text-[15px]">
                {offer.type}
              </p>
            </div>
          ))}
        </div>
      </div>
    </section>
  );
};