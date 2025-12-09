package com.usersproyect.users.Controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.usersproyect.users.Models.User;
import com.usersproyect.users.dao.userDAO;
import com.usersproyect.users.utils.JWTUtil;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import de.mkammerer.argon2.Argon2Factory.Argon2Types;
import jakarta.persistence.EntityManager;


@RestController
@RequestMapping("api/")
public class UserController {
    
    @Autowired
    private userDAO userDAO;
    @Autowired
    private JWTUtil jwtUtil;
    @Autowired
    private EntityManager entityManager;
    
    // Creamos un metodo que valida si el token es correcto y se 
    // lo implementamos a cada método que necesita autorización
    // NO necesito anotar el token con RequestHeader ya que este método 
    // usa su parámetro dentro de los otros métodos que SI están anotados
    private boolean validateToken (String token) {
        try {
            // Limpiamos el token que viene del LocalStorage
            if(token.startsWith("Bearer ")){
                String tokenClean = token.replaceAll("Bearer ", ""); 
                String userId = jwtUtil.getKey(tokenClean);
                return userId != null;
            } else {
                 String userId = jwtUtil.getKey(token);
                return userId != null;
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // CURD REAL

    @GetMapping("/app/user/{id}")
    public ResponseEntity<User> GetUser (@RequestHeader(value="Authorization") String token, @PathVariable int id){
        if (!validateToken(token)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Optional<User> user = userDAO.findById(id);

        if(user.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok(user.get());
    }
    
    @GetMapping("/app/users")
    // Pedimos como parametro el token de autorización que trae en el Header
    // en vez de ser un PathVariable, es un RequestHeader
    public ResponseEntity<List<User>> GetUsers (@RequestHeader(value ="Authorization") String token) {
        
        // Usando ResponseEntity damos respuesta a las respuesta que hace la API
        if (!validateToken(token)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(userDAO.findAll());
    }

    @PostMapping("/app/user")
    public ResponseEntity<User> CreateUser (@RequestBody User usuario) {
    
        Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);
        char[] passwordChars = usuario.getPassword().toCharArray();
        String hashPassword = argon2.hash(1, 1024, 1, passwordChars);
        usuario.setPassword(hashPassword);
        // Guardamos el usuario que traemos en el body con la contraseña hasheada
        userDAO.save(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    
    @DeleteMapping("/app/user/{id}")
    public ResponseEntity<Void> GetDelete (@RequestHeader(value = "Authorization") String token, 
                                            @PathVariable int id) {
            if(!validateToken(token)){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            userDAO.deleteById(id);
        return ResponseEntity.ok().build();
    }

    
     @PutMapping("/app/user/{id}")
     public ResponseEntity<User> EditUser ( @RequestHeader(value = "Authorization") 
                                            String token, 
                                            @RequestBody User user,
                                            @PathVariable int id){
                 if (!validateToken(token)){
                     return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
                 }
                 Optional<User> savedUser = userDAO.findById(id);

                 if(savedUser.isEmpty()){
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
                 }

                 User updatedUser = savedUser.get();
                 updatedUser.setNombre(user.getNombre());
                 updatedUser.setApellido(user.getApellido());
                 updatedUser.setTelefono(user.getTelefono());
                 updatedUser.setEmail(user.getEmail());

                 userDAO.save(updatedUser);

                 return ResponseEntity.ok(updatedUser);
     }
    
   
    
   


    // MÉTODO LOGIN
    public User getLoginUser (User usuario){
        // Se devuelve una lista de usuarios que tienen el mismo mail, debería ser solo uno
        List<User> lista = entityManager.createQuery("From User u where u.email = :email", User.class)
            .setParameter("email", usuario.getEmail())
            .getResultList();

            if (lista.isEmpty()){
                return null;
            }
            // Aquí verificamos la contraseña tomando el index 0 de la lista devuelta
            String passwordHashed = lista.get(0).getPassword(); // Recuperamos el primer pass del elemento primero, puesto que debe ser único
            char[] passwordChars = usuario.getPassword().toCharArray();
            Argon2 argon2 = Argon2Factory.create(Argon2Types.ARGON2id);
            
            if(argon2.verify(passwordHashed, passwordChars)){ // aquí verificamos que el pass que tomamos de la lista sea igual que el que hay en la BBDD
                return lista.get(0);
            } else {
                return null;
            }
    }

}
