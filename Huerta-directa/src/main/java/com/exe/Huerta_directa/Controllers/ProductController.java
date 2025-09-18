package com.exe.Huerta_directa.Controllers;

import com.exe.Huerta_directa.DTO.ProductDTO;
import com.exe.Huerta_directa.Service.ProductService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Value("${upload.path}")
    private String uploadPath;

    // Crear producto con imagen
    @PostMapping("/create")
    public ResponseEntity<ProductDTO> crearProductConImagen(
            @RequestParam("nombre") String nameProduct,
            @RequestParam("precio") Double price,
            @RequestParam("unidad") String unit,
            @RequestParam("categoria-producto") String category,
            @RequestParam("image_product") MultipartFile imageFile,
            @RequestParam("descripcion") String descriptionProduct,
            @RequestParam("userId") Long userId
    ) {
        try {
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) uploadDir.mkdirs();

            String nombreImagen = "default.png";
            if (imageFile != null && !imageFile.isEmpty()) {
                String extension = Optional.ofNullable(imageFile.getOriginalFilename())
                        .filter(f -> f.contains("."))
                        .map(f -> f.substring(imageFile.getOriginalFilename().lastIndexOf(".")))
                        .orElse("");
                nombreImagen = UUID.randomUUID() + extension;
                File destino = new File(uploadDir, nombreImagen);
                System.out.println("Upload path: " + uploadPath);
                System.out.println("Nombre imagen: " + nombreImagen);
                System.out.println("Destino: " + destino.getAbsolutePath());
                imageFile.transferTo(destino);
            }

            ProductDTO productDTO = new ProductDTO();
            productDTO.setNameProduct(nameProduct);
            productDTO.setPrice(BigDecimal.valueOf(price));
            productDTO.setCategory(category);
            productDTO.setUnit(unit);
            productDTO.setDescriptionProduct(descriptionProduct);
            productDTO.setPublicationDate(LocalDate.now());
            productDTO.setUserId(userId);
            productDTO.setImageProduct(nombreImagen);

            ProductDTO creado = productService.crearProduct(productDTO);

            return ResponseEntity.status(HttpStatus.CREATED).body(creado);

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
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

    /*
    //Metodo para crear un nuevo producto
    @PostMapping
    public ResponseEntity<ProductDTO> crearProduct(@RequestBody ProductDTO productDTO) {
        return new ResponseEntity<>(productService.crearProduct(productDTO), HttpStatus.CREATED);
    }
    */


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
 