package com.exe.Huerta_directa.Controllers;


import com.exe.Huerta_directa.DTO.ProductDTO;
import com.exe.Huerta_directa.Entity.User;
import com.exe.Huerta_directa.Repository.UserRepository;
import com.exe.Huerta_directa.Service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
@RestController
    @RequestMapping("/api/products")
@CrossOrigin(origins = "*")
public class ProductController {
    private final ProductService productService;
    private final UserRepository userRepository;
    public ProductController(ProductService productService, UserRepository userRepository) {
        this.productService = productService;
        this.userRepository = userRepository;
    }
    @Value("${upload.path}")
    private String uploadPath;
    // Crear producto con imagen
    @PostMapping("/create")
    public RedirectView crearProductConImagen(
            @RequestParam("nombre") String nameProduct,
            @RequestParam("precio") Double price,
            @RequestParam("unidad") String unit,
            @RequestParam("categoria-producto") String category,
            @RequestParam("image_product") MultipartFile imageFile,
            @RequestParam("descripcion") String descriptionProduct,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            return new RedirectView("/login?error=session&message=Debe+iniciar+sesiÃ³n+para+registrar+productos");
        }
        // Validar datos completos del usuario
        User user = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        if (user.getPhone() == null || user.getPhone().trim().isEmpty() ||
                user.getAddress() == null || user.getAddress().trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("error",
                    "Complete su telÃ©fono y direcciÃ³n en el perfil antes de agregar productos");
            return new RedirectView("/actualizacionUsuario");
        }
        try {
            // Obtener usuario desde la sesiÃ³n
            User userSession = (User) session.getAttribute("user");
            if (userSession == null) {
                // Si no hay usuario en sesiÃ³n, agregar mensaje de alerta y redirigir al login
                return new RedirectView("/login?error=session&message=Debe+iniciar+sesiÃ³n+para+registrar+productos");
            }
            //  PERMITIR A CUALQUIER USUARIO REGISTRADO AGREGAR PRODUCTOS
            // (No solo admins, cualquier usuario autenticado puede agregar productos)
            // Crear carpeta /productos dentro de C:/HuertaUploads
            File uploadDir = new File(uploadPath, "productos");
            if (!uploadDir.exists()) {
                boolean created = uploadDir.mkdirs();
                if (!created) {
                    throw new IOException("No se pudo crear el directorio de uploads");
                }
            }
            String nombreImagen = "default.png";
            if (imageFile != null && !imageFile.isEmpty()) {
                String extension = Optional.ofNullable(imageFile.getOriginalFilename())
                        .filter(f -> f.contains("."))
                        .map(f -> f.substring(imageFile.getOriginalFilename().lastIndexOf(".")))
                        .orElse("");
                nombreImagen = UUID.randomUUID() + extension;
                File destino = new File(uploadDir, nombreImagen);
                imageFile.transferTo(destino);
            }
            ProductDTO productDTO = new ProductDTO();
            productDTO.setNameProduct(nameProduct);
            productDTO.setPrice(BigDecimal.valueOf(price));
            productDTO.setCategory(category);
            productDTO.setUnit(unit);
            productDTO.setDescriptionProduct(descriptionProduct);
            productDTO.setPublicationDate(LocalDate.now());
            productDTO.setUserId(userSession.getId()); // Usar ID del usuario de la sesiÃ³n
            productDTO.setImageProduct(nombreImagen);
            ProductDTO creado = productService.crearProduct(productDTO, userSession.getId()); // Usar ID del usuario de la sesiÃ³n
            // Condicional para redirigir
            if (creado != null && creado.getIdProduct() != null) {
                //  Registro exitoso -> Redirigir segÃºn el rol del usuario con mensaje de Ã©xito
                if (user != null && user.getRole() != null && user.getRole().getIdRole() == 1) {
                    // Si es admin, ir al dashboard admin con mensaje de Ã©xito
                    return new RedirectView("/DashboardAdmin?success=Producto+'" + creado.getNameProduct() + "'+registrado+exitosamente");
                } else {
                    // Si es usuario normal, ir al dashboard cliente o index con mensaje de Ã©xito
                    return new RedirectView("/index?success=Â¡Producto+registrado+exitosamente!+Gracias+por+contribuir+a+nuestra+comunidad");
                }
            } else {
                // FallÃ³ el registro -> volver al formulario con error
                return new RedirectView("/agregar_producto?error=Error+al+registrar+el+producto.+IntÃ©ntalo+de+nuevo");
            }
        } catch (IOException | RuntimeException e) {
            // En caso de error -> volver al formulario con mensaje de error
            return new RedirectView("/agregar_producto?error=Error+interno+del+servidor.+Contacta+al+administrador");
        }
    }
    // AquÃ­ irÃ­an los endpoints para manejar las solicitudes HTTP relacionadas con
    // producto
    @GetMapping
    public ResponseEntity<List<ProductDTO>> listarProducts() {
        return new ResponseEntity<>(productService.listarProducts(), HttpStatus.OK);
    }
    // Metodo para obtener un producto por su id
    @GetMapping("/{productId}")
    public ResponseEntity<ProductDTO> obtenerProductPorId(@PathVariable Long productId) {
        return new ResponseEntity<>(productService.obtenerProductPorId(productId), HttpStatus.OK);
    }
    /*
     *
     * @PostMapping
     * public ResponseEntity<ProductDTO> crearProduct(@RequestBody ProductDTO
     * productDTO) {
     * return new ResponseEntity<>(productService.crearProduct(productDTO),
     * HttpStatus.CREATED);
     * }
     */
    // Metodo para actualizar un producto
    @PutMapping("/{productId}")
    public ResponseEntity<ProductDTO> actualizarProduct(@PathVariable("productId") Long productId,
            @RequestBody ProductDTO productDTO) {
        return new ResponseEntity<>(productService.actualizarProduct(productId, productDTO), HttpStatus.OK);
    }
    // Metodo para eliminar un producto por su id
    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> eliminarProductPorId(@PathVariable("productId") Long productId) {
        productService.eliminarProductPorId(productId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @GetMapping("/mis-Productos" )
    @ResponseBody
    public ResponseEntity<List<ProductDTO>> obtenerMisProductos(HttpSession session) {
        User user = (User)  session.getAttribute("user");
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        List<ProductDTO> productos = productService.listarProductosPorUsuario(user.getId());
        return ResponseEntity.ok(productos);
    }
}

