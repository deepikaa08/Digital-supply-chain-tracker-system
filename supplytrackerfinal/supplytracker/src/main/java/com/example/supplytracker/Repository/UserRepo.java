package com.example.supplytracker.Repository;

import java.util.Optional;

import com.example.supplytracker.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User,Long>
{
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
}
