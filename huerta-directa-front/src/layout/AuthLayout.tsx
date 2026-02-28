import { Outlet } from "react-router-dom";

const AuthLayout = () => {
  return (
    <div className="flex min-h-screen overflow-hidden overscroll-none items-center justify-center bg-[#FEF5DC] px-4">
      {/*aca entra el login y el register */}
      <Outlet />
    </div>
  );
};

export default AuthLayout;