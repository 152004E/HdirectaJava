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
import { AdminStats } from "./pages/DashboardAdmin/AdminStats";
import { AdminUsers } from "./pages/DashboardAdmin/AdminUsers";
import { AdminProducts } from "./pages/DashboardAdmin/AdminProducts";
import { AdminReports } from "./pages/DashboardAdmin/AdminReports";
import { AdminConfig } from "./pages/DashboardAdmin/AdminConfig";
import { AdminRegister } from "./pages/DashboardAdmin/AdminRegister";
import QuienesSomos from "./pages/About/QuienesSomos";
import CategoryPage from "./pages/Main/CategoryPage/CategoryPage";
import ProductDetailPage from "./pages/AboutProduct/ProductDetailPage";
import { CartProvider } from "./contexts/CartContext";

function App() {
  return (
    <CartProvider>
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

          {/* Home, Quienes Somos, Categorias, Productos */}
          <Route
            element={
              <MainLayout
                navbarProps={{
                  showInicio: true,
                  showProductos: true,
                  showCategorias: true,
                  showAddProduct: true,
                  showCart: true,
                  showProfile: true,
                  showQuienesSomos: true,
                }}
              />
            }
          >
            <Route path="/QuienesSomos" element={<QuienesSomos />} />
            <Route path="/HomePage" element={<HomePage />} />
            <Route path="/categoria/:categorySlug" element={<CategoryPage />} />
            <Route path="/producto/:id" element={<ProductDetailPage />} />
          </Route>

          {/* Dashboard Layout */}
          <Route element={<DashboardLayout />}>
            <Route path="/dashboard" element={<Dashboard />} />
            <Route path="/DashBoardGraficos" element={<DashboardGraficos />} />
            <Route path="/MensajesAreaSocial" element={<MensajesAreaSocial />} />
            <Route
              path="/DashBoardAgregarProducto"
              element={<DashboardAgregarProducto />}
            />
            <Route
              path="/actualizacionUsuario"
              element={<ActualizacionUsuario />}
            />
          </Route>

          {/* Admin Dashboard Layout */}
          <Route element={<AdminDashboardLayout />}>
            <Route path="/admin-dashboard" element={<DashboardAdmin />} />
            <Route path="/admin/stats" element={<AdminStats />} />
            <Route path="/admin/usuarios" element={<AdminUsers />} />
            <Route path="/admin/productos" element={<AdminProducts />} />
            <Route path="/admin/reportes" element={<AdminReports />} />
            <Route path="/admin/config" element={<AdminConfig />} />
            <Route path="/admin/registrar" element={<AdminRegister />} />
          </Route>

          {/* Layout auth */}
          <Route element={<AuthLayout />}>
            <Route path="/login" element={<Login />} />
            <Route path="/register" element={<Login />} />
          </Route>

          {/* Ruta comodín */}
          <Route path="*" element={<Navigate to="/HomePage" />} />
        </Routes>
      </BrowserRouter>
    </CartProvider>
  );
}


export default App;
