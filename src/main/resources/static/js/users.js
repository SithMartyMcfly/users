// Call the dataTables jQuery plugin
$(document).ready(function() {
  loadUsers();
  $('#dataTable').DataTable();
  activeUserMail();
});


function activeUserMail(){
  // Recuperamos del localStorage el mail y le damos el valor al elemento
  document.querySelector('#txt-id-user').outerHTML = localStorage.email; 

}

// AUTORIZACIÓN HEADERS

function getHeaderAuthorization (){
  return {
    'Accept': 'application/json',
    'Content-Type': 'application/json',
    'Authorization': localStorage.token
  }

}


//FUNCION FETCH QUE CARGA USUARIOS
async function loadUsers() {
  console.log('ejecuto CARGA');
  const request = await fetch('/api/app/users', { //marca el método que llama en el controlador, GETUSERS
    method: 'GET', //usamos GET porque va a devolver datos
    headers: getHeaderAuthorization()
    
  });
  const users = await request.json();
  
  
  let tbody = document.querySelector('#users tbody');
  tbody.innerHTML = ""; // Limpiamos contenido del HTML
  // Usamos un foreach para sacar todo lo que tenemos dentro de users y por cada elemento (user)
  // concatenamos una fila más  
  users.forEach(user => {
    let deleteButton = `<a href=# onclick="deleteUser(${user.id})" class="btn btn-danger btn-circle btn-sm">
                          <i class="fas fa-trash"></i>
                        </a>`;
    let viewButton = `<a href="user-view.html?id=${user.id}" class="btn btn-info btn-circle btn-sm">
                          <i class="fas fa-info"></i>
                        </a>`;
    let editButton = `<a href="user-edit.html?id=${user.id}" class="btn btn-warning btn-circle btn-sm">
                          <i class="fas fa-edit"></i>
                        </a>`;
    // En cada fila que construimos con HTML le injectamos el atributo del Objeto de la List
    let userHTML = `
    <tr>
      <td>${user.nombre}</td>
      <td>${user.apellido}</td>
      <td>${user.email}</td>
      <td>${user.telefono}</td>
      <td>
        ${deleteButton} ${viewButton} ${editButton}
      </td>
    </tr>
    `
    // Concatenamos cada fila
    tbody.innerHTML += userHTML; 
  });
  
}

// ELIMINAR USUARIOS

async function deleteUser (id) {
  console.log('ejecuto ELIMINAR');
  if (!confirm('¿Desea eliminar el usuario?')){ // Confirm es una función que saca una ventana de confirmación
                                                // en el momento que se da a cancelar rompe esta ejecución
    return;
  }
  const request = await fetch('/api/app/user/' + id, { //marca el método que llama en el USERCONTROLLER, DELETEUSER
    method: 'DELETE', //usamos DELETE porque va a borrar datos
    headers: getHeaderAuthorization()
  });
  location.reload(); // Recarga la página
}
