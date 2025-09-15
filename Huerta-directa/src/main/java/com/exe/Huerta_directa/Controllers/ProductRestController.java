package com.exe.Huerta_directa.Controllers;

import com.exe.Huerta_directa.DTO.ProductDTO;
import com.exe.Huerta_directa.Service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductRestController {

    private final ProductService productService;

    public ProductRestController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<ProductDTO> listarProducts() {
        return productService.listarProducts();
    }

    @GetMapping("/{id}")
    public ProductDTO obtenerProductPorId(@PathVariable Long id) {
        return productService.obtenerProductPorId(id);
    }

    @PostMapping
    public ProductDTO crearProduct(@RequestBody ProductDTO productDTO) {
        return productService.crearProduct(productDTO);
    }

    @PutMapping("/{id}")
    public ProductDTO actualizarProduct(@PathVariable Long id, @RequestBody ProductDTO productDTO) {
        return productService.actualizarProduct(id, productDTO);
    }

    @DeleteMapping("/{id}")
    public void eliminarProduct(@PathVariable Long id) {
        productService.eliminarProductPorId(id);
    }
}
