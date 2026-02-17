import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import Login from './pages/Login';

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Login />} />
        <Route path="/" element={
          <div className="p-10 text-center">
            <h1 className="text-3xl font-bold">Bienvenido a Huerta Directa</h1>
            <p className="mt-4">
              <a href="/login" className="text-blue-500 underline">Iniciar Sesión / Registrarse</a>
            </p>
          </div>
        } />
        <Route path="*" element={<Navigate to="/" />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;

