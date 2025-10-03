package com.exe.Huerta_directa.Service;

import com.exe.Huerta_directa.DTO.ProductDTO;

import java.util.List;

public interface ProductService {

    List<ProductDTO> listarProducts();

    ProductDTO obtenerProductPorId(Long productId);

    ProductDTO crearProduct(ProductDTO productDTO, Long userId);

    ProductDTO actualizarProduct(Long productId, ProductDTO productDTO);

    List<ProductDTO> listarProductsPorCategoria(String categoria);

    void eliminarProductPorId(Long productId);

    List<ProductDTO> buscarPorNombre(String nombre);
    
    List<ProductDTO> buscarPorCategoria(String categoria);
}
