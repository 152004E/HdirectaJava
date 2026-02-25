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
│  │  └─ logo_huerta.png                ✓ Logo principal
│  │
│  ├─ components/                       ← Componentes reutilizables UI
│  │  ├─ Auth/                          ← Componentes de autenticación
│  │  │  └─ (vacío - en desarrollo)     ⏳ Componentes auth por implementar
│  │  │
│  │  └─ GlobalComponents/              ← Componentes globales
│  │     ├─ Button.tsx                  ✓ Botón reutilizable
│  │     ├─ Footer.tsx                  ✓ Pie de página
│  │     ├─ Loader.tsx                  ✓ Componente de carga
│  │     ├─ Modal.tsx                   ✓ Modal genérico
│  │     ├─ Navbar.tsx                  ✓ Barra de navegación
│  │     └─ ProfileMenu.tsx             ✓ Menú de perfil
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
│  │  └─ MainLayout.tsx                 ✓ Layout principal
│  │
│  ├─ pages/                            ← Vistas (rutas)
│  │  ├─ Auth/                          ← Páginas de autenticación
│  │  │  ├─ Login.tsx                   ✓ Página de login
│  │  │  └─ Login.css                   ✓ Estilos de login
│  │  │
│  │  ├─ Landing/                       ← Páginas públicas
│  │  │  └─ Landing.tsx                 ✓ Landing page / Home
│  │  │
│  │  └─ Main/                          ← Páginas principales (autenticadas)
│  │     └─ HomePage.tsx                ✓ Home page del usuario
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
- [ ] Implementar Dashboard de usuario
- [ ] Agregar más páginas de navegación
- [ ] Mejorar sistema de componentes

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
