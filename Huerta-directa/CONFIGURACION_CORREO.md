# 📧 Configuración de Variables de Entorno para Envío de Correos

## 🚨 Problema Solucionado

Antes, las credenciales de correo estaban hardcodeadas en el código, lo que causaba que **el envío de correos no funcionara en producción** (Railway).

Ahora usamos **variables de entorno**, lo que permite:
- ✅ Mantener las credenciales seguras
- ✅ Diferentes configuraciones para desarrollo y producción
- ✅ Fácil configuración en Railway sin modificar código

---

## 🛠️ Configuración en Railway

### 📋 Paso 1: Obtener una Contraseña de Aplicación de Gmail

1. Ve a tu cuenta de Google: https://myaccount.google.com/security
2. **Activa "Verificación en 2 pasos"** (si no está activada)
3. Busca **"Contraseñas de aplicaciones"**
4. Selecciona:
   - **App**: Correo
   - **Dispositivo**: Otro (nombre personalizado)
   - Nombre sugerido: `Railway Huerta Directa`
5. **Copia la contraseña generada** (sin espacios)
   - Ejemplo: `abcd efgh ijkl mnop` → Usa: `abcdefghijklmnop`

### 📋 Paso 2: Configurar Variables en Railway

1. Abre tu proyecto en Railway
2. Selecciona tu servicio (backend)
3. Ve a la pestaña **"Variables"**
4. Agrega las siguientes variables:

```
MAIL_SMTP_HOST=smtp.gmail.com
MAIL_SMTP_PORT=587
MAIL_SMTP_USERNAME=hdirecta@gmail.com
MAIL_SMTP_PASSWORD=tu_contraseña_de_aplicacion_aqui
```

### 📋 Paso 3: Redeploy

1. Después de agregar las variables, Railway hará un **redeploy automático**
2. Si no, puedes forzarlo desde la consola de Railway

---

## 🧪 Verificar que Funciona

### En Desarrollo (Local)

Las credenciales ya están configuradas en `application-dev.properties`. Solo asegúrate de que:
- El perfil activo sea `dev` en `application.properties`
- Puedes probar el envío de correos localmente

### En Producción (Railway)

1. Una vez configuradas las variables, prueba:
   - Registro de un nuevo usuario (debería enviar correo de bienvenida)
   - Login con código de verificación por correo
   - Recuperación de contraseña

2. **Si no funciona**, verifica los logs en Railway:
   - Ve a tu servicio → Pestaña "Logs"
   - Busca errores relacionados con `mail` o `smtp`
   - Los errores comunes son:
     - ❌ Contraseña incorrecta
     - ❌ Verificación en 2 pasos no activada
     - ❌ Variables no configuradas correctamente

---

## 🔒 Seguridad

✅ **Qué SÍ hacer:**
- Usar contraseñas de aplicación de Gmail
- Mantener las credenciales en variables de entorno
- Nunca commitear credenciales al repositorio

❌ **Qué NO hacer:**
- NO uses tu contraseña normal de Gmail
- NO compartas las credenciales en el código
- NO subas archivos `.env` al repositorio (ya está en `.gitignore`)

---

## 📁 Archivos Modificados

- ✅ `application-dev.properties` - Credenciales para desarrollo local
- ✅ `application-prod.properties` - Referencias a variables de entorno
- ✅ `LoginController.java` - Usa `@Value` para inyectar credenciales
- ✅ `UserController.java` - Usa `@Value` para inyectar credenciales
- ✅ `PaymentController.java` - Usa `@Value` para inyectar credenciales

---

## 📞 Soporte

Si tienes problemas:
1. Verifica que las variables estén bien configuradas en Railway
2. Revisa los logs de Railway
3. Asegúrate de que la contraseña de aplicación esté correcta
4. Prueba primero en local para descartar problemas de código

---

**Última actualización:** 2026-03-15
