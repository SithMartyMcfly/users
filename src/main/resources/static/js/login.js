$(document).ready(function() {
  // on ready
});

async function logInUser() {
    let datos = {
    };
    datos.email = document.getElementById('inputEmail').value;
    datos.password = document.getElementById('inputPassword').value;

   
  const response = await fetch('/api/app/login', { //marca el método que llama en el controlador AuthController
                                                  //este le devuelve el JWToken, necesita el await para esperar la respuesta
                                                  // que viene en HTTP
    method: 'POST', //usamos POST porque va a crear datos
    headers: {
      'Accept': 'application/json',
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(datos)
  });
  const token = await response.text(); // necesita el await para leer el body como un texto
  
  if (response.ok){
    // Añadimos la info a localStorage, al token le añadimos el standard BEARER
    localStorage.token = 'Bearer ' + token; // el token contenido en request se le añade Bearer
    localStorage.email = datos.email;
    window.location.href = 'users.html'; // enviamos a users.html
  } else if (response.status === 401) {
    alert("Contraseña o usuario no válido.");
  } else {
    alert("Error del servidor");
  }
}