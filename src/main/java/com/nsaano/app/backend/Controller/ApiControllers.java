package com.nsaano.app.backend.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.nsaano.app.backend.Models.User;
import com.nsaano.app.backend.Repo.UserRepo;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api") // Base path for all endpoints
public class ApiControllers {

    @Autowired
    private UserRepo userRepo;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


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
            user.setPassword(passwordEncoder.encode(user.getPassword()));
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

        if (user == null || !passwordEncoder.matches(loginRequest.getPassword(), user.getPassword()))
 {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"message\": \"Invalid phone number or password\"}");
        }

        // Return success response with user role (assuming you have a role field)
        return ResponseEntity.ok("{\"message\": \"Login successful\", \"role\": \"" + user.getRole() + "\", \"token\": \"dummy-token\"}");
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"message\": \"Login failed: " + e.getMessage() + "\"}");
    }
}

@PutMapping("/users/reset-password")
public ResponseEntity<?> resetPassword(@RequestParam String phoneOrEmail, @RequestParam String newPassword) {
    User user = userRepo.findByPhoneNumber(phoneOrEmail);
    if (user == null) {
        user = userRepo.findByEmail(phoneOrEmail);
    }

    if (user == null) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "User not found"));
    }

    user.setPassword(passwordEncoder.encode(newPassword));
    userRepo.save(user);
    return ResponseEntity.ok(Map.of("message", "Password reset successfully"));
}


@GetMapping("/users/findUserIdByPhone")
public ResponseEntity<?> findUserIdByPhone(@RequestParam String phoneNumber) {
    User user = userRepo.findByPhoneNumber(phoneNumber); // Use the method from UserRepo to find the user by phone number

    if (user == null) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                             .body("{\"message\": \"User not found\"}");
    }

    // Return the user ID or any relevant information you want
    return ResponseEntity.ok("{\"user_id\": \"" + user.getUser_id() + "\"}");
}
@GetMapping("/users/findUserIdByPhoneNumberAndPassword")
public ResponseEntity<?> findUserIdByPhoneNumberAndPassword(@RequestParam String phoneNumber, @RequestParam String password) {
    User user = userRepo.findByPhoneNumberAndPassword(phoneNumber, password);

    if (user != null) {
        return ResponseEntity.ok().body(Map.of("user_id", user.getUser_id()));
    } else {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "User not found"));
    }
}

@GetMapping("/users/login")
public ResponseEntity<?> login(@RequestParam String phoneNumber, @RequestParam String password) {
    User user = userRepo.findByPhoneNumberAndPassword(phoneNumber, password);

    if (user != null) {
        Map<String, Object> profile = Map.of(
            "user_id", user.getUser_id(),
            "first_name", user.getFirst_name(),
            "last_name", user.getLast_name(),
            "email", user.getEmail(),
            "phone_number", user.getPhone_number(),
            "age", user.getAge(),
            "gender", user.getGender(),
            "address", user.getAddress(),
            "profile_picture", user.getProfile_picture()
        );

        return ResponseEntity.ok().body(profile);
    } else {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "User not found"));
    }
}

    @GetMapping("/users/profile")
public ResponseEntity<?> getUserProfileByPhone(@RequestParam String phoneNumber) {
    try {
        // Find user by phone number
        User user = userRepo.findByPhoneNumber(phoneNumber);
        
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"message\": \"User not found\"}");
        }
        
        // Build the profile response
        Map<String, Object> profile = new HashMap<>();
        profile.put("first_name", user.getFirst_name());
        profile.put("last_name", user.getLast_name());
        profile.put("email", user.getEmail());
        profile.put("phone_number", user.getPhone_number());
        profile.put("gender", user.getGender() != null ? user.getGender() : "");
        
        // Handle age differently since it's a primitive int
        profile.put("age", user.getAge()); // This will be 0 if not set
        
        profile.put("address", user.getAddress() != null ? user.getAddress() : "");
        profile.put("profile_picture", user.getProfile_picture() != null ? user.getProfile_picture() : "");
        
        return ResponseEntity.ok(profile);
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("{\"message\": \"Error retrieving profile: " + e.getMessage() + "\"}");
    }
}

//endpoint to update user profile
@PutMapping("/users/update-profile")
public ResponseEntity<?> updateUserProfile(@RequestParam String phoneNumber, @RequestBody User updatedUser) {
    try {
        User user = userRepo.findByPhoneNumber(phoneNumber);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body(Map.of("message", "User not found with phone number: " + phoneNumber));
        }

        // Update only the fields that are not null in updatedUser
        if (updatedUser.getFirst_name() != null) user.setFirst_name(updatedUser.getFirst_name());
        if (updatedUser.getLast_name() != null) user.setLast_name(updatedUser.getLast_name());
        if (updatedUser.getEmail() != null) user.setEmail(updatedUser.getEmail());
        if (updatedUser.getGender() != null) user.setGender(updatedUser.getGender());
        if (updatedUser.getAddress() != null) user.setAddress(updatedUser.getAddress());
        if (updatedUser.getProfile_picture() != null) user.setProfile_picture(updatedUser.getProfile_picture());

        // Only update age if it's not 0
        if (updatedUser.getAge() > 0) user.setAge(updatedUser.getAge());

        userRepo.save(user);
        return ResponseEntity.ok(Map.of("message", "Profile updated successfully"));

    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .body(Map.of("message", "Error updating profile", "error", e.getMessage()));
    }
}


//endpoint to update availability
@PutMapping("/users/update-availability")
public ResponseEntity<?> updateAvailability(@RequestParam String phoneNumber, @RequestParam boolean isAvailable) {
    try {
        User user = userRepo.findByPhoneNumber(phoneNumber);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "User not found with phone number: " + phoneNumber));
        }
        
        // Debug log to verify user object
        System.out.println("Found user: " + user.getFirst_name() + ", current availability: " + user.isAvailable());
        
        user.setAvailable(isAvailable);
        userRepo.save(user);
        
        return ResponseEntity.ok(Map.of(
            "message", "Availability updated successfully", 
            "isAvailable", user.isAvailable()
        ));
    } catch (Exception e) {
        e.printStackTrace(); // Add this for better debugging
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "Failed to update availability", "error", e.getMessage()));
    }
}

@GetMapping("/users/findByUserId/{userId}")
public ResponseEntity<?> getUserByUserId(@PathVariable String userId) {
    try {
        Optional<User> user = userRepo.findByUserId(userId);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("message", "User not found"));
        }
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(Map.of("error", e.getMessage()));
    }
}

@PostMapping("/logout")
    public ResponseEntity<?> logoutUser() {
        // Logout is handled client-side by deleting the JWT token.
        return ResponseEntity.ok("{\"message\": \"Logout successful\"}");
    }

    
}