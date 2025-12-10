const carrito = document.getElementById("carrito");
const elementos1 = document.getElementById("lista-1");
const lista = document.querySelector("#lista-carrito tbody");
const vaciarCarritoBtn = document.querySelector("#vaciar-carrito");

// 🆕 Variable global para el ID del usuario actual
let currentUserId = null;

// 🆕 Array para almacenar productos del carrito
let productosCarrito = [];

// 🆕 Obtener usuario de la sesión al cargar la página
obtenerUsuarioSesion().then(() => {
    cargarEvenetListeners();
});

// 🆕 Función para obtener el usuario de la sesión
async function obtenerUsuarioSesion() {
    try {
        const response = await fetch('/api/login/current');
        if (response.ok) {
            const userData = await response.json();
            currentUserId = userData.id;
            console.log("✅ Usuario de sesión obtenido:", currentUserId);

            // Cargar el carrito específico del usuario
            cargarCarritoDesdeLocalStorage();
        } else {
            console.log("ℹ️ No hay usuario en sesión, usando modo invitado");
            currentUserId = "guest";

            // Cargar carrito de invitado
            cargarCarritoDesdeLocalStorage();
        }
    } catch (error) {
        console.error("❌ Error obteniendo usuario:", error);
        currentUserId = "guest";
        cargarCarritoDesdeLocalStorage();
    }
}

// 🆕 Función para limpiar carrito al cerrar sesión (llamar desde logout)
function limpiarCarritoSesion() {
    if (currentUserId && currentUserId !== "guest") {
        const claveCarrito = `carrito_${currentUserId}`;
        localStorage.removeItem(claveCarrito);
        console.log(`🧹 Carrito limpiado para usuario: ${currentUserId}`);
    }
    productosCarrito = [];
    actualizarCarritoHTML();
}

function cargarEvenetListeners() {
    if (elementos1) {
        elementos1.addEventListener("click", comprarElemento);
    }
    if (carrito) {
        carrito.addEventListener("click", eliminarElemento);
    }
    if (vaciarCarritoBtn) {
        vaciarCarritoBtn.addEventListener("click", vaciarCarrito);
    }

    console.log("✅ Event listeners del carrito cargados");
}

function comprarElemento(e) {
    e.preventDefault();
    if (e.target.classList.contains("agregar-carrito")) {
        console.log("¡Click en botón agregar!");
        const elemento = e.target.closest(".product");
        leerDatosElemento(elemento);
    }
}

function leerDatosElemento(elemento) {
    try {
        // 1. Subir al contenedor padre
        const producto = elemento.closest('.product');

        if (!producto) {
            console.error("❌ No se encontró el contenedor .product");
            return;
        }

        // 2. Extraer ID del botón Info
        const btnInfo = producto.querySelector('.btnInfo');
        const urlInfo = btnInfo ? btnInfo.href : '';
        const id = urlInfo.match(/\/producto\/(\d+)/)?.[1] || Date.now();

        // 3. Extraer datos del producto
        const nombre = producto.querySelector("h3")?.textContent.trim() || "Sin nombre";
        const precioTexto = producto.querySelector(".precio")?.textContent.trim() || "0";
        const precio = parseFloat(precioTexto.replace(/[^\d]/g, "")) || 0;
        const imagen = producto.querySelector("img")?.src || "/images/default.png";

        // 4. Crear objeto
        const infoElemento = {
            id: id,
            nombre: nombre,
            precio: precio,
            imagen: imagen,
            cantidad: 1
        };

        console.log("📦 Producto leído:", infoElemento);

        // 5. Agregar al carrito
        agregarAlCarrito(infoElemento);

    } catch (error) {
        console.error("❌ Error al leer datos del producto:", error);
    }
}

// 🆕 Función mejorada para agregar productos
function agregarAlCarrito(producto) {
    // Buscar si el producto ya existe
    const existe = productosCarrito.find(p => p.id === producto.id);

    if (existe) {
        // Si existe, incrementar cantidad
        existe.cantidad++;
    } else {
        // Si no existe, agregarlo
        productosCarrito.push(producto);
    }

    // Actualizar la interfaz
    actualizarCarritoHTML();

    // Guardar en localStorage (respaldo)
    guardarCarritoEnLocalStorage();

    mostrarAlerta("Producto agregado al carrito");
}

