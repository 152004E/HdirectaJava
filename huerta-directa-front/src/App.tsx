import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import Login from "./pages/Auth/Login";
import AuthLayout from "./layout/AuthLayout";
import MainLayout from "./layout/MainLayout";
import DashboardLayout from "./layout/DashboardLayout";
import { Landing } from "./pages/Landing/Landing";
import { HomePage } from "./pages/Main/HomePage";
import { Dashboard } from "./pages/Dashboard/Dashboard";
import { DashboardGraficos } from "./pages/Dashboard/DashboardGraficos";
import { MensajesAreaSocial } from "./pages/Dashboard/MensajesAreaSocial";
import { DashboardAgregarProducto } from "./pages/Dashboard/DashboardAgregarProducto";
import { ActualizacionUsuario } from "./pages/Dashboard/ActualizacionUsuario";
import { DashboardAdmin } from "./pages/DashboardAdmin/DashboardAdmin";
import { AdminDashboardLayout } from "./layout/AdminDashboardLayout";
import QuienesSomos from "./pages/QuienesSomos/QuienesSomos";

function App() {
  return (
    <BrowserRouter>
      <Routes>
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

        {/* Quienes Somos */}      
        <Route
          element={
            <MainLayout
              navbarProps={{
                showQuienesSomos: true,
              }}
            />
          }
        >
          <Route path="/QuienesSomos" element={<QuienesSomos />} />
        </Route>

        {/* Home page */}
        <Route
          element={
            <MainLayout
              navbarProps={{
                showCategorias: true,
                showProductos:true,
                showAddProduct : true,
                showCart: true,
                showProfile: true
              }}
            />
          }
        >
          <Route path="/HomePage" element={<HomePage />} />
        </Route>

        {/* Dashboard Layout */}
        <Route element={<DashboardLayout />}>
          <Route path="/dashboard" element={<Dashboard />} />
          <Route path="/DashBoardGraficos" element={<DashboardGraficos />} />
          <Route path="/MensajesAreaSocial" element={<MensajesAreaSocial />} />
          <Route path="/DashBoardAgregarProducto" element={<DashboardAgregarProducto />} />
          <Route path="/actualizacionUsuario" element={<ActualizacionUsuario />} />
        </Route>

        {/* Admin Dashboard Layout */}
        <Route element={<AdminDashboardLayout />}>
          <Route path="/admin-dashboard" element={<DashboardAdmin />} />
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
