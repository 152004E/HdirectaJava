package com.exe.Huerta_directa.Impl;

import com.exe.Huerta_directa.DTO.ProductDTO;
import com.exe.Huerta_directa.Entity.Product;
import com.exe.Huerta_directa.Entity.User;
import com.exe.Huerta_directa.Repository.ProductRepository;
import com.exe.Huerta_directa.Repository.UserRepository;
import com.exe.Huerta_directa.Service.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public ProductServiceImpl(ProductRepository productRepository, UserRepository userRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<ProductDTO> listarProducts() {
        return productRepository.findAll()
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDTO> listarProductosPorUsuario(Long userID) {
        return productRepository.findByUserId(userID)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Override
    public ProductDTO obtenerProductPorId(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + productId));
        return convertirADTO(product);
    }

    @Override
    public ProductDTO crearProduct(ProductDTO productDTO, Long userId) {
        // Buscar el usuario
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + userId));

        Product product = convertirAEntity(productDTO);
        product.setUser(user); // Asignar el usuario al producto

        Product nuevoProduct = productRepository.save(product);
        return convertirADTO(nuevoProduct);
    }

    @Override
    public ProductDTO actualizarProduct(Long productId, ProductDTO productDTO) {
        Product productExistente = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + productId));

        actualizarDatosProducto(productExistente, productDTO);
        Product productActualizado = productRepository.save(productExistente);
        return convertirADTO(productActualizado);
    }

    @Override
    public void eliminarProductPorId(Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new RuntimeException("Producto no encontrado con id: " + productId);
        } else {
            productRepository.deleteById(productId);
        }
    }

    @Override
    public List<ProductDTO> buscarPorNombre(String nombre) {
        return productRepository.findByNameProductContainingIgnoreCase(nombre).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDTO> buscarPorCategoria(String categoria) {
        return productRepository.findByCategoryIgnoreCase(categoria).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existeProducto(String nombre, String categoria) {
        return productRepository.existsByNameProductIgnoreCaseAndCategoryIgnoreCase(nombre, categoria);
    }

    @Override
    public long contarTotalProductos() {
        return productRepository.count();
    }

    @Override
    public boolean existeProductoPorUsuario(String nombre, String categoria, Long userId) {
        try {
            List<Product> productos = productRepository.findAll();
            return productos.stream()
                    .anyMatch(p -> p.getNameProduct().trim().equalsIgnoreCase(nombre.trim()) &&
                            p.getCategory().trim().equalsIgnoreCase(categoria.trim()) &&
                            p.getUser() != null &&
                            p.getUser().getId().equals(userId));
        } catch (Exception e) {
            return false;
        }

    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductDTO> listarProductsPorCategoria(String categoria) {
        return productRepository.findByCategoryIgnoreCase(categoria)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // Convertir Entity a DTO
    private ProductDTO convertirADTO(Product product) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setIdProduct(product.getIdProduct());
        productDTO.setNameProduct(product.getNameProduct());
        productDTO.setPrice(product.getPrice());
        productDTO.setCategory(product.getCategory());
        productDTO.setImageProduct(product.getImageProduct());
        productDTO.setUnit(product.getUnit());
        productDTO.setDescriptionProduct(product.getDescriptionProduct());
        productDTO.setPublicationDate(product.getPublicationDate());

        // Asignar el id del usuario si existe
        if (product.getUser() != null) {
            productDTO.setUserId(product.getUser().getId());
        } else {
            productDTO.setUserId(null);
        }

        return productDTO;
    }

    // Convertir DTO a Entity
    private Product convertirAEntity(ProductDTO productDTO) {
        Product product = new Product();
        product.setIdProduct(productDTO.getIdProduct());
        product.setNameProduct(productDTO.getNameProduct());
        product.setPrice(productDTO.getPrice());
        product.setCategory(productDTO.getCategory());
        product.setImageProduct(productDTO.getImageProduct());
        product.setUnit(productDTO.getUnit());
        product.setDescriptionProduct(productDTO.getDescriptionProduct());
        product.setPublicationDate(productDTO.getPublicationDate());

        if (productDTO.getUserId() != null) {
            User user = userRepository.findById(productDTO.getUserId())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + productDTO.getUserId()));
            product.setUser(user);
        }

        return product;
    }

    // Actualizar Entity con datos del DTO
    private void actualizarDatosProducto(Product product, ProductDTO productDTO) {
        product.setNameProduct(productDTO.getNameProduct());
        product.setPrice(productDTO.getPrice());
        product.setCategory(productDTO.getCategory());
        product.setImageProduct(productDTO.getImageProduct());
        product.setUnit(productDTO.getUnit());
        product.setDescriptionProduct(productDTO.getDescriptionProduct());
        product.setPublicationDate(productDTO.getPublicationDate());

        if (productDTO.getUserId() != null) {
            User user = userRepository.findById(productDTO.getUserId())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + productDTO.getUserId()));
            product.setUser(user);
        }
    }

    // La implementacion para los graficos por categoria
    @Override
    public Map<String, Long> contarProductosPorCategoria() {
        return listarProducts().stream()
                .collect(Collectors.groupingBy(ProductDTO::getCategory, Collectors.counting()));
    }

    @Override
    @Transactional
    public void descontarStock(Long productId, Integer cantidad) {
        Product producto = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + productId));

        // Validar que hay stock suficiente
        if (producto.getStock() == null) {
            throw new RuntimeException("El producto '" + producto.getNameProduct() + "' no tiene stock configurado");
        }

        if (producto.getStock() < cantidad) {
            throw new RuntimeException("Stock insuficiente para '" + producto.getNameProduct() +
                    "'. Disponible: " + producto.getStock() +
                    ", Solicitado: " + cantidad);
        }

        // Descontar stock
        producto.setStock(producto.getStock() - cantidad);
        productRepository.save(producto);

        // Logging mejorado (más fácil de leer en consola)
        System.out.println("✅ Stock actualizado:");
        System.out.println("   - Producto: " + producto.getNameProduct());
        System.out.println("   - Cantidad descontada: " + cantidad);
        System.out.println("   - Stock restante: " + producto.getStock());
    }
    }


