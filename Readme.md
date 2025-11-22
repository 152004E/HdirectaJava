# 🌱 Proyecto en Spring Boot — *Huerta Directa*

Este proyecto es una **recreación** de un trabajo que previamente realizamos en **PHP**, pero en esta ocasión decidimos implementar **Spring Boot** para aprovechar su arquitectura más robusta y su integración con Java.  
El objetivo principal es **aprender y aplicar buenas prácticas** en el desarrollo de aplicaciones empresariales usando Spring.

---

## 🧠 Descripción

El sistema conserva las mismas funcionalidades que el trabajo original en PHP, pero ahora cuenta con:

- ✅ **Backend** desarrollado en Spring Boot.  
- ✅ Mejor **organización del código** siguiendo el patrón **MVC**.  
- ✅ Mayor **escalabilidad y mantenibilidad**.  
- ✅ Soporte para **inyección de dependencias** y **controladores REST**.  

---

## 👥 Integrantes del equipo

- **Emerson Reyes**  
- **Jesús Parra**  
- **Jeferson Sánchez**  
- **Santiago Puetes**

---

## 🧰 Tecnologías utilizadas

| Tecnología | Descripción |
|-------------|-------------|
| **Java 17+** | Lenguaje base del proyecto |
| **Spring Boot** | Framework principal del backend |
| **Maven** | Gestor de dependencias y compilación |
| **MySQL** | Base de datos relacional |
| **Thymeleaf** | Motor de plantillas HTML |
| **Tailwind CSS** | Framework de estilos moderno |
| **Bootstrap** | Librería CSS para diseño responsivo |

---

## ⚙️ Instalación y configuración de Tailwind CSS

A continuación se explican **todos los pasos necesarios** para instalar y ejecutar **Tailwind CSS** dentro del proyecto **Spring Boot**.

---

### 1️⃣ Posicionarse en la raíz del proyecto

Abre una terminal en la carpeta principal del proyecto:

```bash
cd Huerta-directa
```

---

### 2️⃣ Inicializar npm

Crea el archivo `package.json` para manejar las dependencias de Node.js:

```bash
npm init -y
```

---

### 3️⃣ Instalar Tailwind CSS y su CLI

Ejecuta el siguiente comando para instalar Tailwind:

```bash
npm install tailwindcss @tailwindcss/cli
```

---

### 4️⃣ Compilar Tailwind

Ejecuta este comando para generar el archivo `output.css` y mantenerlo actualizado con cada cambio:

```bash
npx @tailwindcss/cli -i ./src/main/resources/static/css/input.css -o ./src/main/resources/static/css/output.css --watch
```

---

## 🚀 Ejecución completa del proyecto

Sigue los pasos a continuación para ejecutar **Huerta Directa** correctamente.

---

### 🧩 Clonar el repositorio

```bash
git clone https://github.com/152004E/HdirectaJava.git
```

---

### 📁 Entrar en la carpeta del proyecto

```bash
cd HdirectaJava/Huerta-directa
```

---

### 🎨 Compilar Tailwind (mantener abierto el proceso)

```bash
npx @tailwindcss/cli -i ./src/main/resources/static/css/input.css -o ./src/main/resources/static/css/output.css --watch
```

---

### 🔧 Ejecutar el servidor de Spring Boot

```bash
mvn spring-boot:run
```

---

### 🌐 Abrir en el navegador

```bash
http://localhost:8080

```

### 🌐 application.properties


spring.application.name=Huerta-directa

# Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/huerta_directa
spring.datasource.username=postgres
#spring.datasource.password=2424




spring.datasource.driver-class-name=org.postgresql.Driver

# JPA Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.jdbc.lob.non_contextual_creation=true
spring.jpa.properties.hibernate.temp.use_jdbc_metadata_defaults=false
# Desactiva cach? para ver cambios sin reiniciar
spring.thymeleaf.cache=false

# Ubicaci?n y extensi?n por defecto
spring.thymeleaf.prefix=classpath:/templates/
spring.thymeleaf.suffix=.html

# (Opcional) Modo de plantillas
spring.thymeleaf.mode=HTML

logging.level.org.thymeleaf=TRACE

# Ruta para subir im�genes
upload.path=C:/HuertaUploads

# Tama�o m�ximo de archivo individual
spring.servlet.multipart.max-file-size=10MB
# Tama�o m�ximo de request completo
spring.servlet.multipart.max-request-size=10MB

# Puerto salida
server.port=8085

# Access Token de PRUEBA (para desarrollo)
mercadopago.access_token=TEST-2739771912434898-111023-440e0fc48167af26ffe666b77d3d947f-2272938327

# URLs de retorno (ajusta el puerto si es necesario)
mercadopago.success_url=http://localhost:8085/payment/success
mercadopago.failure_url=http://localhost:8085/payment/failure
mercadopago.pending_url=http://localhost:8085/payment/pending

# IA
token de grok = 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