// Variables para las gráficas
let unitsChart, categoriesChart, pricesChart;

// Función para obtener productos del usuario actual desde la sesión
async function obtenerMisProductos() {
  try {
    const response = await fetch("/api/products/mis-Productos");
    
    if (response.status === 401) {
      // Usuario no autenticado
      window.location.href = "/login?error=session&message=Debe+iniciar+sesion";
      return [];
    }
    
    if (!response.ok) {
      throw new Error("Error al obtener productos");
    }
    
    const productos = await response.json();
    return productos;
  } catch (error) {
    console.error("Error al obtener productos:", error);
    mostrarMensajeError("Error al cargar tus productos. Por favor, recarga la página.");
    return [];
  }
}

// Función para calcular estadísticas de productos del usuario actual
async function calcularEstadisticasProductos() {
  // Obtener productos del usuario actual
  const productos = await obtenerMisProductos();
  
  if (productos.length === 0) {
    mostrarMensajeSinProductos();
    return {
      total: 0,
      activos: 0,
      categoriasCount: 0,
      unidadesCount: 0,
      productosPorCategoria: {},
      productosPorUnidad: {},
      precios: { promedio: 0, maximo: 0, minimo: 0 },
      productos: []
    };
  }

  let total = productos.length;

  // Contar productos activos 
  // Si no tienes campo de estado específico, asume todos activos
  let activos = productos.filter(p => {
    // Ajusta esta condición según tu modelo
    if (p.active !== undefined) return p.active === true;
    if (p.status !== undefined) return p.status === 'active' || p.status === 'ACTIVE';
    if (p.estado !== undefined) return p.estado === 'activo';
    // Si no hay campo de estado, considera todos los productos como activos
    return true;
  }).length;

  // Obtener categorías únicas
  const categoriasUnicas = [...new Set(productos.map(p => p.category).filter(c => c))];
  const categoriasCount = categoriasUnicas.length;

  // Obtener unidades de medida únicas
  const unidadesUnicas = [...new Set(productos.map(p => p.unit).filter(u => u))];
  const unidadesCount = unidadesUnicas.length;

  // Contar productos por categoría
  const productosPorCategoria = {};
  categoriasUnicas.forEach(cat => {
    productosPorCategoria[cat] = productos.filter(p => p.category === cat).length;
  });

  // Contar productos por unidad de medida
  const productosPorUnidad = {};
  unidadesUnicas.forEach(unidad => {
    productosPorUnidad[unidad] = productos.filter(p => p.unit === unidad).length;
  });

  // Calcular estadísticas de precios
  const precios = productos
    .map(p => {
      // Manejar diferentes formatos de precio
      if (typeof p.price === 'number') return p.price;
      if (typeof p.price === 'string') return parseFloat(p.price);
      if (p.price && typeof p.price === 'object') {
        // Si es un BigDecimal u objeto, intentar obtener el valor
        return parseFloat(p.price.toString());
      }
      return 0;
    })
    .filter(p => !isNaN(p) && p > 0);

  const precioPromedio = precios.length > 0 
    ? precios.reduce((a, b) => a + b, 0) / precios.length 
    : 0;
  const precioMax = precios.length > 0 ? Math.max(...precios) : 0;
  const precioMin = precios.length > 0 ? Math.min(...precios) : 0;

  return {
    total,
    activos,
    categoriasCount,
    unidadesCount,
    productosPorCategoria,
    productosPorUnidad,
    precios: {
      promedio: precioPromedio,
      maximo: precioMax,
      minimo: precioMin
    },
    productos
  };
}

// Actualizar tarjetas de estadísticas
function actualizarTarjetasProductos(stats) {
  if (!stats) return;
  
  document.getElementById("totalProducts").textContent = stats.total;
  document.getElementById("activeProducts").textContent = stats.activos;
  document.getElementById("categoriesCount").textContent = stats.categoriasCount;
  document.getElementById("unitsCount").textContent = stats.unidadesCount;
}

