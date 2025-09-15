package com.exe.Huerta_directa.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDTO {


    private Long id;
    private String nombres;
    private String apellidos;
    private String email;
    private String rol;
    private boolean activo;


    public UserDTO() {}


    public UserDTO(Long id, String nombres, String apellidos, String email, String rol, boolean activo) {
        this.id = id;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.email = email;
        this.rol = rol;
        this.activo = activo;
    }

    public void setId(Long id) { this.id = id; }

    public void setNombres(String nombres) { this.nombres = nombres; }

    public void setApellidos(String apellidos) { this.apellidos = apellidos; }

    public void setEmail(String email) { this.email = email; }

    public void setRol(String rol) { this.rol = rol; }

    public void setActivo(boolean activo) { this.activo = activo; }
}

