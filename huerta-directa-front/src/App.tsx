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
        <Route element={<MainLayout />}>
          <Route path="/" element={<Landing />} />
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