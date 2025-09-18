package com.exe.Huerta_directa.Controllers;

import com.exe.Huerta_directa.DTO.ProductDTO;
import com.exe.Huerta_directa.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    // Aquí irían los endpoints para manejar las solicitudes HTTP relacionadas con producto
    @GetMapping
    public  ResponseEntity<List<ProductDTO>> listarProducts() {
        List<ProductDTO> listarProducts = productService.listarProducts();
        return new ResponseEntity<>(productService.listarProducts(), HttpStatus.OK);
    }

    //Metodo para obtener un producto por su id
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> obtenerProductPorId(@PathVariable Long productId){
        return new ResponseEntity<>(productService.obtenerProductPorId(productId), HttpStatus.OK);
    }


    @PostMapping
    public ResponseEntity<ProductDTO> crearProduct(@RequestBody ProductDTO productDTO) {
        return new ResponseEntity<>(productService.crearProduct(productDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> actualizarProduct(@PathVariable ("id") Long productId, @RequestBody ProductDTO productDTO) {
        return new ResponseEntity<>(productService.actualizarProduct(productId, productDTO), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
   public ResponseEntity<Void> eliminarProductPorId(@PathVariable ("id") Long productId) {
        productService.eliminarProductPorId(productId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
}
 