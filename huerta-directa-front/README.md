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
│  │  │  ├─ ThemeToggle.tsx             ✓ Toggle para tema oscuro/claro
│  │  │  ├─ Cart/                       ← Componentes del carrito
│  │  │  │  ├─ CartButton.tsx           ✓ Botón del carrito
│  │  │  │  └─ CartDropdown.tsx         ✓ Desplegable del carrito
│  │  │  └─ FloatingButtons/            ← Botones flotantes
│  │  │     ├─ FloatingActionButton.tsx ✓ Botón de acción flotante
│  │  │     └─ FloatingChatButton.tsx   ✓ Botón de chat flotante
│  │  │
│  │  ├─ Home/                          ← Componentes para la página de inicio
│  │  │  ├─ HeaderSection.tsx           ✓ Sección de encabezado
│  │  │  ├─ HeroSlider.tsx              ✓ Carrusel principal
│  │  │  ├─ InformationSection.tsx      ✓ Sección de información
│  │  │  ├─ OffersSection.tsx           ✓ Sección de ofertas
│  │  │  └─ ProductCard.tsx             ✓ Tarjeta de producto
│  │  │
│  │  └─ Modals/                        ← Componentes modales
│  │     └─ ChatModal.tsx               ✓ Modal de chat
│  │
│  ├─ font/                             ← Fuentes personalizadas
│  │  └─ Poppins/                       ✓ Fuente Poppins
│  │
│  ├─ hooks/                            ← Custom React hooks
│  │  ├─ useAuth.ts                     ✓ Hook de autenticación
│  │  └─ usePageTitle.ts                ✓ Hook para título de página
│  │
│  ├─ layout/                           ← Layouts (estructura base)
│  │  ├─ AuthLayout.tsx                 ✓ Layout de autenticación
│  │  ├─ DashboardLayout.tsx            ✓ Layout del dashboard
│  │  └─ MainLayout.tsx                 ✓ Layout principal
│  │
│  ├─ pages/                            ← Vistas (rutas)
│  │  ├─ Auth/                          ← Páginas de autenticación
│  │  │  ├─ Login.tsx                   ✓ Página de login
│  │  │  └─ Login.css                   ✓ Estilos de login
│  │  │
│  │  ├─ Dashboard/                     ← Páginas del dashboard
│  │  │  └─ Dashboard.tsx               ✓ Dashboard principal
│  │  │
│  │  ├─ Landing/                       ← Páginas públicas
│  │  │  └─ Landing.tsx                 ✓ Landing page / Home
│  │  │
│  │  ├─ Main/                          ← Páginas principales (autenticadas)
│  │  │  └─ HomePage.tsx                ✓ Home page del usuario
│  │  │
│  │  └─ QuienesSomos/                  ← Páginas informativas
│  │     └─ QuienesSomos.tsx            ✓ Página quiénes somos
│  │
│  ├─ types/                            ← Definiciones de tipos TypeScript
│  │  └─ swiper.d.ts                    ✓ Tipos para Swiper
│  │
│  ├─ App.tsx                           ✓ Componente principal y rutas
│  ├─ main.tsx                          ✓ Entry point de la aplicación
│  └─ index.css                         ✓ Tailwind base y estilos globales
│
├─ index.html                           ✓ HTML principal
├─ package.json                         ✓ Dependencias del proyecto
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


# carrusel
npm install swiper 
```

---




**Última actualización:** 2026-02-23
