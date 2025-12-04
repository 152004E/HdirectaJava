/*
// java
package com.exe.Huerta_directa.Controllers;

import com.exe.Huerta_directa.DTO.TokenRequest;
import com.exe.Huerta_directa.Entity.User;
import com.exe.Huerta_directa.Repository.UserRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/verify")
    public ResponseEntity<?> verifyToken(@RequestBody TokenRequest request, HttpSession session) {
        try {
            FirebaseToken decoded = FirebaseAuth.getInstance().verifyIdToken(request.getToken());
            String uid = decoded.getUid();
            String email = decoded.getEmail(); // puede ser null

            UserRecord firebaseUser = FirebaseAuth.getInstance().getUser(uid);
            String firebasePhone = firebaseUser.getPhoneNumber(); // puede ser null

            // Buscar usuario local por email (ajusta si prefieres por phone/uid)
            if (email != null) {
                Optional<User> localOpt = userRepository.findByEmail(email);
                if (localOpt.isPresent()) {
                    User local = localOpt.get();

                    // Validar teléfono solo si el usuario local tiene phone no vacío
                    if (local.getPhone() != null && !local.getPhone().trim().isEmpty()) {
                        if (firebasePhone == null || !firebasePhone.equals(local.getPhone())) {
                            return ResponseEntity.status(401).body(Map.of("error", "Teléfono no validado con Firebase"));
                        }
                    }

                    session.setAttribute("user", local);
                    return ResponseEntity.ok(Map.of("uid", uid, "localUserId", local.getId()));
                }
            }
/*
            // Fallback: si no hay email o no se encontró, intentar por phone (opcional)
            if (firebasePhone != null) {
                Optional<User> byPhone = userRepository.findByPhone(firebasePhone);
                if (byPhone.isPresent()) {
                    User local = byPhone.get();
                    session.setAttribute("user", local);
                    return ResponseEntity.ok(Map.of("uid", uid, "localUserId", local.getId()));
                }
            }
*//*
            // Si no existe usuario local, devolver uid (token válido)
            return ResponseEntity.ok(Map.of("uid", uid));

        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("error", "Token inválido", "detail", e.getMessage()));
        }
    }
}
*/