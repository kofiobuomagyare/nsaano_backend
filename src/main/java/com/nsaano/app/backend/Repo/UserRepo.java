package com.nsaano.app.backend.Repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nsaano.app.backend.Models.User;

public interface UserRepo extends JpaRepository<User, Long> {
User findByPhoneNumberOrEmail(String phoneNumber, String email);
User findByEmail(String email);
User findByPhoneNumber(String phone);
}
