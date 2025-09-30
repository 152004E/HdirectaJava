package com.exe.Huerta_directa.Controllers;


import com.exe.Huerta_directa.DTO.UserDTO;
import com.exe.Huerta_directa.Impl.UserServiceImpl;
import com.exe.Huerta_directa.Service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import java.util.List;

@Controller
@RequestMapping ("/api/users")
@CrossOrigin("*")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    //Aqui irian los endpoints para manejar las solicitudes HTTP relacionadas con usuario

    //Metodo para listar todos los usuarios
    @GetMapping
    public  ResponseEntity<List<UserDTO>> listarUsers() {
        return new ResponseEntity<>(userService.listarUsers(), HttpStatus.OK);
    }

    //Metodo para obtener un usuario por su id
    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> obtenerUserPorId(@PathVariable Long userId){
        return new ResponseEntity<>(userService.obtenerUserPorId(userId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<UserDTO> crearUser(@RequestBody UserDTO userDTO){
        return new ResponseEntity<>(userService.crearUser(userDTO), HttpStatus.CREATED);
    }

    /*Metodo para crear un nuevo usuario
    @PostMapping("/register")
    public String registrarUser(
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            Model model){

        try {
            UserDTO userDTO = new UserDTO();
            userDTO.setName(name);
            userDTO.setEmail(email);
            userDTO.setPassword(password);

            UserDTO registrado = userService.crearUser(userDTO);

            //Condicional para redirigir segun el resultado del registro
            if (registrado != null && registrado.getId() != null){
                //Si el registro es exitoso, redirigir a la pagina de inicio
                model.addAttribute("Mensaje", "Usuario registrado exitosamente");
                return "redirect:/index";
            } else {
                //Si el registro falla, redirigir a la pagina de registro con un mensaje de error
                model.addAttribute("Error", "Error al registrar usuario. Intente nuevamente.");
                return "redirect:/LogIn";
            }
        } catch (RuntimeException e) {
            //Si el usuario falla en registrarse, redirigir a la pagina de registro con un mensaje de error
             model.addAttribute("Error", "Error al registrar usuario. Intente nuevamente.");
             return "redirect:/LogIn";
        }
    }*/


    //Metodo para actualizar un usuario
    @PutMapping("/{userId}")
    public ResponseEntity<UserDTO> actualizarUser(@PathVariable ("userId") Long userId, @RequestBody UserDTO userDTO) {
        return new ResponseEntity<>(userService.actualizarUser(userId, userDTO), HttpStatus.OK);
    }

    //Metodo para eliminar un usuario por su id
    @DeleteMapping("/{userId}")
   public ResponseEntity<Void> eliminarUserPorId(@PathVariable ("userId") Long userId) {
        userService.eliminarUserPorId(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

   }
    /*

     /*
    //Se comento por si acasoooo
   //Metodo para exportar a excel todos los usuarios
   // Endpoint para exportar usuarios a Excel
   @GetMapping("/export/excel")
   public void exportUsersToExcel(HttpServletResponse response) throws IOException {
       try {
           // Configurar la respuesta HTTP para descarga de archivo Excel
           response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

           // Generar nombre de archivo con timestamp
           String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
           String filename = "users_" + timestamp + ".xlsx";

           // Configurar header para descarga
           response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
           response.setHeader("Cache-Control", "no-cache");

           // CAMBIO: Llamar al método correcto (con el typo original)
           userService.exporUserstToExcel(response.getOutputStream());

           // Limpiar el buffer
           response.getOutputStream().flush();

       } catch (Exception e) {
           // En caso de error, devolver un error HTTP 500
           response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
           response.getWriter().write("Error al generar el archivo Excel: " + e.getMessage());
       }
   }

    // Endpoint de prueba
    @GetMapping("/test")
    public ResponseEntity<String> testEndpoint() {
        return ResponseEntity.ok("Controller funcionando correctamente!");
    }

    // Endpoint para contar usuarios
    @GetMapping("/count")
    public ResponseEntity<String> getUserCount() {
        try {
            List<UserDTO> users = userService.listarUsers();
            return ResponseEntity.ok("Total de usuarios encontrados: " + users.size());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al contar usuarios: " + e.getMessage());
        }
    }
    */

    // Método para exportar usuarios a Excel
    @GetMapping("/exportExcel")
    public ResponseEntity<InputStreamResource> exportarExcel() throws IOException {
        // Creamos el flujo de salida en memoria (Array de bytes)
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // Llamamos al servicio para exportar los usuarios a Excel
        ((UserServiceImpl) userService).exporUserstToExcel(outputStream);

        // Configuramos la respuesta HTTP con el archivo Excel
        HttpHeaders headers = new HttpHeaders();

        // Definimos el tipo de contenido y las cabeceras para la descarga
        headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));

        // Indicamos al navegador que es un archivo adjunto con un nombre específico
        headers.setContentDispositionFormData("attachment", "Usuarios.xlsx");

        // Creamos y devolvemos la respuesta con el archivo Excel en el cuerpo
        // Creamos un inputStream a partir del array de bytes
        return new ResponseEntity<>(
                new InputStreamResource(new ByteArrayInputStream(outputStream.toByteArray())),
                headers,
                HttpStatus.OK
        );
    }

    // Endpoint para exportar usuarios a PDF
    @GetMapping("/exportPdf")
    public void exportUsersToPdf(HttpServletResponse response) throws IOException {
        try {
            // Configurar la respuesta HTTP para descarga de archivo PDF
            response.setContentType("application/pdf");

            // Generar nombre de archivo con timestamp, por si acaso
            // String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String filename = "Usuarios" + /*timestamp+ */ ".pdf";

            // Configurar header para descarga
            response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
            response.setHeader("Cache-Control", "no-cache");

            // Llamar al método de exportación del service
            userService.exportUsersToPdf(response.getOutputStream());

            // Limpiar el buffer
            response.getOutputStream().flush();

        } catch (Exception e) {
            // En caso de error, devolver un error HTTP 500
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Error al generar el archivo PDF: " + e.getMessage());
        }
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

   //Aqui van los endpoints para manejar las solicitudes HTTP relacionadas con usuario

    @PostMapping("/register")
    public String seveUserView(
            @Valid @ModelAttribute("userDTO") UserDTO userDTO,
            BindingResult result,
            RedirectAttributes redirect) {
        if (result.hasErrors()) {
            return "users/login"; // Si hay errores, volver al formulario de registro
        }
        userService.crearUser(userDTO);
        redirect.addFlashAttribute("success", "Usuario creado");
        return "redirect:/index";
    }


    @PostMapping("/login")
    public String loginUser(
            @Valid @ModelAttribute("userDTO") UserDTO userDTO,
            BindingResult result,
            RedirectAttributes redirect) {
        if (result.hasErrors()) {
            return "users/login"; // Si hay errores, volver al formulario de registro
        }
        // Lógica de autenticación aquí (verificar credenciales, etc.)
        redirect.addFlashAttribute("success", "Inicio de sesión exitoso");
        return "redirect:/index"; // Redirigir a la página principal después del inicio de


    }

}
