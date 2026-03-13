import React from "react";
import "./Login.css";
import { Background } from "../../components/GlobalComponents/Background";
import logo from "../../assets/logo_huerta.png";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faArrowRight,
  faEnvelope,
  faRightToBracket,
  faUser,
  faUserPlus,
} from "@fortawesome/free-solid-svg-icons";
import { useAuth } from "../../hooks/useAuth";
import { Button } from "../../components/GlobalComponents/Button";
import { usePageTitle } from "../../hooks/usePageTitle";
import { PasswordInput } from "../../components/GlobalComponents/PasswordInput";

const Login: React.FC = () => {
  usePageTitle("Login  ");
  const {
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
  } = useAuth();

  return (
    <main className="flex items-center justify-center min-h-screen bg-[#FEF5DC] dark:bg-[#1A221C]! font-['Poppins'] relative">
      <Background />

      {/* ALERTAS */}
      {error && (
        <div className="fixed top-5 right-5 bg-[#dc3545] text-white border border-[#dc3545] rounded-[10px] text-center font-medium w-75 text-[13px] p-[15px_20px] z-1000 shadow-[0_4px_12px_rgba(220,53,69,0.2)] flex items-center justify-between">
          <span>{error}</span>
          <button onClick={() => setError(null)} className="ml-2">
            ×
          </button>
        </div>
      )}

      {success && (
        <div className="fixed top-5 right-5 bg-[#52c41a] text-white border border-[#52c41a] rounded-[10px] text-center font-medium w-75 text-[13px] p-[15px_20px] z-1000 shadow-[0_4px_12px_rgba(0,0,0,0.15)] flex items-center justify-between">
          <span>{success}</span>
          <button onClick={() => setSuccess(null)} className="ml-2">
            ×
          </button>
        </div>
      )}

      <div
        className={`login-container bg-white dark:bg-[#1f2a22]! overflow-hidden rounded-[30px] shadow-[0_5px_15px_rgba(0,0,0,0.35)] ${
          isActive ? "active" : ""
        }`}
        id="container"
      >
        {/* ================= SIGN UP ================= */}
        <div className="form-container sign-up dark:bg-[#1f2a22] ">
          <form
            onSubmit={handleRegisterSubmit}
            className="bg-white dark:bg-[#1f2a22] w-110 flex items-center justify-center flex-col px-10 h-full"
          >
            <h1 className="text-2xl font-bold dark:text-white">Crear cuenta</h1>

            <img src={logo} alt="Logo huerta directa" className="w-20 mb-4" />

            {/* Nombre */}
            <div className="w-full mb-2">
              <label className="text-sm font-normal block dark:text-gray-300">
                Nombre de usuario
              </label>
              <div className="relative flex items-center">
                <FontAwesomeIcon
                  icon={faUser}
                  className="absolute left-3 text-[#888] dark:text-gray-400"
                />
                <input
                  type="text"
                  name="name"
                  placeholder="Ejemplo: Santiago Puentes"
                  value={registerData.name}
                  onChange={handleRegisterChange}
                  className="py-2.5 pl-10 pr-4 w-full my-1.5 border-2 border-[#8dc84b] dark:border-[#6fa33b] rounded-[15px] outline-none text-base text-[#333128] dark:text-white dark:bg-[#26322a] transition-all duration-500 focus:border-[#004d00] focus:shadow-[0_0_5px_rgba(0,77,0,0.5)]"
                  required
                />
              </div>
            </div>

            {/* Email */}
            <div className="w-full mb-2">
              <label className="text-sm font-normal block dark:text-gray-300">
                Ingrese su correo electrónico
              </label>
              <div className="relative flex items-center">
                <FontAwesomeIcon
                  icon={faEnvelope}
                  className="absolute left-3 text-[#888] dark:text-gray-400"
                />
                <input
                  type="email"
                  name="email"
                  placeholder="Ej: tunombre@correo.com"
                  value={registerData.email}
                  onChange={handleRegisterChange}
                  className="py-2.5 pl-10 pr-4 w-full my-1.5 border-2 border-[#8dc84b] dark:border-[#6fa33b] rounded-[15px] outline-none text-base text-[#333128] dark:text-white dark:bg-[#26322a] transition-all duration-500 focus:border-[#004d00] focus:shadow-[0_0_5px_rgba(0,77,0,0.5)]"
                  required
                />
              </div>
            </div>

            {/* Password */}
            <div className="w-full mb-2">
              <label className="text-sm font-normal block dark:text-gray-300!">
                Ingrese su contraseña
              </label>

              <PasswordInput
                name="password"
                value={registerData.password}
                onChange={handleRegisterChange}
                placeholder="Contraseña"
                required
              />
            </div>

            <Button
              text="Registrar"
              iconRight={faUserPlus}
              type="submit"
              className="text-[17px] inline-block py-3 px-8 text-white bg-[#8dc84b] rounded-[15px] transition-all duration-500 mt-2.5 hover:bg-[#004d00] font-semibold uppercase text-xs tracking-wider cursor-pointer"
            />
          </form>
        </div>

        {/* ================= SIGN IN ================= */}
        <div className="form-container sign-in dark:bg-[#1f2a22]">
          <form
            onSubmit={handleLoginSubmit}
            className="bg-white dark:bg-[#1f2a22] w-100 flex items-center justify-center flex-col px-10 h-full "
          >
            <h1 className="text-3xl font-bold mb-4 dark:text-white">
              Iniciar Sesión
            </h1>

            <img src={logo} alt="Logo huerta directa" className="w-20 mb-4" />

            {/* Email */}
            <div className="w-full mb-2">
              <label className="text-sm font-normal block dark:text-gray-300">
                Ingrese su correo electrónico
              </label>
              <div className="relative flex items-center">
                <FontAwesomeIcon
                  icon={faEnvelope}
                  className="absolute left-3 text-[#888] dark:text-gray-400"
                />
                <input
                  type="email"
                  name="email"
                  placeholder="Correo electrónico"
                  value={loginData.email}
                  onChange={handleLoginChange}
                  className="py-2.5 pl-10 pr-4 w-full my-1.5 border-2 border-[#8dc84b] dark:border-[#6fa33b] rounded-[15px] outline-none text-base text-[#333128] dark:text-white dark:bg-[#26322a] transition-all duration-500 focus:border-[#004d00] focus:shadow-[0_0_8px_rgba(0,77,0,0.4)]"
                  required
                />
              </div>
            </div>

            {/* Password */}
            <div className="w-full mb-2">
              <label className="text-sm font-normal block dark:text-gray-300">
                Ingrese su contraseña
              </label>

              <PasswordInput
                name="password"
                value={loginData.password}
                onChange={handleLoginChange}
                placeholder="Contraseña"
                required
              />
            </div>

            <a
              href="/forgot-password"
              className="text-[#333] dark:text-gray-300 text-[13px] no-underline mb-5 hover:text-[#8dc84b] transition-colors duration-500"
            >
              ¿Olvidaste tu contraseña?
            </a>

            <Button iconRight={faArrowRight} text="Ingresar" type="submit"  className="text-[17px] inline-block py-3 px-8 text-white bg-[#8dc84b] rounded-[15px] transition-all duration-500 mt-2.5 hover:bg-[#004d00] font-semibold uppercase text-xs tracking-wider cursor-pointer" />
          </form>
        </div>

        {/* ================= TOGGLE ================= */}
        <div className="toggle-container ">
          <div className="toggle ">
            <div className="toggle-panel toggle-left ">
              <h1 className="text-4xl font-bold mb-4">
                ¡Bienvenido de vuelta!
              </h1>
              <p className="mb-5">Usa tu información para ingresar</p>
              <Button
                text="Iniciar Sesión"
                iconRight={faRightToBracket}
                onClick={() => setIsActive(false)}
                className="bg-transparent border border-white text-white py-3! px-8! rounded-[15px] font-semibold uppercase text-xs tracking-wider cursor-pointer transition-all duration-500 hover:bg-white hover:text-[#8dc84b]"
              />
            </div>

            <div className="toggle-panel toggle-right">
              <h1 className="text-4xl font-bold mb-4">¡Hola!</h1>
              <p className="mb-5">
                Regístrate con tu información para ingresar
              </p>
              <Button
                text="Registrar"
                iconRight={faUserPlus}
                onClick={() => setIsActive(true)}
                className="bg-transparent border border-white text-white py-3 px-8 rounded-[15px] font-semibold uppercase text-xs tracking-wider cursor-pointer transition-all duration-500 hover:bg-white hover:text-[#8dc84b]"
              />
            </div>
          </div>
        </div>
      </div>
    </main>
  );
};

export default Login;
