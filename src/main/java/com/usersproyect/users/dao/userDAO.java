package com.usersproyect.users.dao;



import org.springframework.data.jpa.repository.JpaRepository;

import com.usersproyect.users.Models.User;

public interface userDAO extends JpaRepository<User, Integer> {

}
