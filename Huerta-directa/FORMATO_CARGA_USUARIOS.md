# 📋 Formato de Archivo para Carga Masiva de Usuarios

## 📁 Formatos Soportados
- CSV (.csv)
- Excel (.xlsx, .xls)

## 🗂️ Estructura del Archivo

### Columnas Requeridas (en orden):
1. **Nombre** (obligatorio)
2. **Email** (obligatorio)
3. **Contraseña** (opcional, se asigna "123456" por defecto)
4. **ID Rol** (opcional, se asigna 2 por defecto)

### Valores de Rol:
- `1` = Administrador
- `2` = Cliente (por defecto)

## 📊 Ejemplo CSV:

```csv
Nombre,Email,Contraseña,ID Rol
Juan Pérez,juan@email.com,mipassword,2
María García,maria@email.com,,2
Admin Usuario,admin@email.com,admin123,1
```

## 📝 Ejemplo Excel:

| A (Nombre)     | B (Email)        | C (Contraseña) | D (ID Rol) |
|----------------|------------------|----------------|------------|
| Juan Pérez     | juan@email.com   | mipassword     | 2          |
| María García   | maria@email.com  |                | 2          |
| Admin Usuario  | admin@email.com  | admin123       | 1          |

## ⚠️ Notas Importantes:
- La primera fila se considera encabezado y se omite
- Emails duplicados se omiten automáticamente
- Si falta contraseña, se asigna "123456"
- Si falta rol, se asigna rol de Cliente (2)
- Campos de Nombre y Email son obligatorios

## 🔄 Proceso de Importación:
1. Validación de formato de archivo
2. Procesamiento fila por fila
3. Validación de datos requeridos
4. Verificación de emails duplicados
5. Creación de usuarios válidos
6. Reporte de resultados con estadísticas
