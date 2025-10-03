<!-- Script para validación adicional -->

// Mejorar la experiencia del usuario
document.addEventListener('DOMContentLoaded', function() {
    const form = document.querySelector('form');
    const inputs = document.querySelectorAll('input[type="text"], input[type="email"], input[type="password"]');

    // Agregar efectos focus
    inputs.forEach(input => {
        input.addEventListener('focus', function() {
            this.parentElement.style.transform = 'scale(1.02)';
        });

        input.addEventListener('blur', function() {
            this.parentElement.style.transform = 'scale(1)';
        });
    });

    // Validación antes de enviar
    form.addEventListener('submit', function(e) {
        const name = document.querySelector('input[name="name"]').value;
        const email = document.querySelector('input[type="email"]').value;
        const password = document.querySelector('input[type="password"]').value;

        if (name.trim().length < 2) {
            alert('El nombre debe tener al menos 2 caracteres.');
            e.preventDefault();
            return;
        }

        if (password.length < 8) {
            alert('La contraseña debe tener al menos 8 caracteres.');
            e.preventDefault();
            return;
        }

        // Confirmación final
        if (!confirm(`¿Confirma registrar al administrador?\n\nNombre: ${name}\nEmail: ${email}`)) {
            e.preventDefault();
        }
    });
});
