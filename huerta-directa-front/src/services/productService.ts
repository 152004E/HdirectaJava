import { API_URL } from "../config/api";

export const createProduct = async (formData: FormData) => {
  const response = await fetch(`${API_URL}/api/products/create`, {
    method: "POST",
    body: formData,
    credentials: "include",
  });

  if (!response.ok) {
    const error = await response.text();
    throw new Error(error);
  }

  return response.json();
};