// Mostrar mensaje cuando no hay productos
function mostrarMensajeSinProductos() {
  const container = document.querySelector('.charts-grid');
  if (container) {
    container.innerHTML = `
      <div class="col-span-full text-center py-12">
        <i class="fas fa-box-open text-6xl text-gray-400 mb-4"></i>
        <p class="text-xl text-gray-600 font-semibold mb-2">No tienes productos registrados</p>
        <p class="text-gray-500 mb-4">Comienza agregando tus primeros productos para ver las estadísticas</p>
        <a href="/agregar_producto" class="inline-block bg-[#8bc34a] text-white text-lg px-12 py-2 rounded-2xl hover:bg-[#496826] transition-colors">
          <i class="fas fa-plus mr-2"></i>Agregar Producto
        </a>
      </div>
    `;
  }
  
  // Limpiar los valores de las tarjetas
  document.getElementById("totalProducts").textContent = "0";
  document.getElementById("activeProducts").textContent = "0";
  document.getElementById("categoriesCount").textContent = "0";
  document.getElementById("unitsCount").textContent = "0";
}

// Mostrar mensaje de error
function mostrarMensajeError(mensaje) {
  if (typeof Swal !== 'undefined') {
    Swal.fire({
      icon: 'error',
      title: 'Error',
      text: mensaje,
      confirmButtonColor: '#8bc34a'
    });
  } else {
    alert(mensaje);
  }
}

