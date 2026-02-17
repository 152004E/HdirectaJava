import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './Login.css';

const Login: React.FC = () => {
    const [isActive, setIsActive] = useState(false);
    const [error, setError] = useState<string | null>(null);
    const [success, setSuccess] = useState<string | null>(null);
    const navigate = useNavigate();

    // Register Form State
    const [registerData, setRegisterData] = useState({
        name: '',
        email: '',
        password: '',
    });

    // Login Form State
    const [loginData, setLoginData] = useState({
        email: '',
        password: '',
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
            const response = await fetch('/api/register', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(registerData),
            });

            const data = await response.json();

            if (response.ok) {
                setSuccess('¡Registro exitoso! Por favor revisa tu correo.');
                // Optional: Switch to login view
                setIsActive(false); 
            } else {
                 // Handle validation errors list or single string
                const errorMsg = Array.isArray(data) 
                    ? data.map((err: any) => err.defaultMessage).join(', ') 
                    : (data.message || 'Error en el registro');
                setError(errorMsg);
            }
        } catch (err) {
            setError('Error de conexión con el servidor');
        }
    };

    const handleLoginSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setError(null);
        setSuccess(null);

        try {
            const response = await fetch('/api/loginUser', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(loginData),
            });

            // Handle non-JSON responses (like existing HTML redirects if any slipped through)
            const contentType = response.headers.get("content-type");
            if (!contentType || !contentType.includes("application/json")) {
                 setError("Error: El servidor no devolvió JSON. Verifica el backend.");
                 return;
            }

            const data = await response.json();

            if (response.ok) {
                // Check if redirect is needed (Admin vs User) or SMS verification
                if (data.status === 'verify-sms') {
                     // TODO: Handle SMS verification UI
                     setSuccess('Se requiere verificación SMS (No implementado en React por ahora)');
                } else if (data.redirect) {
                     // This usually won't happen with the new JSON change, but handling legacy logic if any
                     window.location.href = data.redirect;
                } else {
                    // Standard login success
                    // Store user data if needed context/redux
                    console.log('Login success:', data);
                    
                    // Client-side redirect based on role
                    if (data.idRole === 1) {
                         // Admin route (if migrated) or external
                         window.location.href = 'http://localhost:8085/DashboardAdmin'; // Direct backend redirect for now if Admin dashboard isn't React
                    } else {
                         navigate('/'); // Home
                    }
                }
            } else {
                setError(typeof data === 'string' ? data : (data.message || 'Error al iniciar sesión'));
            }
        } catch (err) {
            setError('Error de conexión al iniciar sesión');
        }
    };

    return (
        <div className="flex items-center justify-center min-h-screen bg-[#f7efd8] font-['Poppins']">
            {error && (
                <div className="fixed top-5 right-5 bg-[#dc3545] text-white border border-[#dc3545] rounded-[10px] text-center font-medium w-[300px] text-[13px] p-[15px_20px] z-[1000] shadow-[0_4px_12px_rgba(220,53,69,0.2)] flex items-center justify-between">
                    <span>{error}</span>
                    <button onClick={() => setError(null)} className="ml-2">×</button>
                </div>
            )}
            {success && (
                <div className="fixed top-5 right-5 bg-[#52c41a] text-white border border-[#52c41a] rounded-[10px] text-center font-medium w-[300px] text-[13px] p-[15px] z-[1000] shadow-[0_4px_12px_rgba(0,0,0,0.15)] flex items-center justify-between">
                    <span>{success}</span>
                     <button onClick={() => setSuccess(null)} className="ml-2">×</button>
                </div>
            )}

            <div className={`login-container bg-white rounded-[30px] shadow-[0_5px_15px_rgba(0,0,0,0.35)] ${isActive ? 'active' : ''}`} id="container">
                
                {/* Sign Up Form */}
                <div className="form-container sign-up">
                    <form onSubmit={handleRegisterSubmit} className="bg-white flex items-center justify-center flex-col px-10 h-full">
                        <h1 className="text-2xl font-bold mb-4">Crear cuenta</h1>
                         {/* Icons or Logo here */}
                        <div className="w-full mb-2">
                            <input 
                                type="text" 
                                name="name"
                                placeholder="Nombre de usuario" 
                                value={registerData.name}
                                onChange={handleRegisterChange}
                                className="py-2.5 px-4 w-full my-1.5 border-2 border-[#8dc84b] rounded-[15px] outline-none text-base bg-gray-100"
                                required
                            />
                        </div>
                        <div className="w-full mb-2">
                             <input 
                                type="email" 
                                name="email"
                                placeholder="Correo electrónico" 
                                value={registerData.email}
                                onChange={handleRegisterChange}
                                className="py-2.5 px-4 w-full my-1.5 border-2 border-[#8dc84b] rounded-[15px] outline-none text-base bg-gray-100"
                                required
                            />
                        </div>
                        <div className="w-full mb-2">
                            <input 
                                type="password" 
                                name="password"
                                placeholder="Contraseña" 
                                value={registerData.password}
                                onChange={handleRegisterChange}
                                className="py-2.5 px-4 w-full my-1.5 border-2 border-[#8dc84b] rounded-[15px] outline-none text-base bg-gray-100"
                                required
                            />
                        </div>
                        <button type="submit" className="text-[17px] inline-block py-3 px-8 text-white bg-[#8dc84b] rounded-[15px] mt-2.5 hover:bg-[#004d00] font-semibold uppercase text-xs tracking-wider cursor-pointer transition-all">
                            Registrar
                        </button>
                    </form>
                </div>

                {/* Sign In Form */}
                <div className="form-container sign-in">
                    <form onSubmit={handleLoginSubmit} className="bg-white flex items-center justify-center flex-col px-10 h-full">
                        <h1 className="text-3xl font-bold mb-4">Iniciar Sesión</h1>
                        <div className="w-full mb-2">
                            <input 
                                type="email" 
                                name="email"
                                placeholder="Correo electrónico" 
                                value={loginData.email}
                                onChange={handleLoginChange}
                                className="py-2.5 px-4 w-full my-1.5 border-2 border-[#8dc84b] rounded-[15px] outline-none text-base bg-gray-100"
                                required
                            />
                        </div>
                        <div className="w-full mb-2">
                            <input 
                                type="password" 
                                name="password"
                                placeholder="Contraseña" 
                                value={loginData.password}
                                onChange={handleLoginChange}
                                className="py-2.5 px-4 w-full my-1.5 border-2 border-[#8dc84b] rounded-[15px] outline-none text-base bg-gray-100"
                                required
                            />
                        </div>
                        <a href="/forgot-password" className="text-[#333] text-[13px] no-underline mb-5 hover:text-[#8dc84b]">
                            Olvidaste tu contraseña?
                        </a>
                        <button type="submit" className="text-[17px] inline-block py-3 px-8 text-white bg-[#8dc84b] rounded-[15px] mt-2.5 hover:bg-[#004d00] font-semibold uppercase text-xs tracking-wider cursor-pointer transition-all">
                            Ingresar
                        </button>
                    </form>
                </div>

                {/* Toggle Container */}
                <div className="toggle-container">
                    <div className="toggle">
                        <div className="toggle-panel toggle-left">
                            <h1 className="text-4xl font-bold mb-4">¡Bienvenido de vuelta!</h1>
                            <p className="mb-5">Usa tu información para ingresar</p>
                            <button className="bg-transparent border border-white text-white py-3 px-8 rounded-[15px] font-semibold uppercase text-xs tracking-wider cursor-pointer" onClick={() => setIsActive(false)}>
                                Iniciar Sesión
                            </button>
                        </div>
                        <div className="toggle-panel toggle-right">
                            <h1 className="text-4xl font-bold mb-4">¡Hola!</h1>
                            <p className="mb-5">Regístrate con tu información para ingresar</p>
                            <button className="bg-transparent border border-white text-white py-3 px-8 rounded-[15px] font-semibold uppercase text-xs tracking-wider cursor-pointer" onClick={() => setIsActive(true)}>
                                Registrar
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default Login;