// 🆕 Actualizar la tabla HTML del carrito
function actualizarCarritoHTML() {
    // Limpiar la tabla
    lista.innerHTML = "";

    let totalCarrito = 0; // 🆕 Variable para acumular el total

    // Agregar cada producto
    productosCarrito.forEach(producto => {
        const subtotal = producto.precio * producto.cantidad;
        totalCarrito += subtotal; // 🆕 Sumar al total

        const row = document.createElement("tr");
        row.innerHTML = ` 
      <td class=" !p-2">
        <img src="${producto.imagen}" class="w-[60px] h-[55px] rounded-sm">
      </td>
      <td>
        ${producto.nombre}
      </td>
      <td >
        $${producto.precio.toLocaleString('es-CO')}
      </td>
      <td class="!flex !justify-center !items-center">
        <input type="number" value="${producto.cantidad}" min="1" 
               class="cantidad-input  !w-[100px] !px-7" data-id="${producto.id}" 
               style=" height: 20px; text-align: start;">
      </td>
      <td>
        $${subtotal.toLocaleString('es-CO')}
      </td>
      <td class="flex justify-center items-center  ">
        <a href="#" class="borrar !px-2 !py-1 rounded-sm bg-[#90CA50] hover:bg-[#90CA50]/80 transition-all duration-500 " data-id="${producto.id}"><span class="material-symbols-outlined borrar">delete</span></a>
      </td>
    `;
        lista.appendChild(row);
    });

    // 🆕 Actualizar el elemento del total en el HTML
    const totalElement = document.getElementById("total");
    if (totalElement) {
        totalElement.textContent = totalCarrito.toLocaleString('es-CO');
    }

    // Agregar event listeners a los inputs de cantidad
    document.querySelectorAll(".cantidad-input").forEach(input => {
        input.addEventListener("change", cambiarCantidad);
    });
}

// 🆕 Cambiar cantidad de un producto
function cambiarCantidad(e) {
    const id = e.target.getAttribute("data-id");
    const nuevaCantidad = parseInt(e.target.value);

    if (nuevaCantidad > 0) {
        const producto = productosCarrito.find(p => p.id === id);
        if (producto) {
            producto.cantidad = nuevaCantidad;
            actualizarCarritoHTML();
            guardarCarritoEnLocalStorage();
        }
    } else {
        // Si la cantidad es 0 o menor, eliminar el producto
        eliminarProductoPorId(id);
    }
}

function eliminarElemento(e) {
    e.preventDefault();
    if (e.target.classList.contains("borrar")) {
        const id = e.target.getAttribute("data-id");
        eliminarProductoPorId(id);
        mostrarAlertaEliminacion("Producto eliminado del carrito");
    }
}

// 🆕 Eliminar producto por ID
function eliminarProductoPorId(id) {
    productosCarrito = productosCarrito.filter(p => p.id !== id);
    actualizarCarritoHTML();
    guardarCarritoEnLocalStorage();
}

function vaciarCarrito() {
    productosCarrito = [];
    actualizarCarritoHTML();
    guardarCarritoEnLocalStorage();
    mostrarAlertaEliminacion("Carrito vaciado");
}

// 🆕 Guardar en localStorage específico por usuario
function guardarCarritoEnLocalStorage() {
    if (currentUserId) {
        const claveCarrito = `carrito_${currentUserId}`;
        localStorage.setItem(claveCarrito, JSON.stringify(productosCarrito));
        console.log(`💾 Carrito guardado para usuario: ${currentUserId}`);
    }
}

// 🆕 Cargar desde localStorage específico por usuario
function cargarCarritoDesdeLocalStorage() {
    if (currentUserId) {
        const claveCarrito = `carrito_${currentUserId}`;
        const carritoGuardado = localStorage.getItem(claveCarrito);
        if (carritoGuardado) {
            productosCarrito = JSON.parse(carritoGuardado);
            actualizarCarritoHTML();
            console.log(`📂 Carrito cargado para usuario: ${currentUserId}`, productosCarrito);
        } else {
            productosCarrito = [];
            console.log(`📂 Carrito nuevo para usuario: ${currentUserId}`);
        }
    }
}

// 🆕 NUEVA FUNCIÓN: Guardar carrito en sesión del servidor y proceder al pago
async function procederAlPago() {
    if (productosCarrito.length === 0) {
        alert("⚠️ Tu carrito está vacío. Agrega productos primero.");
        return;
    }

    // 🔍 DEBUGGING: Ver qué se va a enviar
    console.log("📦 Productos a enviar:", JSON.stringify(productosCarrito, null, 2));

    try {
        console.log("📤 Enviando carrito al servidor:", productosCarrito);

        const response = await fetch("/carrito/guardar-sesion", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(productosCarrito)
        });

        // 🔍 Ver respuesta del servidor
        const texto = await response.text();
        console.log("📥 Respuesta del servidor (texto):", texto);

        const resultado = JSON.parse(texto);
        console.log("📥 Respuesta parseada:", resultado);

        if (resultado.status === "success") {
            console.log("✅ Carrito guardado en sesión del servidor");
            window.location.href = "/carrito/resumen";
        } else {
            throw new Error(resultado.message || "Error al guardar el carrito");
        }

    } catch (error) {
        console.error("❌ Error completo:", error);
        alert(error.message);
    }
}