// Inicializar gráficas
async function inicializarGraficasProductos() {
  try {
    const stats = await calcularEstadisticasProductos();
    
    actualizarTarjetasProductos(stats);
    
    if (!stats || stats.total === 0) {
      return;
    }

    // Destruir gráficas existentes si las hay
    if (unitsChart) unitsChart.destroy();
    if (categoriesChart) categoriesChart.destroy();
    if (pricesChart) pricesChart.destroy();

    const colors = {
      // Colores para unidades
      "Kilogramos (kg)": "#10b981",
      "Gramos (g)": "#3b82f6",
      "Libras (lb)": "#f59e0b",
      "Unidad (u)": "#ef4444",
      "Litros (L)": "#8b5cf6",
      "Millilitros (ml)": "#06b6d4",
      "Docena": "#84cc16",
      "Paquete": "#f97316",
      "Atado": "#ec4899",
      "Otro": "#6b7280",
      // Colores para categorías
      "Frutas": "#ef4444",
      "Verduras": "#3b82f6",
      "Hierbas": "#8b5cf6",
      "Orgánicos": "#84cc16",
      "Lácteos": "#06b6d4",
      "Cereales": "#f97316",
      "Legumbres": "#10b981",
      "Tubérculos": "#f59e0b"
    };

    // Función auxiliar para generar color aleatorio
    function generarColorAleatorio() {
      return `#${Math.floor(Math.random() * 16777215).toString(16).padStart(6, '0')}`;
    }

    // Gráfica de unidades de medida
    if (Object.keys(stats.productosPorUnidad).length > 0) {
      const unitsCtx = document.getElementById("unitsChart").getContext("2d");
      const unidadesLabels = Object.keys(stats.productosPorUnidad);
      const unidadesData = Object.values(stats.productosPorUnidad);

      const unidadesColors = unidadesLabels.map(unidad => 
        colors[unidad] || generarColorAleatorio()
      );

      unitsChart = new Chart(unitsCtx, {
        type: "doughnut",
        data: {
          labels: unidadesLabels,
          datasets: [{
            data: unidadesData,
            backgroundColor: unidadesColors,
            borderWidth: 3,
            borderColor: "#fff"
          }]
        },
        options: {
          responsive: true,
          maintainAspectRatio: false,
          plugins: {
            legend: {
              position: "bottom",
              labels: {
                padding: 15,
                font: { size: 11 },
                usePointStyle: true
              }
            },
            tooltip: {
              callbacks: {
                label: function(context) {
                  const total = context.dataset.data.reduce((a, b) => a + b, 0);
                  const percentage = ((context.parsed / total) * 100).toFixed(1);
                  return `${context.label}: ${context.parsed} (${percentage}%)`;
                }
              }
            }
          }
        }
      });
    }

    // Gráfica de categorías
    if (Object.keys(stats.productosPorCategoria).length > 0) {
      const categoriesCtx = document.getElementById("categoriesChart").getContext("2d");
      const categoriasLabels = Object.keys(stats.productosPorCategoria);
      const categoriasData = Object.values(stats.productosPorCategoria);

      const categoriasColors = categoriasLabels.map(cat => 
        colors[cat] || generarColorAleatorio()
      );

      categoriesChart = new Chart(categoriesCtx, {
        type: "bar",
        data: {
          labels: categoriasLabels,
          datasets: [{
            label: "Productos por Categoría",
            data: categoriasData,
            backgroundColor: categoriasColors,
            borderRadius: 8,
            borderWidth: 1,
            borderColor: "#fff"
          }]
        },
        options: {
          responsive: true,
          maintainAspectRatio: false,
          plugins: {
            legend: { display: false },
            tooltip: {
              callbacks: {
                label: function(context) {
                  return `${context.label}: ${context.parsed.y} producto${context.parsed.y !== 1 ? 's' : ''}`;
                }
              }
            }
          },
          scales: {
            y: {
              beginAtZero: true,
              ticks: { 
                stepSize: 1,
                precision: 0
              },
              grid: { color: "rgba(0, 0, 0, 0.1)" }
            },
            x: {
              grid: { display: false }
            }
          }
        }
      });
    }

    // Gráfica de análisis de precios
    if (stats.precios.maximo > 0) {
      const pricesCtx = document.getElementById("pricesChart").getContext("2d");
      pricesChart = new Chart(pricesCtx, {
        type: "line",
        data: {
          labels: ["Precio Mínimo", "Precio Promedio", "Precio Máximo"],
          datasets: [{
            label: "Análisis de Precios ($)",
            data: [
              stats.precios.minimo.toFixed(2),
              stats.precios.promedio.toFixed(2),
              stats.precios.maximo.toFixed(2)
            ],
            borderColor: "#16a34a",
            backgroundColor: "rgba(22, 163, 74, 0.1)",
            borderWidth: 3,
            fill: true,
            tension: 0.4,
            pointRadius: 8,
            pointBackgroundColor: "#16a34a",
            pointBorderColor: "#fff",
            pointBorderWidth: 2
          }]
        },
        options: {
          responsive: true,
          maintainAspectRatio: false,
          plugins: {
            legend: {
              display: true,
              position: "top",
              labels: {
                padding: 15,
                font: { size: 12 }
              }
            },
            tooltip: {
              callbacks: {
                label: function(context) {
                  return `${context.dataset.label}: $${parseFloat(context.parsed.y).toFixed(2)}`;
                }
              }
            }
          },
          scales: {
            y: {
              beginAtZero: true,
              title: {
                display: true,
                text: "Precio ($)",
                font: { size: 12, weight: "bold" }
              },
              grid: { color: "rgba(0, 0, 0, 0.1)" },
              ticks: {
                callback: function(value) {
                  return '$' + value.toFixed(2);
                }
              }
            },
            x: {
              title: {
                display: true,
                text: "Métricas de Precio",
                font: { size: 12, weight: "bold" }
              },
              grid: { display: false }
            }
          }
        }
      });
    }

  } catch (error) {
    console.error("Error al inicializar gráficas:", error);
    mostrarMensajeError("Error al cargar las estadísticas");
  }
}

// Función para refrescar los datos (útil si agregas/eliminas productos)
async function refrescarEstadisticas() {
  await inicializarGraficasProductos();
}

// Inicializar cuando el DOM esté listo
document.addEventListener("DOMContentLoaded", function() {
  inicializarGraficasProductos();
});

// Exportar funciones para uso externo si es necesario
window.refrescarEstadisticas = refrescarEstadisticas;