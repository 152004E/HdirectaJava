package com.exe.Huerta_directa.DTO;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//import java.time.LocalDate;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDTO {
    private Long id;
    private String name;
    private String email;
    private String password;
    private String phone;
    private String address;
    private LocalDate creacionDate;
    private Long idRole;
    private String gender;
    private LocalDate birthDate;
    
    // Getters y Setters para TODOS los campos
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    
    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public Long getIdRole() { return idRole; }
    public void setIdRole(Long idRole) { this.idRole = idRole; }
    
    public LocalDate getCreacionDate() { return creacionDate; }
    public void setCreacionDate(LocalDate creacionDate) { this.creacionDate = creacionDate; }
    
    // ... otros getters y setters
}


