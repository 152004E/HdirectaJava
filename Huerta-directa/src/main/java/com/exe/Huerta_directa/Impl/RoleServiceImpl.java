package com.exe.Huerta_directa.Impl;

import com.exe.Huerta_directa.DTO.RoleDTO;
import com.exe.Huerta_directa.Service.RoleService;

import java.util.List;

public class RoleServiceImpl implements RoleService {
    @Override
    public List<RoleDTO> listarRoles() {
        return List.of();
    }

    @Override
    public RoleDTO obtenerRolePorId(Long roleId) {
        return null;
    }

    @Override
    public RoleDTO crearRole(RoleDTO roleDTO) {
        return null;
    }

    @Override
    public RoleDTO actualizarRole(Long roleId, RoleDTO roleDTO) {
        return null;
    }

    @Override
    public void eliminarRolePorId(Long roleId) {

    }
}
