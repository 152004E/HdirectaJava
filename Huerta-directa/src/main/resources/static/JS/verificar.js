document.getElementById("btnVerificar").addEventListener("click", async () => {
    const code = document.getElementById("codigo").value;

    const confirmationObj = JSON.parse(sessionStorage.getItem("confirmacionSMS"));

    try {
        await confirmationObj.confirm(code);

        // SMS correcto → entrar
        window.location.href = "dashboard.html";

    } catch (e) {
        alert("Código incorrecto o expirado");
    }
});
