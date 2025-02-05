package com.nsaano.app.backend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.nsaano.app.backend.Models.ServiceProvider;
import com.nsaano.app.backend.Repo.ServiceProviderRepo;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@RestController
@RequestMapping("/service-provider")
public class ServiceProviderController {
    
    @Autowired
    private ServiceProviderRepo serviceProviderRepo;
    
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
    @PostMapping("/register")
    public String registerServiceProvider(@RequestBody ServiceProvider serviceProvider) {
        // Check if email already exists
        if (serviceProviderRepo.findByEmail(serviceProvider.getEmail()) != null) {
            return "Email already in use";
        }
        
        // Encrypt password before saving
        serviceProvider.setPassword(passwordEncoder.encode(serviceProvider.getPassword()));
        serviceProviderRepo.save(serviceProvider);
        return "Service Provider registered successfully";
    }
    
    @PostMapping("/login")
    public String loginServiceProvider(@RequestBody ServiceProvider serviceProvider) {
        ServiceProvider existingProvider = serviceProviderRepo.findByEmail(serviceProvider.getEmail());
        
        if (existingProvider != null && passwordEncoder.matches(serviceProvider.getPassword(), existingProvider.getPassword())) {
            return "Login successful";
        }
        return "Invalid email or password";
    }
}
