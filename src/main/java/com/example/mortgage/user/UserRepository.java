package com.example.mortgage.user;

import com.example.mortgage.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    // Custom query to find a user by id
//    User findById(long id);
}
