package com.example.user.Repo;

import com.example.user.Model.Users;
import com.example.user.Model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<Users, Long> {
    @Query("SELECT u from Users u where u.email = ?1")
    Optional<Users> findUserByEmail(String email);
}
