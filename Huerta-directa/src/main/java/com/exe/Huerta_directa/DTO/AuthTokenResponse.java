package com.exe.Huerta_directa.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AuthTokenResponse {
    private String uid;
    private String email;
    private String message;
    private boolean success;
}
