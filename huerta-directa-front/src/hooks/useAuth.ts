import { useState } from "react";
import { useNavigate } from "react-router-dom";

export const useAuth = () => {
  const [isActive, setIsActive] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [success, setSuccess] = useState<string | null>(null);

  const navigate = useNavigate();

  // Register Form State
  const [registerData, setRegisterData] = useState({
    name: "",
    email: "",
    password: "",
  });

  // Login Form State
  const [loginData, setLoginData] = useState({
    email: "",
    password: "",
  });

  const handleRegisterChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setRegisterData({ ...registerData, [e.target.name]: e.target.value });
  };

  const handleLoginChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setLoginData({ ...loginData, [e.target.name]: e.target.value });
  };

  const handleRegisterSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError(null);
    setSuccess(null);

    try {
      const response = await fetch("/api/login/register", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(registerData),
      });

      const data = await response.json();

      if (response.ok) {
        setSuccess("¡Registro exitoso! Por favor revisa tu correo.");
        setIsActive(false);
      } else {
        const errorMsg = Array.isArray(data)
          ? data.map((err: any) => err.defaultMessage).join(", ")
          : data.message || "Error en el registro";
        setError(errorMsg);
      }
    } catch (err) {
      setError("Error de conexión con el servidor");
    }
  };

  const handleLoginSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError(null);
    setSuccess(null);

    try {
      const response = await fetch("/api/login/loginUser", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(loginData),
      });

      const contentType = response.headers.get("content-type");
      if (!contentType || !contentType.includes("application/json")) {
        setError("Error: El servidor no devolvió JSON.");
        return;
      }

      const data = await response.json();

      if (response.ok) {
        if (data.status === "verify-sms") {
          setSuccess("Se requiere verificación SMS");
        } else if (data.redirect) {
          window.location.href = data.redirect;
        } else {
          if (data.idRole === 1) {
            window.location.href =
              "http://localhost:8085/DashboardAdmin";
          } else {
            navigate("/");
          }
        }
      } else {
        setError(
          typeof data === "string"
            ? data
            : data.message || "Error al iniciar sesión"
        );
      }
    } catch (err) {
      setError("Error de conexión al iniciar sesión");
    }
  };

  return {
    isActive,
    setIsActive,
    error,
    setError,
    success,
    setSuccess,
    registerData,
    loginData,
    handleRegisterChange,
    handleLoginChange,
    handleRegisterSubmit,
    handleLoginSubmit,
  };
};