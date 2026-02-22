import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import Login from "./pages/Auth/Login";
import AuthLayout from "./layout/AuthLayout";
import MainLayout from "./layout/MainLayout";
import { Landing } from "./pages/Landing/Landing";

function App() {
  return (
    <BrowserRouter>
      <Routes>
        {/* Layout principal */}

        {/* Landing Layout */}
        <Route
          element={
            <MainLayout
              navbarProps={{
                
                showQuienesSomos: true,
              }}
            />
          }
        >
          <Route path="/" element={<Landing />} />
        </Route>

        {/* Dashboard Layout */}
        <Route
          element={
            <MainLayout
              navbarProps={{
                showCategorias: true,
                showProductos: true,
                showAddProduct: true,
                showProfile: true,
              }}
            />
          }
        >
          {/* aquí irían rutas futuras */}
          {/* <Route path="/dashboard" element={<Dashboard />} /> */}
        </Route>

        {/* Layout auth */}
        <Route element={<AuthLayout />}>
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Login />} />
        </Route>

        {/* Ruta comodín */}
        <Route path="*" element={<Navigate to="/" />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
