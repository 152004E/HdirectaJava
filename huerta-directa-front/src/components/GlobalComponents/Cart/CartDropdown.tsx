import { Button } from "../Button";

interface Props {
  open: boolean;
}

export const CartDropdown = ({ open }: Props) => {
  return (
    <div
      className={`
      absolute right-0 mt-3
      w-162.5
      bg-[#4e4b5076]
      backdrop-blur-md
      rounded-xl
      p-5
      transition-all duration-500
      ${
        open
          ? "opacity-100 visible translate-y-0 pointer-events-auto"
          : "opacity-0 invisible -translate-y-3 pointer-events-none"
      }
      `}
    >
      <table className="w-full text-[13px] text-white">
        <thead>
          <tr className="flex justify-around uppercase mb-4">
            <th>Imagen</th>
            <th>Nombre</th>
            <th>Precio</th>
            <th>Unidad</th>
            <th>Subtotal</th>
          </tr>
        </thead>

        <tbody className="flex flex-col gap-3">
          {/* EJEMPLO ITEM */}
          <tr className="flex justify-around items-center bg-gray-400/70 rounded-xl h-20 transition hover:bg-gray-500/70">
            <td className="w-20 flex justify-center">
              <img
                src="https://via.placeholder.com/50"
                className="rounded-md"
              />
            </td>
            <td className="w-25 text-center">Manzana</td>
            <td className="w-20 text-center">$1.000</td>
            <td className="w-20 text-center">1</td>
            <td className="w-20 text-center">$1.000</td>
          </tr>
        </tbody>
      </table>

      {/* Footer */}
      <div className="flex justify-between items-center mt-8">
        <Button
          text="Vaciar Carrito"
          className="bg-gray-500/30 hover:bg-gray-500/50 px-4 py-2 rounded-md"
        />

        <div className="flex items-center gap-5">
          <p>Total: $1.000</p>
          <Button
            text="Proceder al Pago"
            className="bg-[#8cc63f] hover:bg-[#6da82f] px-5 py-2 rounded-md text-white"
          />
        </div>
      </div>
    </div>
  );
};
