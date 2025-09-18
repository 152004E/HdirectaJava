package com.exe.Huerta_directa.Controllers;


import com.exe.Huerta_directa.DTO.UserDTO;
import com.exe.Huerta_directa.Service.ProductService;
import com.exe.Huerta_directa.Service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    //Metodo para crear un nuevo usuario
    @PostMapping
    public ResponseEntity<UserDTO> crearUser(@RequestBody UserDTO userDTO) {
        return new ResponseEntity<>(userService.crearUser(userDTO), HttpStatus.CREATED);
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
