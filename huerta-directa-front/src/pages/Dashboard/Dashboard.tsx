import React, { useState, useEffect } from "react";
import "./Dashboard.css";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faMagnifyingGlass,
  faBox,
  faPen,
  faTrash,
  faFileExcel,
  faFilePdf,
  faCloudArrowUp,
  faCartShopping,
  faBagShopping,
  faUser,
  faBars,
  faXmark,
  faChartLine,
  faEnvelope,
  faFileLines,
  faBoxesStacked,
  faGear,
  faRightFromBracket,
  faHouse,
  faUpload,
} from "@fortawesome/free-solid-svg-icons";
import { usePageTitle } from "../../hooks/usePageTitle";
import logo from "../../assets/logo_huerta.png";

// Reusable components
import { Button } from "../../components/GlobalComponents/Button";
import { Background } from "../../components/GlobalComponents/Background";
import { Modal } from "../../components/GlobalComponents/Modal";

interface Product {
  idProduct: number;
  nameProduct: string;
  category: string;
  price: number;
  unit: string;
  descriptionProduct: string;
  stock: number;
}

interface InsightItem {
  title: string;
  value: string;
  percentage: number;
  footer: string;
  color: string;
}

export const Dashboard: React.FC = () => {
  usePageTitle("Dashboard");

  const [products, setProducts] = useState<Product[]>([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState("");
  const [category, setCategory] = useState("");
  const [isSidebarOpen, setIsSidebarOpen] = useState(false);
  const [isUploadModalOpen, setIsUploadModalOpen] = useState(false);
  const [uploadFile, setUploadFile] = useState<File | null>(null);
  const [uploadResult, setUploadResult] = useState<{ success: boolean; message: string } | null>(null);

  const toggleSidebar = () => setIsSidebarOpen(!isSidebarOpen);

  useEffect(() => {
    fetchProducts();
  }, []);

  const fetchProducts = async () => {
    try {
      const response = await fetch("/api/products/mis-Productos");
      if (response.ok) {
        const data = await response.json();
        setProducts(data);
      }
    } catch (error) {
      console.error("Error fetching products:", error);
    } finally {
      setLoading(false);
    }
  };

  const insights: InsightItem[] = [
    {
      title: "Ventas",
      value: "$25,000",
      percentage: 81,
      footer: "Ultimas 24 horas",
      color: "sales",
    },
    {
      title: "Gastos",
      value: "$15,567",
      percentage: 62,
      footer: "Ultimas 24 horas",
      color: "expenses",
    },
    {
      title: "Ingresos",
      value: "$10,240",
      percentage: 31,
      footer: "Ultimas 24 horas",
      color: "income",
    },
  ];

  const sidebarOptions = [
    { label: "Dashboard", icon: faHouse, link: "/dashboard" },
    { label: "Gráficos", icon: faChartLine, link: "/DashBoardGraficos" },
    { label: "Área Social", icon: faEnvelope, link: "/MensajesAreaSocial" },
    { label: "Ordenes", icon: faFileLines, link: "/misOrdenes" },
    { label: "Agregar Producto", icon: faBoxesStacked, link: "/DashBoardAgregarProducto" },
    { label: "Mi Perfil", icon: faGear, link: "/actualizacionUsuario" },
    { label: "Pagina Principal", icon: faRightFromBracket, link: "/" },
  ];

  const filteredProducts = products.filter(
    (p) =>
      p.nameProduct.toLowerCase().includes(searchTerm.toLowerCase()) &&
      (category === "" || p.category === category)
  );

  const handleExportExcel = () => {
    const params = new URLSearchParams();
    if (searchTerm) params.append("buscar", searchTerm);
    if (category) params.append("categoria", category);
    window.location.href = `/exportar_productos_excel?${params.toString()}`;
  };

  const handleExportPdf = () => {
    const params = new URLSearchParams();
    if (searchTerm) params.append("buscar", searchTerm);
    if (category) params.append("categoria", category);
    window.location.href = `/exportar_productos_pdf?${params.toString()}`;
  };

  const handleUploadSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!uploadFile) return;

    const formData = new FormData();
    formData.append("archivo", uploadFile);

    try {
      const response = await fetch("/api/users/upload-products", {
        method: "POST",
        body: formData,
      });
      const result = await response.json();
      if (response.ok && result.success) {
        setUploadResult({ success: true, message: "¡Carga masiva exitosa!" });
        fetchProducts(); // Refresh list
        setTimeout(() => {
          setIsUploadModalOpen(false);
          setUploadResult(null);
        }, 2000);
      } else {
        setUploadResult({ success: false, message: result.message || "Error al cargar productos" });
      }
    } catch (error) {
      setUploadResult({ success: false, message: "Error de conexión con el servidor" });
    }
  };

  return (
    <main className="dashboard-main">
      <Background />
      <h1 className="dashboard-title">Dashboard</h1>

      {/* Sidebar Toggle Button */}
      <button className="sidebar-toggle-btn" onClick={toggleSidebar}>
        <FontAwesomeIcon icon={isSidebarOpen ? faXmark : faBars} />
      </button>

      {/* Overlay */}
      <div 
        className={`sidebar-overlay ${isSidebarOpen ? "open" : ""}`} 
        onClick={toggleSidebar}
      />

      {/* Dynamic Sidebar */}
      <aside className={`dynamic-sidebar ${isSidebarOpen ? "open" : ""}`}>
        <div className="sidebar-header">
          <img src={logo} alt="Logo" />
          <h2>
            Huerta<span className="danger">Directa</span>
          </h2>
        </div>

        <nav className="sidebar-menu">
          {sidebarOptions.map((opt, idx) => (
            <a 
              key={idx} 
              href={opt.link} 
              className={`sidebar-link ${window.location.pathname === opt.link ? "active" : ""}`}
            >
              <FontAwesomeIcon icon={opt.icon} className="sidebar-icon" />
              <h3>{opt.label}</h3>
            </a>
          ))}
        </nav>
      </aside>

      <div className="dashboard-content">
        <section className="dashboard-left">
          {/* Insights Grid */}
          <div className="insights">
            {insights.map((item, idx) => {
              const dashOffset = 226 * (1 - item.percentage / 100);
              return (
                <div key={idx} className={`insight-card ${item.color}`}>
                  <div className="middle">
                    <div className="left">
                      <h3>{item.title}</h3>
                      <h1>{item.value}</h1>
                    </div>
                    <div className="progress">
                      <svg>
                        <circle
                          cx="38"
                          cy="38"
                          r="36"
                          style={{ strokeDashoffset: dashOffset }}
                        ></circle>
                      </svg>
                      <div className="number">
                        <p>{item.percentage}%</p>
                      </div>
                    </div>
                  </div>
                  <small className="text-muted">{item.footer}</small>
                </div>
              );
            })}
          </div>

          {/* Product Management */}
          <section className="recent-orders">
            <h2>Gestión de Productos</h2>

            <div className="total-products-card">
              <FontAwesomeIcon icon={faBox} />
              <span>Total de Productos: {products.length}</span>
            </div>

            {/* Filters */}
            <div className="busqueda-filtros">
              <div className="barra-busqueda">
                <input
                  type="text"
                  placeholder="Buscar producto..."
                  className="input-buscar"
                  value={searchTerm}
                  onChange={(e) => setSearchTerm(e.target.value)}
                />
              </div>
              <div className="filtros-selects">
                <select
                  className="select-categoria"
                  value={category}
                  onChange={(e) => setCategory(e.target.value)}
                >
                  <option value="">Por categoría</option>
                  <option value="frutas">Frutas</option>
                  <option value="verduras-hortalizas">Verduras y Hortalizas</option>
                  <option value="lacteos">Lácteos</option>
                  <option value="carnes-proteinas">Carnes y Proteínas</option>
                  <option value="cereales-granos">Cereales y Granos</option>
                  <option value="legumbres-secas">Legumbres Secas</option>
                  <option value="productos-organicos">Productos Orgánicos</option>
                  <option value="hierbas-especias">Hierbas y Especias</option>
                  <option value="miel-derivados">Miel y Derivados</option>
                  <option value="procesados-artesanales">Procesados Artesanales</option>
                  <option value="bebidas-naturales">Bebidas Naturales</option>
                  <option value="plantas-semillas">Plantas y Semillas</option>
                  <option value="cajas-combos">Cajas Mixtas o Combos</option>
                </select>
                <Button
                  text="Consultar"
                  iconLetf={faMagnifyingGlass}
                  className="btn-consultar"
                />
              </div>
            </div>

            {/* Product Table */}
            <div className="table-container">
              {loading ? (
                <p>Cargando productos...</p>
              ) : (
                <table className="product-table">
                  <thead>
                    <tr>
                      <th>Nombre</th>
                      <th>Categoría</th>
                      <th>Precio</th>
                      <th>Unidad</th>
                      <th>Descripción</th>
                      <th>Stock</th>
                      <th>Acciones</th>
                    </tr>
                  </thead>
                  <tbody>
                    {filteredProducts.map((p) => (
                      <tr key={p.idProduct}>
                        <td>{p.nameProduct}</td>
                        <td>{p.category}</td>
                        <td>${p.price.toLocaleString()}</td>
                        <td>{p.unit}</td>
                        <td>
                          {p.descriptionProduct.length > 50 
                            ? p.descriptionProduct.substring(0, 50) + "..." 
                            : p.descriptionProduct}
                        </td>
                        <td>
                          <span
                            className={`stock-tag ${
                              p.stock > 0 ? "stock-disponible" : "stock-agotado"
                            }`}
                          >
                            {p.stock > 0 ? `${p.stock} disponibles` : "Sin stock"}
                          </span>
                        </td>
                        <td>
                          <div className="action-buttons">
                            <button className="btn-action btn-edit">
                              <FontAwesomeIcon icon={faPen} />
                            </button>
                            <button className="btn-action btn-delete">
                              <FontAwesomeIcon icon={faTrash} />
                            </button>
                          </div>
                        </td>
                      </tr>
                    ))}
                    {filteredProducts.length === 0 && (
                      <tr>
                        <td colSpan={7} style={{ textAlign: "center", padding: "2rem" }}>
                          No se encontraron productos
                        </td>
                      </tr>
                    )}
                  </tbody>
                </table>
              )}
            </div>

            {/* Export Actions */}
            <div className="export-container">
              <Button 
                text="Exportar a Excel" 
                iconLetf={faFileExcel} 
                onClick={handleExportExcel} 
                className="btn-export btn-excel" 
              />
              <Button 
                text="Exportar a PDF" 
                iconLetf={faFilePdf} 
                onClick={handleExportPdf} 
                className="btn-export btn-pdf" 
              />
              <Button 
                text="Carga Masiva de Productos" 
                iconLetf={faCloudArrowUp} 
                onClick={() => setIsUploadModalOpen(true)} 
                className="btn-export btn-upload" 
              />
            </div>
          </section>
        </section>

        {/* Right Section Widgets */}
        <aside className="dashboard-right">
          <div className="widget-card">
            <h2>Actualizaciones</h2>
            <div className="update-list">
              <div className="update-item">
                <div className="message">
                  <p>
                    <b>Sistema</b> funcionando correctamente
                  </p>
                  <small className="text-muted">Hace 1 hora</small>
                </div>
              </div>
            </div>
          </div>

          <div className="widget-card">
            <h2>Analíticas</h2>
            <div className="summary-items">
              <div className="summary-item">
                <div className="icon">
                  <FontAwesomeIcon icon={faCartShopping} />
                </div>
                <div className="details">
                  <div className="info">
                    <h3>Ordenes</h3>
                    <small className="text-muted">Ultimas 24 horas</small>
                  </div>
                  <div className="value">
                    <span className="success">+39%</span>
                    <p>3849</p>
                  </div>
                </div>
              </div>
              <div className="summary-item">
                <div className="icon" style={{ background: "#004d00" }}>
                  <FontAwesomeIcon icon={faBagShopping} />
                </div>
                <div className="details">
                  <div className="info">
                    <h3>Offline Ordenes</h3>
                    <small className="text-muted">Ultimas 24 horas</small>
                  </div>
                  <div className="value">
                    <span className="danger">-17%</span>
                    <p>1100</p>
                  </div>
                </div>
              </div>
              <div className="summary-item">
                <div className="icon">
                  <FontAwesomeIcon icon={faUser} />
                </div>
                <div className="details">
                  <div className="info">
                    <h3>Nuevos Clientes</h3>
                    <small className="text-muted">Ultimas 24 horas</small>
                  </div>
                  <div className="value">
                    <span className="success">+67%</span>
                    <p>849</p>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </aside>
      </div>

      {/* Mass Upload Modal */}
      <Modal
        isOpen={isUploadModalOpen}
        onClose={() => setIsUploadModalOpen(false)}
        title="Carga Masiva de Productos"
        icon={faUpload}
      >
        <form onSubmit={handleUploadSubmit}>
          <div className="form-group">
            <label>Seleccionar archivo de productos:</label>
            <input 
              type="file" 
              accept=".csv,.xlsx,.xls" 
              required 
              onChange={(e) => setUploadFile(e.target.files ? e.target.files[0] : null)}
            />
            <div className="help-text">
              💡 <strong>Formato esperado:</strong><br />
              📋 <em>Nombre | Precio | Categoría | Unidad | Descripción | Imagen | Stock</em><br />
              📁 Formatos: CSV, Excel (.xlsx, .xls)<br />
              ⚠️ <strong>Nota:</strong> El campo Stock es opcional.
            </div>
          </div>
          <Button type="submit" text="Cargar Productos" className="btn-send-email" />
        </form>
        {uploadResult && (
          <div className={`result-message active ${uploadResult.success ? "success" : "error"}`}>
            {uploadResult.message}
          </div>
        )}
      </Modal>
    </main>
  );
};

export default Dashboard;
