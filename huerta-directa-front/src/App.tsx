import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import Login from "./pages/Auth/Login";
import AuthLayout from "./layout/AuthLayout";
import MainLayout from "./layout/MainLayout";
import DashboardLayout from "./layout/DashboardLayout";
import PaymentLayout from "./layout/PaymentLayaout";
import { Landing } from "./pages/Landing/Landing";
import { HomePage } from "./pages/Main/HomePage";
import { Dashboard } from "./pages/Dashboard/Dashboard";
import QuienesSomos from "./pages/QuienesSomos/QuienesSomos";
import CheckoutSummaryPage from "./pages/Payment/CheckoutSummaryPage";
import { CartProvider } from "./contexts/CartContext";
import { PaymentProvider } from "./contexts/PaymentContext";
import MercadoPagoPayment from "./pages/Payment/MercadoPagoPayment.tsx";

function App() {
  return (
    <CartProvider>
      <PaymentProvider>
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
        </Route>

        {/* Payment Layout */}
        <Route element={<PaymentLayout />}>
          <Route path="/payment/checkout" element={<CheckoutSummaryPage />} />
          <Route path="/payment/MercadoPayment" element={<MercadoPagoPayment />} />
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
      </PaymentProvider>
    </CartProvider>
  );
}

export default App;
