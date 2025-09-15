package com.exe.Huerta_directa.Impl;

import com.exe.Huerta_directa.DTO.ProductDTO;
import com.exe.Huerta_directa.Service.ProductService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    @Override
    public List<ProductDTO> listarProducts() {
        return List.of();
    }

    @Override
    public ProductDTO obtenerProductPorId(Long productId) {
        return null;
    }

    @Override
    public ProductDTO crearProduct(ProductDTO productDTO) {
        return null;
    }

    @Override
    public ProductDTO actualizarProduct(Long productId, ProductDTO productDTO) {
        return null;
    }

    @Override
    public void eliminarProductPorId(Long productId) {

    }
}
