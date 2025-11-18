package com.exe.Huerta_directa.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BulkEmailByRoleRequest extends BulkEmailRequest {
    private Long idRole; // 1 = Admin, 2 = Cliente

    public Long getIdRole() { return idRole; }
    public void setIdRole(Long idRole) { this.idRole = idRole; }
}
