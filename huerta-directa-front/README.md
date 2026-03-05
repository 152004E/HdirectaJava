# 🌱 Huerta Directa - Frontend

## 📁 Estructura del proyecto (Frontend Architecture)

### **Arquitectura por capas del frontend**

```
PAGES (Vistas)
   ↓
COMPONENTS (UI reutilizable)
   ↓
LAYOUTS (Estructura base)
   ↓
ASSETS (Recursos estáticos)
```

---

## 📂 Estructura de archivos completa

```
huerta-directa-front/
├─ public/                              ← Archivos estáticos públicos
│
├─ src/
│  ├─ assets/                           ← Imágenes, logos, íconos
│  │  ├─ logo_huerta.png                ✓ Logo principal
│  │  └─ image/                         ← Imágenes de productos y UI
│  │     ├─ 1.png                       ✓ Imagen producto 1
│  │     ├─ ImagenHover-modified.png    ✓ Imagen hover modificada
│  │     ├─ pr4.png                     ✓ Imagen producto 4
│  │     ├─ pr5.png                     ✓ Imagen producto 5
│  │     ├─ pr6.png                     ✓ Imagen producto 6
│  │     ├─ rigth.png                   ✓ Imagen decorativa
│  │     └─ oferts/                     ✓ Carpeta con ofertas
│  │
│  ├─ components/                       ← Componentes reutilizables UI
│  │  ├─ Auth/                          ← Componentes de autenticación
│  │  │  └─ (vacío - en desarrollo)     ⏳ Componentes auth por implementar
│  │  │
│  │  ├─ GlobalComponents/              ← Componentes globales
│  │  │  ├─ AdminSidebar.tsx            ✓ Barra lateral admin
│  │  │  ├─ Background.tsx              ✓ Componente de fondo
│  │  │  ├─ Button.tsx                  ✓ Botón reutilizable
│  │  │  ├─ DashboardHeader.tsx         ✓ Encabezado del dashboard
│  │  │  ├─ Footer.tsx                  ✓ Pie de página
│  │  │  ├─ Loader.tsx                  ✓ Componente de carga
│  │  │  ├─ Modal.tsx                   ✓ Modal genérico
│  │  │  ├─ Navbar.tsx                  ✓ Barra de navegación
│  │  │  ├─ PasswordInput.tsx           ✓ Input para contraseña
│  │  │  ├─ ProfileMenu.tsx             ✓ Menú de perfil
│  │  │  ├─ Sidebar.tsx                 ✓ Barra lateral
│  │  │  ├─ ScrollToTop.tsx             ✓ Scroll hacia arriba
│  │  │  ├─ ThemeToggle.tsx             ✓ Toggle para tema oscuro/claro
│  │  │  ├─ Cart/                       ← Componentes del carrito
│  │  │  │  ├─ CartButton.tsx           ✓ Botón del carrito
│  │  │  │  └─ CartDropdown.tsx         ✓ Desplegable del carrito
│  │  │  └─ FloatingButtons/            ← Botones flotantes
│  │  │     ├─ FloatingActionButton.tsx ✓ Botón de acción flotante
│  │  │     └─ FloatingChatButton.tsx   ✓ Botón de chat flotante
│  │  │
│  │  ├─ Home/                          ← Componentes para la página de inicio
│  │  │  ├─ CategoriesSection.tsx       ✓ Sección de categorías
│  │  │  ├─ FiltersBar.tsx              ✓ Barra de filtros
│  │  │  ├─ FiltersPanel.tsx            ✓ Panel de filtros avanzados
│  │  │  ├─ HeaderSection.tsx           ✓ Sección de encabezado
│  │  │  ├─ HeroSlider.tsx              ✓ Carrusel principal
│  │  │  ├─ InformationSection.tsx      ✓ Sección de información
│  │  │  ├─ OffersSection.tsx           ✓ Sección de ofertas
│  │  │  ├─ ProductCard.tsx             ✓ Tarjeta de producto
│  │  │  └─ ProductsSection.tsx         ✓ Sección de productos
│  │  │
│  │  ├─ Checkout/                      ← Componentes del checkout
│  │  │  ├─ CheckoutHeader.tsx          ✓ Encabezado del checkout
│  │  │  ├─ OrderItem.tsx               ✓ Item del pedido
│  │  │  ├─ OrderList.tsx               ✓ Lista de pedidos
│  │  │  ├─ OrderSummaryCard.tsx        ✓ Resumen del pedido
│  │  │  ├─ PaymentMethodCard.tsx       ✓ Tarjeta de método de pago
│  │  │  ├─ SecureFooter.tsx            ✓ Pie de seguridad
│  │  │  └─ ShippingCard.tsx            ✓ Tarjeta de envío
│  │  │
│  │  ├─ AboutComponents/               ← Componentes para About/Quiénes somos
│  │  │  ├─ AboutSection.tsx            ✓ Sección de información
│  │  │  └─ ForumSection.tsx            ✓ Sección de foro
│  │  │
│  │  └─ Modals/                        ← Componentes modales
│  │     ├─ ChatModal.tsx               ✓ Modal de chat
│  │     ├─ EditProductModal.tsx        ✓ Modal para editar productos
│  │     ├─ EditUserModal.tsx           ✓ Modal para editar datos del usuario
│  │     └─ NotifyProducerModal.tsx     ✓ Modal para notificar productores
│  │
│  ├─ font/                             ← Fuentes personalizadas
│  │  └─ Poppins/                       ✓ Fuentes Poppins (19 variantes)
│  │     ├─ OFL.txt                     ✓ Licencia Open Font License
│  │     ├─ Poppins-Black.ttf           ✓ Peso Black
│  │     ├─ Poppins-Bold.ttf            ✓ Peso Bold
│  │     ├─ Poppins-ExtraBold.ttf       ✓ Peso ExtraBold
│  │     ├─ Poppins-Light.ttf           ✓ Peso Light
│  │     ├─ Poppins-Medium.ttf          ✓ Peso Medium
│  │     ├─ Poppins-Regular.ttf         ✓ Peso Regular
│  │     ├─ Poppins-SemiBold.ttf        ✓ Peso SemiBold
│  │     └─ ... (y más variantes)       ✓ Variantes Italic incluidas
│  │
│  ├─ hooks/                            ← Custom React hooks
│  │  ├─ useAuth.ts                     ✓ Hook de autenticación
│  │  ├─ useCart.ts                     ✓ Hook para gestión del carrito
│  │  ├─ usePageTitle.ts                ✓ Hook para título de página
│  │  └─ usePayment.ts                  ✓ Hook para gestión de pagos
│  │
│  ├─ contexts/                         ← Context API para estado global
│  │  ├─ CartContext.tsx                ✓ Context del carrito
│  │  └─ PaymentContext.tsx             ✓ Context de pagos
│  │
│  ├─ layout/                           ← Layouts (estructura base)
│  │  ├─ AdminDashboardLayout.tsx       ✓ Layout para admin dashboard
│  │  ├─ AuthLayout.tsx                 ✓ Layout de autenticación
│  │  ├─ DashboardLayout.tsx            ✓ Layout del dashboard de usuario
│  │  ├─ MainLayout.tsx                 ✓ Layout principal
│  │  └─ PaymentLayaout.tsx             ✓ Layout para checkout/pago
│  │
│  ├─ pages/                            ← Vistas (rutas)
│  │  ├─ Auth/                          ← Páginas de autenticación
│  │  │  ├─ Login.tsx                   ✓ Página de login
│  │  │  └─ Login.css                   ✓ Estilos de login
│  │  │
│  │  ├─ Dashboard/                     ← Páginas del dashboard de usuario
│  │  │  ├─ Dashboard.tsx               ✓ Dashboard principal
│  │  │  ├─ ActualizacionUsuario.tsx    ✓ Actualizar perfil de usuario
│  │  │  ├─ DashboardAgregarProducto.tsx ✓ Agregar nuevo producto
│  │  │  ├─ DashboardGraficos.tsx       ✓ Gráficos y estadísticas
│  │  │  └─ MensajesAreaSocial.tsx      ✓ Área de mensajes sociales
│  │  │
│  │  ├─ DashboardAdmin/                ← Páginas del dashboard admin
│  │  │  ├─ DashboardAdmin.tsx          ✓ Dashboard admin principal
│  │  │  ├─ AdminConfig.tsx             ✓ Configuración general del sistema
│  │  │  ├─ AdminProducts.tsx           ✓ Gestión de productos
│  │  │  ├─ AdminRegister.tsx           ✓ Registro de nuevos admins
│  │  │  ├─ AdminReports.tsx            ✓ Reportes del sistema
│  │  │  ├─ AdminStats.tsx              ✓ Estadísticas generales
│  │  │  └─ AdminUsers.tsx              ✓ Gestión de usuarios
│  │  │
│  │  ├─ Landing/                       ← Páginas públicas
│  │  │  └─ Landing.tsx                 ✓ Landing page / Home público
│  │  │
│  │  ├─ Main/                          ← Páginas principales (autenticadas)
│  │  │  ├─ HomePage.tsx                ✓ Home page del usuario logueado
│  │  │  └─ CategoryPage/               ← Categorías de productos
│  │  │     └─ CategoryPage.tsx         ✓ Página de categoría de productos
│  │  │
│  │  ├─ About/                         ← Páginas informativas
│  │  │  └─ QuienesSomos.tsx            ✓ Página quiénes somos
│  │  │
│  │  ├─ AboutProduct/                  ← Detalles de productos
│  │  │  └─ ProductDetailPage.tsx       ✓ Página detalle del producto
│  │  │
│  │  └─ Payment/                       ← Páginas de pago/checkout
│  │     ├─ CheckoutSummaryPage.tsx     ✓ Resumen de checkout
│  │     └─ MercadoPagoPayment.tsx      ✓ Página de pago Mercado Pago
│  │
│  ├─ types/                            ← Definiciones de tipos TypeScript
│  │  └─ swiper.d.ts                    ✓ Tipos para Swiper
│  │
│  ├─ App.tsx                           ✓ Componente principal y rutas
│  ├─ main.tsx                          ✓ Entry point de la aplicación
│  └─ index.css                         ✓ Tailwind base y estilos globales
│
├─ .gitignore                           ✓ Exclusiones de git
├─ index.html                           ✓ HTML principal
├─ package.json                         ✓ Dependencias del proyecto
├─ package-lock.json                    ✓ Lock file de dependencias
├─ tsconfig.json                        ✓ Configuración TypeScript
├─ tsconfig.app.json                    ✓ Config TypeScript - aplicación
├─ tsconfig.node.json                   ✓ Config TypeScript - node
├─ vite.config.ts                       ✓ Configuración de Vite
├─ eslint.config.js                     ✓ Configuración de ESLint
└─ README.md                            ✓ Este archivo
```

---

## 📋 Leyenda

- **✓** = Implementado y funcional
- **⏳** = En desarrollo
- **❌** = No implementado

---

## 🎯 Próximas mejoras

- [ ] Expandir componentes de Auth
- [ ] Crear páginas adicionales (Productos, Perfil, etc.)
- [x] Implementar Dashboard de usuario
- [x] Agregar más páginas de navegación (QuienesSomos, Dashboard)
- [x] Mejorar sistema de componentes (Cart, FloatingButtons, Home, Modals)
- [ ] Crear más componentes en Auth/
- [ ] Implementar funcionalidades de búsqueda avanzada
- [ ] Optimizar imágenes y assets

---

## 🚀 Inicio rápido

```bash
# Instalar dependencias
npm install

# Ejecutar en desarrollo
npm run dev

# Build para producción
npm run build

# Preview de producción
npm run preview

# Instalar Swiper (carrusel)
npm install swiper 
```

---

**Última actualización:** 2026-03-02
