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
    try {
        if (serviceProviderRepo.findByEmail(serviceProvider.getEmail()) != null) {
            return ResponseEntity.badRequest().body("Email already in use");
        }
    
        // Encrypt password before saving
        serviceProvider.setPassword(passwordEncoder.encode(serviceProvider.getPassword()));
    
        // Save to get ID
        serviceProviderRepo.save(serviceProvider);
    
        // Set custom ID after saving
        serviceProvider.setService_provider_id(ServiceProvider.generateServiceProviderId(serviceProvider.getId()));
    
        // Save updated service provider
        serviceProviderRepo.save(serviceProvider);
    
        return ResponseEntity.ok("Service Provider registered successfully");
    }catch (Exception e) {
            e.printStackTrace();  // Log the error
            return ResponseEntity.status(500).body("Internal Server Error");
    }
}
    



    // Login Service Provider
    @PostMapping("/login")
    public ResponseEntity<String> loginServiceProvider(@RequestBody ServiceProvider serviceProvider) {
        ServiceProvider existingProvider = serviceProviderRepo.findByPhoneNumber(serviceProvider.getPhoneNumber());
    
        if (existingProvider != null && passwordEncoder.matches(serviceProvider.getPassword(), existingProvider.getPassword())) {
            return ResponseEntity.ok("Login successful");
        }
        return ResponseEntity.badRequest().body("Invalid phone number or password");
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
     @GetMapping("/serviceproviders")
    public List<ServiceProvider> getAllServiceProviders() {
        return serviceProviderRepo.findAll();
    }

    // Update a Service Provider’s Details
    @PutMapping("/{id}")
    public ResponseEntity<ServiceProvider> updateProvider(
        @PathVariable Long id,
        @RequestBody ServiceProvider provider
    ) {
        Optional<ServiceProvider> existingOpt = serviceProviderRepo.findById(id);
        if (existingOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
    
        ServiceProvider existing = existingOpt.get();
    
        // Only update necessary fields
        existing.setEmail(provider.getEmail());
        existing.setPhoneNumber(provider.getPhoneNumber());
        existing.setLocation(provider.getLocation());
        existing.setServiceType(provider.getServiceType());
        existing.setProfilePicture(provider.getProfilePicture());
        existing.setBusinessName(provider.getBusinessName());
        // ... more fields you want to allow updating
    
        return ResponseEntity.ok(serviceProviderRepo.save(existing));
    }
    
}

