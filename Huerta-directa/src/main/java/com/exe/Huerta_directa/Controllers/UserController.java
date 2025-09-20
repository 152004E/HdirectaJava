package com.exe.Huerta_directa.Controllers;


import com.exe.Huerta_directa.DTO.UserDTO;
import com.exe.Huerta_directa.Service.ProductService;
import com.exe.Huerta_directa.Service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

@RestController
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

    //Metodo para crear un nuevo usuario
    @PostMapping("/register")
    public RedirectView registrarUser(
            @RequestParam("Nombre_usuario") String name,
            @RequestParam("email") String email,
            @RequestParam("password") String password){

        try {
            UserDTO userDTO = new UserDTO();
            userDTO.setName(name);
            userDTO.setEmail(email);
            userDTO.setPassword(password);

            UserDTO registrado = userService.crearUser(userDTO);

            //Condicional para redirigir segun el resultado del registro
            if (registrado != null && registrado.getId() != null){
                //Si el registro es exitoso, redirigir a la pagina de inicio
                return new RedirectView("/");
            } else {
                //Si el registro falla, redirigir a la pagina de registro con un mensaje de error
                return new RedirectView("/LogIn");
            }
        } catch (RuntimeException e) {
            //Si el usuario falla en registrarse, redirigir a la pagina de registro con un mensaje de error
             System.out.println("Error al registrar usuario: " + e.getMessage());
             return new RedirectView("/LogIn");
        }
    }


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



}
