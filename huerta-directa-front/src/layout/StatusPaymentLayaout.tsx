import { Outlet } from 'react-router-dom';
import { Navbar } from '../components/GlobalComponents/Navbar';
import { Footer } from '../components/GlobalComponents/Footer';

const StatusPaymentLayout = () => {
  return (
    <div className="min-h-screen bg-[#F5F0E8] flex flex-col">
      {/* Navbar */}
      <div className="w-full max-w-330 mx-auto px-4">
        <Navbar />
      </div>

      {/* Main Content */}
      <main className="flex-1 w-full">
        <Outlet />
      </main>

      {/* Footer */}
      <Footer />
    </div>
  );
};

export default StatusPaymentLayout;

