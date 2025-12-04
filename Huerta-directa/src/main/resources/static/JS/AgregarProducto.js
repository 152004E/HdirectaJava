const inputImagen = document.getElementById("imagen");
      const fileNameSpan = document.getElementById("file-name");

      inputImagen.addEventListener("change", function () {
        fileNameSpan.textContent =
          inputImagen.files.length > 0
            ? inputImagen.files[0].name
            : "Ningún archivo seleccionado";
      });



      
      // Nombre
      document.getElementById("nombre").addEventListener("input", function () {
        document.getElementById("prev-nombre").textContent =
          this.value || "Nombre del Producto";
      });

      // Descripción
      document
        .getElementById("descripcion")
        .addEventListener("input", function () {
          document.getElementById("prev-descripcion").textContent =
            this.value ||
            "Breve descripción del producto que se mostrará aquí.";
        });

      // Precio
      document.getElementById("precio").addEventListener("input", function () {
        document.getElementById("prev-precio").textContent = this.value
          ? "$" + parseFloat(this.value).toFixed(2)
          : "$0.00";
      });

      // Unidad de medida
      document.getElementById("unidad").addEventListener("change", function () {
        document.getElementById("prev-unidad").textContent =
          this.options[this.selectedIndex].text || "Unidad";
      });

      // Categoría
      document
        .getElementById("categoria-producto")
        .addEventListener("change", function () {
          const text = this.options[this.selectedIndex].text;
          document.getElementById("prev-categoria").textContent = text;
        });

      // Imagen
      document
        .getElementById("imagen")
        .addEventListener("change", function (event) {
          const file = event.target.files[0];
          if (!file) return;

          const reader = new FileReader();
          reader.onload = function (e) {
            document
              .querySelector(".material-icons-outlined")
              .classList.add("hidden");
            const imgPreviewContainer = document.querySelector(".w-full.h-48");

            imgPreviewContainer.innerHTML = `<img src="${e.target.result}" class="w-full h-full object-cover rounded-md" />`;
          };
          reader.readAsDataURL(file);
        });