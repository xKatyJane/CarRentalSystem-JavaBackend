package com.ironhack.carrentalsystem.repository;

import com.ironhack.carrentalsystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User save(User user);
    List<User> findAll();
    Optional<User> findByUsername(String username);
    List<User> findByRoles_Name(String rolesName);
    Long id(Long id);
}