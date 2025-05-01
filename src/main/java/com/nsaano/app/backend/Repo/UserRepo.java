package com.nsaano.app.backend.Repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nsaano.app.backend.Models.User;

public interface UserRepo extends JpaRepository<User, Long> {
    User findByPhoneNumberOrEmail(String phoneNumber, String email); // This is fine as long as your entity has the same field names
    User findByEmail(String email); 
    User findByPhoneNumber(String phoneNumber);
    User findByPhoneNumberAndPassword(String phoneNumber, String password);
     @Query("SELECT u FROM User u WHERE u.user_id = :userId")
    Optional<User> findByUserId(@Param("userId") String userId);
}
