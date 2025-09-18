package com.exe.Huerta_directa.Impl;

import com.exe.Huerta_directa.DTO.RoleDTO;
import com.exe.Huerta_directa.Entity.Role;
import com.exe.Huerta_directa.Repository.RoleRepository;
import com.exe.Huerta_directa.Service.RoleService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public List<RoleDTO> listarRoles() {
        return roleRepository.findAll()
        .stream()
        .map(this::convertirADTO)
        .toList();
    }

    @Override
    public RoleDTO obtenerRolePorId(Long roleId) {
        Role role = roleRepository.findById(roleId)
        .orElseThrow(() -> new RuntimeException("Rol no encontrado por id: " + roleId));
        return convertirADTO(role);
    }

    @Override
    public RoleDTO crearRole(RoleDTO roleDTO) {
        Role role = convertirAEntity(roleDTO);
        Role nuevoRole = roleRepository.save(role);
        return convertirADTO(nuevoRole);
    }

    @Override
    public RoleDTO actualizarRole(Long roleId, RoleDTO roleDTO) {
        Role roleExistente = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado por id: " + roleId));

        actualizarDatosRole(roleExistente, roleDTO);
        Role roleActualizado = roleRepository.save(roleExistente);
        return convertirADTO(roleActualizado);
    }

    @Override
    public void eliminarRolePorId(Long roleId) {
        if (!roleRepository.existsById(roleId)) {
            throw new RuntimeException("Rol no encontrado por id: " + roleId);
            
        }
        roleRepository.deleteById(roleId);
    }

    //Convertir Entity a DTO
    private RoleDTO convertirADTO(Role role) {
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setIdRole(role.getIdRole());
        roleDTO.setName(role.getName());
        return roleDTO;
    }

    //Convertir DTO a Entity
    private Role convertirAEntity(RoleDTO roleDTO) {
        Role role = new Role();
        role.setIdRole(roleDTO.getIdRole());
        role.setName(roleDTO.getName());
        return role;
    }

    //Actualizar datos Role
    private void actualizarDatosRole(Role roleExistente, RoleDTO roleDTO) {
        roleExistente.setName(roleDTO.getName());
    }
}
