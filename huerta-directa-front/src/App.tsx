import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import Login from "./pages/Auth/Login";
import AuthLayout from "./layout/AuthLayout";
import MainLayout from "./layout/MainLayout";

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route element={<MainLayout />}>
          <Route path="/" element />
          <Route path="*" element={<Navigate to="/" />} />
        </Route>
      </Routes>
      <Route element={<AuthLayout />}>
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Login />} />
        {/* <Route path="/register" element={<Register />} />
          <Route path="/ForgotPassword" element={<ForgotPassword />} /> */}
      </Route>
    </BrowserRouter>
  );
}

export default App;
