package com.softserve.itacademy.todolist.repository;

import com.softserve.itacademy.todolist.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}
