package com.nsaano.app.backend.Repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nsaano.app.backend.Models.User;

public interface UserRepo extends JpaRepository<User, Long> {
    User findByPhoneNumberOrEmail(String phoneNumber, String email); // This is fine as long as your entity has the same field names
    User findByEmail(String email); 
    User findByPhoneNumber(String phoneNumber);
    User findByPhoneNumberAndPassword(String phoneNumber, String password);
    User findByUser_id(Long userId);
    
}
