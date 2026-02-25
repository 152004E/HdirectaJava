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
    <section className="w-full mx-auto pt-20 flex justify-between gap-6 flex-wrap">
      {offers.map((offer, index) => (
        <div
          key={index}
          className="
          w-75
          flex flex-col
          items-center
          text-center
          p-9
          rounded-2xl
          shadow-xl
          transition-transform duration-300
          hover:-translate-y-3
          bg-white
          gap-4
          "
        >
          <img src={offer.img} alt={offer.title} className="w-35 mx-auto" />

          <h3 className="font-bold text-lg text-[#333128]">{offer.title}</h3>

          <p className="text-[#8dc84b] font-semibold text-[15px]">
            {offer.type}
          </p>
        </div>
      ))}
    </section>
  );
};
