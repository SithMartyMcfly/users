$(document).ready(function() {
  // on ready
});

async function createUser () {
    let datos = {

    };
    datos.nombre = document.getElementById('inputName').value;
    datos.apellido = document.getElementById('inputLastName').value;
    datos.email = document.getElementById('inputEmail').value;
    datos.telefono = document.getElementById('inputPhone').value;
    datos.password = document.getElementById('inputPassword').value;

    let confirmPasword = document.getElementById('inputRepeatPassword').value;

    if (confirmPasword !== datos.password){
        alert("La contraseña es diferente")
        return;
    }
  const response = await fetch('/api/app/user', { //marca el método que llama en el controlador CREATEUSER
    method: 'POST', //usamos POST porque va a crear datos
    headers: {
      'Accept': 'application/json',
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(datos)
  });
  if (response.ok) {
        alert("Usuario creado correctamente");
        window.location.href = "login.html"
    } else {
        alert("Error al crear usuario");
    }
}