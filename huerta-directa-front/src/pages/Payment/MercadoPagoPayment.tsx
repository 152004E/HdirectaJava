import { useNavigate } from 'react-router-dom';
import { CheckoutHeader } from '../../components/Checkout/CheckoutHeader';
import { SecureFooter } from '../../components/Checkout/SecureFooter';
import { usePageTitle } from '../../hooks/usePageTitle';
import { useEffect, useRef } from "react";

export const MercadoPagoPayment = () => {
    usePageTitle("MercadoPayment");
    const navigate = useNavigate();
    const containerRef = useRef<HTMLDivElement>(null);

    const CONFIG = {
        PUBLIC_KEY: 'TEST-4d1be8e9-6270-47e0-b83b-b8d27d41bcfe',
        AMOUNT: 100000,  // Temporalmente (después será dinámico del carrito)
        DESCRIPTION: 'Compra Huerta Directa',
        PAYER_EMAIL: 'test@test.com'
    };


    // Si no hay items, redirigir
    // React.useEffect(() => {
    //   if (items.length === 0) {
    //     navigate('Checkout'); //en pruabas uando pasemos ya a tener logiva sera navigate('/HomePage')
    //   }
    // }, [items, navigate]);

    //carga el script de MercadoPago al montar el componente para el brick

    useEffect(() => {
        // Cargar el script dinámicamente
        const script = document.createElement('script');
        script.src = 'https://sdk.mercadopago.com/js/v2';
        script.async = true;
        document.head.appendChild(script);

        return () => {
            // Limpiar cuando el componente se desmonte
            script.remove();
        };
    }, []);

    useEffect(() => {
        // Esperar a que el script de MercadoPago cargue
        const timer = setTimeout(() => {
            const mp = new (window as any).MercadoPago(CONFIG.PUBLIC_KEY, {
                locale: 'es-CO'
            });

            const bricksBuilder = mp.bricks();

            const settings = {
                initialization: {
                    amount: CONFIG.AMOUNT,
                    payer: {
                        email: CONFIG.PAYER_EMAIL
                    }
                },
                customization: {
                    visual: {
                        style: { theme: 'default' }
                    },
                    paymentMethods: {
                        creditCard: 'all',
                        debitCard: 'all',
                        ticket: 'all',
                        bankTransfer: 'all',
                        mercadoPago: 'all',
                        maxInstallments: 12
                    }
                },
                callbacks: {
                    onReady: () => console.log('✅ Brick cargado'),
                    onSubmit: async (data: any) => {
                        console.log('📤 Datos del pago:', data);
                        // Aquí irá el fetch al backend después
                    },
                    onError: (error: any) => console.error('❌ Error:', error)
                }
            };

            if (containerRef.current) {
                bricksBuilder.create('payment', 'paymentBrick_container', settings);
            }
        }, 500);  // Espera 500ms para asegurar que MercadoPago cargó

        return () => clearTimeout(timer);
    }, [CONFIG.PUBLIC_KEY, CONFIG.AMOUNT]);



    return (
        <div className="min-h-screen bg-[#F5F0E8] py-8 px-4">
            <div className="max-w-7xl mx-auto">
                <CheckoutHeader />

                <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
                    <div className="lg:col-span-3 space-y-6">
                        <div
                            ref={containerRef}
                            id="paymentBrick_container"
                            className="bg-white rounded-lg shadow-lg p-6"
                        ></div>
                    </div>
                </div>

                {/* Footer de seguridad */}
                <SecureFooter />

                {/* Botón volver */}
                <div className="mt-8 flex justify-center">
                    <button
                        onClick={() => navigate('/HomePage')}
                        className="px-6 py-2 bg-gray-400 hover:bg-gray-500 text-white font-bold rounded-lg transition-colors"
                    >
                        ← Volver a comprar
                    </button>
                </div>
            </div>
        </div>
    );
};

export default MercadoPagoPayment;

