import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import Login from "./pages/Auth/Login";
import AuthLayout from "./layout/AuthLayout";
import MainLayout from "./layout/MainLayout";
import DashboardLayout from "./layout/DashboardLayout";
import { Landing } from "./pages/Landing/Landing";
import { HomePage } from "./pages/Main/HomePage";
import { Dashboard } from "./pages/Dashboard/Dashboard";

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

        {/* Home page */}
        <Route
          element={
            <MainLayout
              navbarProps={{
                showCategorias: true,
                showProductos:true,
                showAddProduct : true,
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
