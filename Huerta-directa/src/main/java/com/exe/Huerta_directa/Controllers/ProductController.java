package com.exe.Huerta_directa.Controllers;

import com.exe.Huerta_directa.DTO.ProductDTO;
import com.exe.Huerta_directa.Service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // Aquí irían los endpoints para manejar las solicitudes HTTP relacionadas con producto
    @GetMapping
    public  ResponseEntity<List<ProductDTO>> listarProducts() {
        return new ResponseEntity<>(productService.listarProducts(), HttpStatus.OK);
    }

    //Metodo para obtener un producto por su id
    @GetMapping("/{productId}")
    public ResponseEntity<ProductDTO> obtenerProductPorId(@PathVariable Long productId){
        return new ResponseEntity<>(productService.obtenerProductPorId(productId), HttpStatus.OK);
    }

    //Metodo para crear un nuevo producto
    @PostMapping
    public ResponseEntity<ProductDTO> crearProduct(@RequestBody ProductDTO productDTO) {
        return new ResponseEntity<>(productService.crearProduct(productDTO), HttpStatus.CREATED);
    }

    //Metodo para actualizar un producto
    @PutMapping("/{productId}")
    public ResponseEntity<ProductDTO> actualizarProduct(@PathVariable ("productId") Long productId, @RequestBody ProductDTO productDTO) {
        return new ResponseEntity<>(productService.actualizarProduct(productId, productDTO), HttpStatus.OK);
    }

    //Metodo para eliminar un producto por su id
    @DeleteMapping("/{productId}")
   public ResponseEntity<Void> eliminarProductPorId(@PathVariable ("productId") Long productId) {
        productService.eliminarProductPorId(productId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
}
 