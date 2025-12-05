$(document).ready(function() {
  // on ready
});

async function logInUser() {
    let datos = {
    };
    datos.email = document.getElementById('inputEmail').value;
    datos.password = document.getElementById('inputPassword').value;

   
  const response = await fetch('/api/app/login', { //marca el método que llama en el controlador CREATEUSER
    method: 'POST', //usamos POST porque va a crear datos
    headers: {
      'Accept': 'application/json',
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(datos)
  });
  const request = await response.text();

  if (request != 'FAIL' ){
    // Añadimos la info a localStorage, al token le añadimos el standard BEARER
    localStorage.token = 'Bearer ' + request;
    localStorage.email = datos.email;
    window.location.href = 'users.html';
  } else {
    alert("Contraseña o usuario no válido.")
  }
}