// Funciones de alertas (sin cambios)
function mostrarAlerta(mensaje) {
    const alerta = document.createElement("div");
    alerta.textContent = mensaje;
    alerta.style.position = "fixed";
    alerta.style.top = "20px";
    alerta.style.right = "20px";
    alerta.style.backgroundColor = "#28a745";
    alerta.style.color = "#fff";
    alerta.style.padding = "10px 20px";
    alerta.style.borderRadius = "5px";
    alerta.style.boxShadow = "0 2px 6px rgba(0,0,0,0.3)";
    alerta.style.zIndex = "9999";
    alerta.style.fontFamily = "sans-serif";
    alerta.style.transition = "opacity 0.5s ease";
    alerta.style.opacity = "1";

    document.body.appendChild(alerta);

    setTimeout(() => {
        alerta.style.opacity = "0";
        setTimeout(() => alerta.remove(), 500);
    }, 2000);
}

function mostrarAlertaEliminacion(mensaje) {
    const alerta = document.createElement("div");
    alerta.textContent = mensaje;
    alerta.style.position = "fixed";
    alerta.style.top = "20px";
    alerta.style.left = "20px";
    alerta.style.backgroundColor = "#dc3545";
    alerta.style.color = "#fff";
    alerta.style.padding = "10px 20px";
    alerta.style.borderRadius = "5px";
    alerta.style.boxShadow = "0 2px 6px rgba(0,0,0,0.3)";
    alerta.style.zIndex = "9999";
    alerta.style.fontFamily = "sans-serif";
    alerta.style.transition = "opacity 0.5s ease";
    alerta.style.opacity = "1";

    document.body.appendChild(alerta);

    setTimeout(() => {
        alerta.style.opacity = "0";
        setTimeout(() => alerta.remove(), 500);
    }, 2000);
}

// Toggle del carrito (sin cambios)
const iconoCarrito = document.getElementById("icono-carrito");

iconoCarrito.addEventListener("click", function (e) {
    e.stopPropagation();
    carrito.classList.toggle("activo");
});

document.addEventListener("click", function (e) {
    if (!carrito.contains(e.target) && !iconoCarrito.contains(e.target)) {
        carrito.classList.remove("activo");
    }
});

// Swiper (sin cambios)
const swiper = new Swiper(".swiper", {
    loop: true,
    pagination: {
        el: ".swiper-pagination",
    },
});

// Theme toggler (sin cambios)
const themeToggler = document.querySelector(".ajustar .theme-toggler");

themeToggler.addEventListener("click", () => {
    document.body.classList.toggle("dark-theme");
    themeToggler.querySelector("span:nth-child(1)").classList.toggle("active");
    themeToggler.querySelector("span:nth-child(2)").classList.toggle("active");
});

// Sidebar toggle (sin cambios)
let toggle = document.querySelector("#menu-btn");
let sidebar = document.querySelector("aside");
let main = document.querySelector("main");

if (toggle && sidebar && main) {
    toggle.onclick = function () {
        sidebar.classList.toggle("active");
        main.classList.toggle("active");
        console.log("Sidebar toggled");
    };
}

//  para  el  botón  de  perfil
function DesplegarProfile() {
    console.log("🔍 DesplegarProfile clicked");
    const MostrarInfo = document.getElementById("MostrarInfo");

    if (!MostrarInfo) {
        console.error("❌ No se encontró el elemento MostrarInfo");
        return;
    }

    if (MostrarInfo.classList.contains("hidden")) {
        // Si está oculto, mostrarlo
        MostrarInfo.classList.remove("hidden");
        MostrarInfo.classList.add("flex");
        console.log("✅ Perfil desplegado");
    } else {
        // Si está visible, ocultarlo
        MostrarInfo.classList.add("hidden");
        MostrarInfo.classList.remove("flex");
        console.log("✅ Perfil ocultado");
    }
}

// 🆕 Hacer disponible globalmente
window.DesplegarProfile = DesplegarProfile;
