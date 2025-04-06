package com.nsaano.app.backend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.nsaano.app.backend.Models.ServiceProvider;
import com.nsaano.app.backend.Repo.ServiceProviderRepo;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/providers") // Updated endpoint structure
public class ServiceProviderController {
    
    @Autowired
    private ServiceProviderRepo serviceProviderRepo;
    
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
    // Register a new Service Provider
    @PostMapping("/register")
    public ResponseEntity<String> registerServiceProvider(@RequestBody ServiceProvider serviceProvider) {
        if (serviceProviderRepo.findByEmail(serviceProvider.getEmail()) != null) {
            return ResponseEntity.badRequest().body("Email already in use");
        }

        // Encrypt password before saving
        serviceProvider.setPassword(passwordEncoder.encode(serviceProvider.getPassword()));
        serviceProviderRepo.save(serviceProvider);
        return ResponseEntity.ok("Service Provider registered successfully");
    }

    // Login Service Provider
    @PostMapping("/login")
    public ResponseEntity<String> loginServiceProvider(@RequestBody ServiceProvider serviceProvider) {
        ServiceProvider existingProvider = serviceProviderRepo.findByEmail(serviceProvider.getEmail());

        if (existingProvider != null && passwordEncoder.matches(serviceProvider.getPassword(), existingProvider.getPassword())) {
            return ResponseEntity.ok("Login successful");
        }
        return ResponseEntity.badRequest().body("Invalid email or password");
    }

    // Get Nearby Service Providers
    @GetMapping("/nearby")
    public ResponseEntity<List<ServiceProvider>> getNearbyProviders(
        @RequestParam double latitude, 
        @RequestParam double longitude, 
        @RequestParam double radius
    ) {
        List<ServiceProvider> providers = serviceProviderRepo.findNearbyProviders(latitude, longitude, radius);
        return ResponseEntity.ok(providers);
    }

    // Get a Service Provider by ID
    @GetMapping("/{id}")
    public ResponseEntity<ServiceProvider> getProviderDetails(@PathVariable Long id) {
        Optional<ServiceProvider> provider = serviceProviderRepo.findById(id);
        return provider.map(ResponseEntity::ok)
                       .orElse(ResponseEntity.notFound().build());
    }

    // Update a Service Provider’s Details
    @PutMapping("/{id}")
    public ResponseEntity<ServiceProvider> updateProvider(
        @PathVariable Long id,
        @RequestBody ServiceProvider provider
    ) {
        if (!serviceProviderRepo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        provider.setId(id);
        return ResponseEntity.ok(serviceProviderRepo.save(provider));
    }
}
