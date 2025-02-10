package com.nsaano.app.backend.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.nsaano.app.backend.Models.User;
import com.nsaano.app.backend.Repo.UserRepo;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api") // Base path for all endpoints
public class ApiControllers {

    @Autowired
    private UserRepo userRepo;

    @GetMapping("/")
    public String getPage() {
        return "Welcome";
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        return userRepo.findAll();
    }

    @PostMapping("/register")
    public ResponseEntity<?> saveUser(@RequestBody User user) {
        try {
            // Validate required fields
            if (user.getFirst_name() == null || user.getLast_name() == null || user.getEmail() == null || user.getPassword() == null) {
                return ResponseEntity.badRequest().body("{\"message\": \"All fields are required\"}");
            }
    
            // Save user to database
            userRepo.save(user);
    
            // Return JSON response
            return ResponseEntity.status(HttpStatus.CREATED).body("{\"message\": \"User registered successfully\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"message\": \"Registration failed: " + e.getMessage() + "\"}");
        }
    }

    @PostMapping("/login")
public ResponseEntity<?> loginUser(@RequestBody User loginRequest) {
    try {
        // Find user by phone number or email
        User user = userRepo.findByPhoneNumberOrEmail(loginRequest.getPhone_number(), loginRequest.getEmail());

        if (user == null || !user.getPassword().equals(loginRequest.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"message\": \"Invalid phone number or password\"}");
        }

        // Return success response with user role (assuming you have a role field)
        return ResponseEntity.ok("{\"message\": \"Login successful\", \"role\": \"" + user.getRole() + "\", \"token\": \"dummy-token\"}");
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"message\": \"Login failed: " + e.getMessage() + "\"}");
    }
}

    
}