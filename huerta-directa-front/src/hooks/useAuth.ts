import { useState } from "react";
import { useNavigate } from "react-router-dom";
import authService from "../services/authService";

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
      const response = await authService.register(
        registerData.name,
        registerData.email,
        registerData.password
      );

      setSuccess(response.message || "¡Registro exitoso! Redirigiendo...");
      setRegisterData({ name: "", email: "", password: "" });

      // Redirigir después de un breve delay
      setTimeout(() => {
        navigate("/HomePage");
      }, 1500);
    } catch (err: any) {
      console.error("Error en registro:", err);
      setError(err.message || "Error de conexión con el servidor");
    }
  };

  const handleLoginSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError(null);
    setSuccess(null);

    try {
      const response = await authService.login(
        loginData.email,
        loginData.password
      );

      // Verificar si necesita SMS
      if (response.status === "verify-sms") {
        setSuccess("Se requiere verificación SMS");
        return;
      }

      // Login exitoso
      setSuccess(response.message || "¡Login exitoso!");

      // Redirigir según rol
      setTimeout(() => {
        if (response.redirect) {
          navigate(response.redirect);
        } else if (response.idRole === 1) {
          navigate("/admin-dashboard");
        } else {
          navigate("/dashboard");
        }
      }, 1000);
    } catch (err: any) {
      console.error("Error en login:", err);
      setError(err.message || "Error al iniciar sesión");
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