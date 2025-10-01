const container = document.getElementById("container");
const registerBtn = document.getElementById("register");
const loginBtn = document.getElementById("login");
const passwordOjo = document.getElementById("passwordOjo");
const passwordInput = document.getElementById("passwordInput");
const passwordOjoOff = document.getElementById("passwordOjoOff");
const ojo = document.getElementById("ojo");

// al inicio mostramos solo el ojo abierto
passwordOjoOff.style.display = "none";

// evento para mostrar contraseña
passwordOjo.addEventListener("click", () => {
  passwordInput.setAttribute("type", "text");
  console.log("ver");

  passwordOjo.style.display = "none";
  passwordOjoOff.style.display = "inline";
  ojo.style.marginTop = "5px";
});

// evento para ocultar contraseña
passwordOjoOff.addEventListener("click", () => {
  passwordInput.setAttribute("type", "password");
  console.log("no ver");

  passwordOjo.style.display = "inline";
  passwordOjoOff.style.display = "none";
  ojo.style.marginTop = "5px";
});

registerBtn.addEventListener("click", () => {
  container.classList.add("active");
});

loginBtn.addEventListener("click", () => {
  container.classList.remove("active");
});
console.log("js");
function validarCorreo(idCorreo) {
  const correo = document.getElementById(idCorreo).value;
  const patronEmail = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

  if (!patronEmail.test(correo)) {
    alert("Por favor ingresa un correo electrónico válido.");
    return false;
  }
  return true;
}

function validarRegistro() {
  const password = document.getElementById("passwordRegistro").value;

  const tieneMayuscula = /[A-Z]/.test(password);
  const tieneMinuscula = /[a-z]/.test(password);
  const tieneNumero = /[0-9]/.test(password);
  const longitudValida = password.length >= 8;

  if (!tieneMayuscula || !tieneMinuscula || !tieneNumero || !longitudValida) {
    alert(
      "La contraseña debe contener al menos:\n- 8 caracteres\n- 1 letra mayúscula\n- 1 letra minúscula\n- 1 número"
    );
    return false;
  }

  return true;
}
