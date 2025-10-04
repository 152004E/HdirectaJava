const themeToggler = document.querySelector('.theme-toggler');

// Función para aplicar el tema guardado
function applySavedTheme() {
    const savedTheme = localStorage.getItem('theme'); // Intenta obtener el tema guardado

    if (savedTheme === 'dark') {
        document.body.classList.add('dark-theme');
        // Asegúrate de que los íconos también reflejen el tema oscuro
        if (themeToggler) { // Verifica si themeToggler existe antes de intentar acceder a sus hijos
            themeToggler.querySelector('span:nth-child(1)').classList.remove('active');
            themeToggler.querySelector('span:nth-child(2)').classList.add('active');
        }
    } else {
        document.body.classList.remove('dark-theme');

        if (themeToggler) { // Verifica si themeToggler existe
            themeToggler.querySelector('span:nth-child(1)').classList.add('active');
            themeToggler.querySelector('span:nth-child(2)').classList.remove('active');
        }
    }
}

// Llama a la función al cargar la página para aplicar el tema guardado
document.addEventListener('DOMContentLoaded', applySavedTheme);


// Event listener para el cambio de color
if (themeToggler) { // Asegúrate de que themeToggler existe antes de agregar el event listener
    themeToggler.addEventListener('click', () => {
        document.body.classList.toggle('dark-theme');

        // Alternar la clase 'active' en los span del toggler
        themeToggler.querySelector('span:nth-child(1)').classList.toggle('active');
        themeToggler.querySelector('span:nth-child(2)').classList.toggle('active');

        // Guardar la preferencia en localStorage
        if (document.body.classList.contains('dark-theme')) {
            localStorage.setItem('theme', 'dark');
        } else {
            localStorage.setItem('theme', 'light');
        }
    });
}
// Hover dinámico en los enlaces del sidebar
let list = document.querySelectorAll("aside .sidebar a");

function activeLink() {
    list.forEach((item) => {
        item.classList.remove("hovered");
    });
    this.classList.add("hovered");
}

list.forEach((item) => item.addEventListener("mouseover", activeLink));


// Resaltar el elemento seleccionado en el sidebar
let sidebarLinks = document.querySelectorAll("aside .sidebar a");

sidebarLinks.forEach(link => {
    link.addEventListener("click", function () {
        sidebarLinks.forEach(item => item.classList.remove("active"));
        this.classList.add("active");
    });
});


/*
// filtro del dashboard
function toggleFiltro() {
    const filtro = document.getElementById('bloque-filtro');
    filtro.classList.toggle('d-none');
}

function cerrarFiltro() {
    const filtro = document.getElementById('bloque-filtro');
    filtro.classList.add('d-none');
    // Aquí puedes agregar código para limpiar los inputs si lo deseas
}
*/


// Función para actualizar producto
function actualizarProducto(id) {
    // Redirigir a la página de edición con el ID del producto
    window.location.href = `/editar_producto/${id}`;
}

// Función para eliminar producto
function eliminarProducto(id) {
    // Mostrar confirmación antes de eliminar
    if (confirm('¿Estás seguro de que deseas eliminar este producto?')) {
        // Realizar petición DELETE al controlador
        fetch(`/api/productos/${id}`, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            }
        })
        .then(response => {
            if (response.ok) {
                alert('Producto eliminado exitosamente');
                // Recargar la página para actualizar la tabla
                window.location.reload();
            } else {
                alert('Error al eliminar el producto');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Error al eliminar el producto');
        });
    }
}

// Alternativa: Eliminar mediante formulario (si prefieres POST en lugar de DELETE)
function eliminarProductoForm(id) {
    if (confirm('¿Estás seguro de que deseas eliminar este producto?')) {
        // Crear un formulario dinámicamente
        const form = document.createElement('form');
        form.method = 'POST';
        form.action = `/eliminar_producto/${id}`;
        document.body.appendChild(form);
        form.submit();
    }
}

// ===============================
// FUNCIONES PARA ALERTAS MODERNAS
// ===============================

// Función para cerrar alertas manualmente
function closeAlert(alertId) {
    const alert = document.getElementById(alertId);
    if (alert) {
        alert.classList.add('closing');
        setTimeout(() => {
            alert.style.display = 'none';
        }, 300);
    }
}

// Función para auto-ocultar alertas después de 8 segundos
function autoHideAlerts() {
    const alerts = document.querySelectorAll('.modern-alert');
    alerts.forEach(alert => {
        // Agregar clase para animación automática
        alert.classList.add('auto-hide');

        // Remover el elemento después de 8 segundos
        setTimeout(() => {
            if (alert.style.display !== 'none') {
                alert.classList.add('closing');
                setTimeout(() => alert.style.display = 'none', 300);
            }
        }, 8000);
    });
}

// Función para mostrar alerta de éxito dinámicamente (opcional para uso futuro)
function showSuccessAlert(message) {
    const alertHTML = `
        <div class="modern-alert success-alert auto-hide" id="dynamicSuccessAlert">
            <div class="alert-content">
                <div class="alert-icon">
                    <i class="fas fa-check-circle"></i>
                </div>
                <div class="alert-message">
                    <h4>¡Éxito!</h4>
                    <p>${message}</p>
                </div>
                <button class="alert-close" onclick="closeAlert('dynamicSuccessAlert')">
                    <i class="fas fa-times"></i>
                </button>
            </div>
        </div>
    `;

    document.body.insertAdjacentHTML('beforeend', alertHTML);

    // Auto-hide después de 8 segundos
    setTimeout(() => {
        const alert = document.getElementById('dynamicSuccessAlert');
        if (alert && alert.parentNode) {
            alert.remove();
        }
    }, 8000);
}

// Función para mostrar alerta de error dinámicamente (opcional para uso futuro)
function showErrorAlert(message) {
    const alertHTML = `
        <div class="modern-alert error-alert auto-hide" id="dynamicErrorAlert">
            <div class="alert-content">
                <div class="alert-icon">
                    <i class="fas fa-exclamation-triangle"></i>
                </div>
                <div class="alert-message">
                    <h4>Error</h4>
                    <p>${message}</p>
                </div>
                <button class="alert-close" onclick="closeAlert('dynamicErrorAlert')">
                    <i class="fas fa-times"></i>
                </button>
            </div>
        </div>
    `;

    document.body.insertAdjacentHTML('beforeend', alertHTML);

    // Auto-hide después de 8 segundos
    setTimeout(() => {
        const alert = document.getElementById('dynamicErrorAlert');
        if (alert && alert.parentNode) {
            alert.remove();
        }
    }, 8000);
}

// Inicializar alertas cuando el DOM esté listo
document.addEventListener('DOMContentLoaded', function() {
    // Auto-ocultar alertas existentes
    autoHideAlerts();

    // Agregar efecto de sonido opcional (comentado por defecto)
    // const successAlert = document.querySelector('.success-alert');
    // if (successAlert) {
    //     // Reproducir sonido de éxito (requiere archivo de audio)
    //     // const audio = new Audio('/sounds/success.mp3');
    //     // audio.play().catch(e => console.log('No se pudo reproducir el sonido'));
    // }
});

// Cerrar alertas con tecla Escape
document.addEventListener('keydown', function(event) {
    if (event.key === 'Escape') {
        const alerts = document.querySelectorAll('.modern-alert');
        alerts.forEach(alert => {
            alert.classList.add('closing');
            setTimeout(() => {
                if (alert && alert.parentNode) {
                    alert.remove();
                }
            }, 300);
        });
    }
});
