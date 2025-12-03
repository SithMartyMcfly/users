package com.usersproyect.users.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.usersproyect.users.Models.User;
import com.usersproyect.users.dao.userDAO;
import com.usersproyect.users.utils.JWTUtil;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;


@RestController
@RequestMapping("api/")
public class UserController {
    
    @Autowired
    private userDAO userDAO;
    @Autowired
    private JWTUtil jwtUtil;
    
    // Creamos un metodo que valida si el token es correcto y se lo implementamos a cada método que necesita autorización
    private boolean validateToken (String token) {
        String userId = jwtUtil.getKey(token);
        return userId != null;
    }
    
    // CURD REAL
    
    @GetMapping("/app/users")
    public List<User> GetUsers (@RequestHeader(value ="Authorization") String token) {
        
        
        if (!validateToken(token)){
            return null;
        }
        return userDAO.getUsers();
    }
    
    @DeleteMapping("/app/user/{id}")
    public void GetDelete (@RequestHeader(value = "Authorization") String token, 
                            @PathVariable int id) {
            if(!validateToken(token)){
                return;
            }
        userDAO.delete(id);
    }

    @PostMapping("/app/user")
    public void CreateUser (@RequestBody User usuario) {

        Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);
        String hash = argon2.hash(1, 1024, 1, usuario.getPassword());
        usuario.setPassword(hash);
        userDAO.createUser(usuario);
    }



    //  PRUEBAS PRE-CRUD
    @CrossOrigin
    @GetMapping("prueba")
    public List<String> prueba () {
        return List.of("Guti", "Cristina", "Juan");
    }

    @GetMapping("user")
    public User GetUserOne () {
        User usuario = new User();
            usuario.setNombre("Antonio");
            usuario.setApellido("Gutiérrez");
            usuario.setEmail("anto@user.com");
            usuario.setTelefono("987123");
            usuario.setPassword("12345");
        
        return usuario;
    }
    @GetMapping(value = "/app/user/{id}")
    public User GetUser () {
        User usuario = new User();
            usuario.setId(2324);
            usuario.setNombre("Antonio");
            usuario.setApellido("Gutiérrez");
            usuario.setEmail("anto@user.com");
            usuario.setTelefono("987123");
            usuario.setPassword("12345");
        
        return usuario;
    }
    
    @GetMapping("user/edit")
    public User GetEdit () {
        User usuario = new User();
            usuario.setNombre("Antonio");
            usuario.setApellido("Gutiérrez");
            usuario.setEmail("anto@user.com");
            usuario.setTelefono("987123");
            usuario.setPassword("12345");
        
        return usuario;
    }

}
