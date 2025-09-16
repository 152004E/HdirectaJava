package com.exe.Huerta_directa.Impl;

import com.exe.Huerta_directa.DTO.ProductDTO;
import com.exe.Huerta_directa.Entity.Product;
import com.exe.Huerta_directa.Repository.ProductRepository;
import com.exe.Huerta_directa.Service.ProductService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    // Inyección por constructor
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // Listar todos los productos
    @Override
    public List<ProductDTO> listarProducts() {
        return productRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Obtener producto por ID
    @Override
    public ProductDTO obtenerProductPorId(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id " + productId));
        return mapToDTO(product);
    }

    // Crear producto
    @Override
    public ProductDTO crearProduct(ProductDTO productDTO) {
        Product product = mapToEntity(productDTO);
        Product guardado = productRepository.save(product);
        return mapToDTO(guardado);
    }

    // Actualizar producto
    @Override
    public ProductDTO actualizarProduct(Long productId, ProductDTO productDTO) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id " + productId));

        product.setNameProduct(productDTO.getNameProduct());
        product.setCategory(productDTO.getCategory());
        product.setPrice(productDTO.getPrice());

        Product actualizado = productRepository.save(product);
        return mapToDTO(actualizado);
    }

    // Eliminar producto
    @Override
    public void eliminarProductPorId(Long productId) {
        productRepository.deleteById(productId);
    }

    // Métodos extra: buscar por nombre
    public ProductDTO buscarPorNombre(String nameProduct) {
        Product product = productRepository.findBynameProduct(nameProduct);
        if (product == null) {
            throw new RuntimeException("Producto no encontrado con nombre " + nameProduct);
        }
        return mapToDTO(product);
    }

    // Métodos extra: buscar por categoría
    public ProductDTO buscarPorCategoria(String category) {
        Product product = productRepository.findBycategory(category);
        if (product == null) {
            throw new RuntimeException("Producto no encontrado en la categoría " + category);
        }
        return mapToDTO(product);
    }

    // Conversión Entidad - DTO
    private ProductDTO mapToDTO(Product product) {
        return new ProductDTO(
                product.getIdProduct(),
                product.getNameProduct(),
                product.getPrice(),
                product.getCategory(),
                product.getImageProduct(),
                product.getUnit(),
                product.getPublicationDate(),
                product.getDescriptionProduct()
        );
    }

    // Conversión DTO - Entidad
    private Product mapToEntity(ProductDTO dto) {
        Product product = new Product();
        product.setIdProduct(dto.getIdProduct());
        product.setNameProduct(dto.getNameProduct());
        product.setCategory(dto.getCategory());
        product.setPrice(dto.getPrice());
        return product;
    }
}
