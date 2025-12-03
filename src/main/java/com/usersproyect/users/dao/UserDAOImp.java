package com.usersproyect.users.dao;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.usersproyect.users.Models.User;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import de.mkammerer.argon2.Argon2Factory.Argon2Types;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Repository
@Transactional
public class UserDAOImp implements userDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<User> getUsers() {

        String query = "From User";
        List<User> users = entityManager.createQuery(query).getResultList(); 
        return users;
    }

    @Override
    public void delete(int id) {

        User usuario = entityManager.find(User.class, id);
        entityManager.remove(usuario);
    }

    @Override
    public void createUser(User usuario) {

       entityManager.merge(usuario);
    }

    @Override
    public User getLoginUser (User usuario){
        // Se devuelve una lista de usuarios que tienen el mismo mail, debería ser solo uno
        String query = "From User u where u.email = :email";
        List<User> lista = entityManager.createQuery(query)
            .setParameter("email", usuario.getEmail())
            .getResultList();

            if (lista.isEmpty()){
                return null;
            }
            // Aquí verificamos la contraseña tomando el index 0 de la lista devuelta
            String passwordHashed = lista.get(0).getPassword(); // Recuperamos el primer pass del elemento primero, puesto que debe ser único

            Argon2 argon2 = Argon2Factory.create(Argon2Types.ARGON2id);
            
            if(argon2.verify(passwordHashed, usuario.getPassword())){ // aquí verificamos que el pass que tomamos de la lista sea igual que el que hay en la BBDD
                return lista.get(0);
            } else {
                return null;
            }
    }

    

}
