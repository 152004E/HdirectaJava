package com.exe.Huerta_directa.Controllers;

import com.exe.Huerta_directa.DTO.RoleDTO;
import com.exe.Huerta_directa.Service.RoleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping ("/api/roles")
@CrossOrigin (origins = "*")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    //Metodo para listar todos los roles
    @GetMapping
    public ResponseEntity<List<RoleDTO>>listarRoles() {
        return new ResponseEntity<>(roleService.listarRoles(), HttpStatus.OK);
    }

    //Metodo para obtener un rol por su id
    @GetMapping("/{roleId}")
    public ResponseEntity<RoleDTO> obtenerRolePorId(@PathVariable Long roleId) {
        return new ResponseEntity<>(roleService.obtenerRolePorId(roleId), HttpStatus.OK);
    }

    //Metodo para crear un nuevo rol
    @PostMapping
    public ResponseEntity<RoleDTO> crearRole(@RequestBody RoleDTO roleDTO) {
        return new ResponseEntity<>(roleService.crearRole(roleDTO), HttpStatus.CREATED);
    }

    //Metodo para actualizar un rol
    @PutMapping("/{roleId}")
    public ResponseEntity<RoleDTO> actualizarRole(@PathVariable ("roleId") Long roleId, @RequestBody RoleDTO roleDTO) {
        return new ResponseEntity<>(roleService.actualizarRole(roleId, roleDTO), HttpStatus.OK);
    }

    @DeleteMapping ("/{roleId}")
    public ResponseEntity<Void> eliminarRolePorId(@PathVariable ("roleId") Long roleId) {
        roleService.eliminarRolePorId(roleId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
