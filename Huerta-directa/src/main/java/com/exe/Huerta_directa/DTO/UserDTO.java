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

}