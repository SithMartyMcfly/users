$(document).ready(function() {
    // tenemos que recuperar el id para pasarlo al método getUser
    const params = new URLSearchParams(window.location.search);
    const id = params.get("id"); // obtiene el id de la URL
    if (id){
        getUser(id)
    } else{
        alert('No se ha proporcionado ID');
    }
    console.log(id);
    activeUserMail();
    console.log(localStorage.email)
});

function activeUserMail(){
  // Recuperamos del localStorage el mail y le damos el valor al elemento
  document.querySelector('#txt-id-user').textContent = localStorage.email; 
}

// AUTORIZACIÓN HEADERS

function getHeaderAuthorization (){
  return {
    'Accept': 'application/json',
    'Content-Type': 'application/json',
    'Authorization': localStorage.token
  }

}

async function getUser(id){
  console.log('ejecuto VER');
  const request = await fetch('/api/app/user/' + id, { //marca el método que llama en el controlador, GETUSERS
    method: 'GET', //usamos GET porque va a devolver datos
    headers: getHeaderAuthorization()
    
  });
  
  if(request.ok){
      const user = await request.json();
        document.querySelector('#txt-name-user').textContent = user.nombre
        document.getElementById('input-name').innerHTML = user.nombre;
        document.getElementById('input-lastName').innerHTML = user.apellido;
        document.getElementById('input-phone').innerHTML = user.telefono;
        document.getElementById('input-email').innerHTML = user.email;
  } else {
    alert('Error al cargar usuario');
  }
}