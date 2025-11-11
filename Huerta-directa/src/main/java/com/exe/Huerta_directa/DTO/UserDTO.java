package com.exe.Huerta_directa.DTO;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
//import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDTO {

    private Long id;
    private String name;
    private String email;
    private String password;
    private LocalDateTime creacionDate;

    private Long idRole;

    public LocalDateTime getCreacionDate() {
    return creacionDate;
}

public void setCreacionDate(LocalDateTime creacionDate) {
    this.creacionDate = creacionDate;
}

}