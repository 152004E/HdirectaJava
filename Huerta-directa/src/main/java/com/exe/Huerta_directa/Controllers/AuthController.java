/*package com.exe.Huerta_directa.Controllers;

import com.exe.Huerta_directa.DTO.AuthTokenResponse;
import com.exe.Huerta_directa.DTO.TokenRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.DecodedToken;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.exe.Huerta_directa.DTO.UserDTO;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final FirebaseAuth firebaseAuth;

    public AuthController(FirebaseAuth firebaseAuth) {
        this.firebaseAuth = firebaseAuth;
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyToken(@RequestBody TokenRequest tokenRequest, HttpSession session) {
        try {
            // Validar que el token no sea nulo o vacío
            if (tokenRequest.getToken() == null || tokenRequest.getToken().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new AuthTokenResponse(null, null, "Token requerido", false));
            }

            // Verificar el token de Firebase
            DecodedToken decodedToken = firebaseAuth.verifyIdToken(tokenRequest.getToken());

            String uid = decodedToken.get();
            String email = decodedToken.getEmail();
            String displayName = decodedToken.getName();

            // Crear objeto con datos del usuario
            AuthTokenResponse response = new AuthTokenResponse(
                    uid,
                    email,
                    "Token validado exitosamente",
                    true
            );

            // Guardar datos en sesión (ejemplo de integración)
            session.setAttribute("firebaseUid", uid);
            session.setAttribute("userEmail", email);
            session.setAttribute("userName", displayName);

            return ResponseEntity.ok(response);

        } catch (FirebaseAuthException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthTokenResponse(null, null,
                            "Token inválido o expirado: " + e.getMessage(), false));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new AuthTokenResponse(null, null,
                            "Error en el servidor: " + e.getMessage(), false));
        }
    }
}

*/