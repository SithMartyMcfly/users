package com.usersproyect.users.dao;

import java.util.List;

import com.usersproyect.users.Models.User;

public interface userDAO {

    List<User> getUsers ();

    void delete(int id);

    void createUser(User usuario);

    User getLoginUser(User usuario);
}
