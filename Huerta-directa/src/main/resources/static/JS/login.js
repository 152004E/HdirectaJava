// login.js
import { auth } from "./firebase-config.js";
import {
    RecaptchaVerifier,
    signInWithPhoneNumber
} from "https://www.gstatic.com/firebasejs/11.0.1/firebase-auth.js";

// Simulación de API PHP para traer datos del usuario
// Tú ya tienes esto en tu backend: valida y devuelve si tiene 2FA
async function traerInfoUsuario(email, password) {
    const res = await fetch("backend/login.php", {
        method: "POST",
        body: JSON.stringify({ email, password }),
        headers: { "Content-Type": "application/json" }
    });
    return await res.json();
}

// LOGIN NORMAL
document.getElementById("loginForm").addEventListener("submit", async (e) => {
    e.preventDefault();

    const email = document.getElementById("email").value;
    const pass  = document.getElementById("password").value;

    const user = await traerInfoUsuario(email, pass);

    if (!user.success) {
        alert("Usuario o contraseña incorrectos");
        return;
    }

    // Si no usa 2FA → login directo
    if (!user.two_factor_enabled) {
        window.location.href = "dashboard.html";
        return;
    }

    // Si usa 2FA → pedimos el teléfono y enviamos SMS
    document.getElementById("telefono").value = user.telefono;
    document.getElementById("telefono").style.display = "block";

    // Mostramos botón de envío del código SMS
    document.getElementById("btnEnviarCodigo").style.display = "block";

    iniciarRecaptcha();
});

// Inicializar reCAPTCHA invisible
function iniciarRecaptcha() {
    window.recaptchaVerifier = new RecaptchaVerifier(auth, "btnEnviarCodigo", {
        size: "invisible"
    });
}

// ENVIAR SMS
document.getElementById("btnEnviarCodigo").addEventListener("click", async () => {
    const numero = document.getElementById("telefono").value;

    try {
        const appVerifier = window.recaptchaVerifier;

        const confirmationResult = await signInWithPhoneNumber(auth, numero, appVerifier);

        // Lo guardamos para usar en el otro HTML
        sessionStorage.setItem("confirmacionSMS", JSON.stringify(confirmationResult));

        window.location.href = "verificar-sms.html";

    } catch (e) {
        alert("Error enviando mensaje: " + e.message);
    }
});
