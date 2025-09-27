package com.exe.Huerta_directa.Controllers;


import com.exe.Huerta_directa.DTO.UserDTO;
import com.exe.Huerta_directa.Service.ProductService;
import com.exe.Huerta_directa.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.util.List;

@Controller
@RequestMapping ("/api/users")
@CrossOrigin("*")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService, ProductService productService) {
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
