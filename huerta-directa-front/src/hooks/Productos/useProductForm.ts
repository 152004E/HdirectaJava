import { useState } from "react";
import Swal from "sweetalert2";
import { createProduct } from "../../services/productService";

export const useProductForm = () => {
  const [formData, setFormData] = useState({
    name: "",
    price: "",
    category: "Frutas",
    description: "",
    stock: "",
  });

  const [loading, setLoading] = useState(false);

  const submitProduct = async (form: FormData) => {
    setLoading(true);

    try {
      const result = await createProduct(form);

      Swal.fire({
        icon: "success",
        title: "Producto publicado correctamente",
        showConfirmButton: false,
        timer: 1800,
      });

      return result;
    } catch (error: any) {
      Swal.fire({
        icon: "error",
        title: "Error",
        text: error.message,
      });

      throw error;
    } finally {
      setLoading(false);
    }
  };

  return {
    formData,
    setFormData,
    loading,
    submitProduct,
  };
};