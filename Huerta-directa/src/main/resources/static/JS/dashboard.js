let toggle = document.querySelector(".toggle");
let navigation = document.querySelector(".navigation");
let main = document.querySelector(".main");

// toggle.onclick = function(){
//     navigation.classList.toggle("active");
//     main.classList.toggle("active");

// }

console.log("scrip ejecutado");

let list = document.querySelectorAll(".navigation li");
function activeLink() {
  list.forEach((item) => {
    item.classList.remove("hovered");
  });
  this.classList.add("hovered");
}
list.forEach((item) => item.addEventListener("mouseover", activeLink));

function toggleCard(id) {
  const content = document.getElementById(id);
  const icon = content.previousElementSibling.querySelector(
    ".material-symbols-outlined"
  );
  if (content.classList.contains("hidden")) {
    content.classList.remove("hidden");
    icon.textContent = "expand_less";
  } else {
    content.classList.add("hidden");
    icon.textContent = "expand_more";
  }
}
function confirmarEliminacion(id) {
  Swal.fire({
    title: '¿Estás seguro?',
    text: "Esta acción no se puede deshacer.",
    icon: 'warning',
    showCancelButton: true,
    confirmButtonColor: '#d33',
    cancelButtonColor: '#3085d6',
    confirmButtonText: 'Sí, eliminar',
    cancelButtonText: 'Cancelar'
  }).then((result) => {
    if (result.isConfirmed) {
      window.location.href = '/deleteComment/' + id;
    }
  });
}