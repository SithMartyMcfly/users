package com.usersproyect.users.Controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.usersproyect.users.Models.User;
import com.usersproyect.users.dao.userDAO;
import com.usersproyect.users.utils.JWTUtil;


@RestController
@RequestMapping("api/")
public class AuthController {

    @Autowired
    private userDAO userDAO;
    @Autowired
    private JWTUtil jwtUtil;

     @PostMapping("/app/login")
    public String login (@RequestBody User usuario) {

        User loggedUser = userDAO.getLoginUser(usuario);
        
        if(loggedUser != null){
            //Creamos el token de JWT el metodo create() necesita el id (en String) y el mail o nombre de usuario
            String tokenJWT = jwtUtil.create(String.valueOf(loggedUser.getId()), loggedUser.getEmail());

            return tokenJWT;
        }
        return "FAIL";
    }
    
}
