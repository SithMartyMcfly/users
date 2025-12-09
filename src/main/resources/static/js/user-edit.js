$(document).ready(function () {
  // Recuperamos el id de la URL
  const params = new URLSearchParams(window.location.search);
  const rawId = params.get("id");
  console.log("ID raw en load:", rawId, "typeof:", typeof rawId);

  if (rawId) {
    // Guardamos el id en el hidden y en window.userId
    window.userId = rawId;
    document.getElementById("input-id").value = rawId;
    console.log("ID guardado en window.userId:", window.userId);

    // Cargamos datos del usuario
    getUser(window.userId);
  } else {
    alert("No se ha proporcionado un ID válido");
    return;
  }

  // Mostramos el mail del usuario activo
  activeUserMail();

  // Evento submit del formulario
  document.querySelector("#edit-user-form").addEventListener("submit", function (e) {
    e.preventDefault(); // evita recarga
    const id = document.getElementById("input-id").value;
    console.log("ID en submit:", id);

    if (!id) {
      alert("No id");
      return;
    }

    PutUser(id); // usamos el id del hidden
  });
});

// Mostrar email del usuario activo
function activeUserMail() {
  document.querySelector("#txt-id-user").textContent = localStorage.email;
  //document.querySelector("#txt-name-user").textContent = user.name;

}

// Headers de autorización
function getHeaderAuthorization() {
  return {
    Accept: "application/json",
    "Content-Type": "application/json",
    Authorization: localStorage.token // añade "Bearer " si tu backend lo requiere
  };
}

// GET usuario por id
async function getUser(id) {
  console.log("GET user con id:", id);
  const response = await fetch("/api/app/user/" + id, {
    method: "GET",
    headers: getHeaderAuthorization(),
  });

  console.log("GET status:", response.status);

  if (response.ok) {
    const user = await response.json();
    console.log("Usuario cargado:", user);

    // Guardamos id en hidden
    document.getElementById("input-id").value = user.id;

    // Rellenamos campos
    document.querySelector("#txt-name-user").textContent = user.nombre;
    document.getElementById("input-name").value = user.nombre;
    document.getElementById("input-lastName").value = user.apellido;
    document.getElementById("input-phone").value = user.telefono;
    document.getElementById("input-email").value = user.email;
  } else {
    alert("Error al cargar usuario");
  }
}

// PUT usuario por id
async function PutUser(id) {
  console.log("PUT user con id:", id);

  const user = {
    nombre: document.getElementById("input-name").value,
    apellido: document.getElementById("input-lastName").value,
    telefono: document.getElementById("input-phone").value,
    email: document.getElementById("input-email").value,
  };
  console.log("Objeto enviado en PUT:", user);

  const response = await fetch("/api/app/user/" + id, {
    method: "PUT",
    headers: getHeaderAuthorization(),
    body: JSON.stringify(user),
  });

  console.log("PUT status:", response.status);

  if (response.ok) {
    const updated = await response.json();
    console.log("Usuario actualizado:", updated);
    alert("Usuario actualizado correctamente");
    // Si quieres quedarte en la misma vista, quita la siguiente línea
    window.location.href = "users.html";
  } else {
    const errText = await response.text().catch(() => "");
    console.log("PUT error body:", errText);
    alert("Error al actualizar usuario");
  }
}