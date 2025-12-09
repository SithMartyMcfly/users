package com.usersproyect.users.Controllers;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.usersproyect.users.Models.User;
import com.usersproyect.users.utils.JWTUtil;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import de.mkammerer.argon2.Argon2Factory.Argon2Types;
import jakarta.persistence.EntityManager;


@RestController
@RequestMapping("api/")
public class AuthController {

    @Autowired
    private EntityManager entityManager;
    @Autowired
    private JWTUtil jwtUtil;

    // Método que comprueba que la contraseña que introduce el usuario coincide con
    // la contraseña hash que tenemos en la BBDD ya guardada y hasheada.
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
            char[] passwordChars = usuario.getPassword().toCharArray(); // Pasamos a un array de char
            Argon2 argon2 = Argon2Factory.create(Argon2Types.ARGON2id);
            
            if(argon2.verify(passwordHashed, passwordChars)){ // aquí verificamos que el pass que tomamos de la lista sea igual que el que hay en la BBDD
                return lista.get(0);
            } else {
                return null;
            }
    }

     @PostMapping("/app/login")
    public ResponseEntity<?> login (@RequestBody User usuario) {
        // Almacena el usuario que devuelve getLoginUser()
        User loggedUser = getLoginUser(usuario);
        
        if(loggedUser != null){
            //Creamos el token de JWT el metodo create() necesita el id (en String) y el mail o nombre de usuario
            String tokenJWT = jwtUtil.create(
                                            String.valueOf(loggedUser.getId()), 
                                            loggedUser.getEmail()
                                            );

            return ResponseEntity.ok(tokenJWT) ;
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
    }
    
}
