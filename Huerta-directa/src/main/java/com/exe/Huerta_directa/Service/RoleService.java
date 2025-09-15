package com.exe.Huerta_directa.Service;

import com.exe.Huerta_directa.DTO.RoleDTO;


import java.util.List;

public interface RoleService {

    List<RoleDTO> listarRoles();

    RoleDTO obtenerRolePorId(Long roleId);

    RoleDTO crearRole(RoleDTO roleDTO);

    RoleDTO actualizarRole(Long roleId, RoleDTO roleDTO);

    void eliminarRolePorId(Long roleId);
}
