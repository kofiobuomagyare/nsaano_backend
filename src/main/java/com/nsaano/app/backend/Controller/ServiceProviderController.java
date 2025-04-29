package com.nsaano.app.backend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.nsaano.app.backend.Models.Location;
import com.nsaano.app.backend.Models.ServiceProvider;
import com.nsaano.app.backend.Repo.ServiceProviderRepo;

import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Map;
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

    @PutMapping("/reset-password")
public ResponseEntity<String> resetPassword(
        @RequestParam String phoneNumber,
        @RequestBody Map<String, String> body) {

    ServiceProvider provider = serviceProviderRepo.findByPhoneNumber(phoneNumber);
    if (provider == null) {
        return ResponseEntity.badRequest().body("Service provider not found");
    }

    String newPassword = body.get("newPassword");
    if (newPassword == null || newPassword.length() < 6) {
        return ResponseEntity.badRequest().body("Password must be at least 6 characters");
    }

    // Encrypt new password
    provider.setPassword(passwordEncoder.encode(newPassword));
    serviceProviderRepo.save(provider);

    return ResponseEntity.ok("Password reset successful");
}

    
     // Get Service Providers by Service Type
     @GetMapping("/service_type")
     public ResponseEntity<List<ServiceProvider>> getServiceProvidersByType(
         @RequestParam List<String> serviceTypes) {
         List<ServiceProvider> providers = serviceProviderRepo.findByServiceTypeIn(serviceTypes);
         return ResponseEntity.ok(providers);
     }
 
     // Get all Service Providers
     @GetMapping("/all")
     public ResponseEntity<List<ServiceProvider>> getAllServiceProviders() {
         List<ServiceProvider> providers = serviceProviderRepo.findAll();
         return ResponseEntity.ok(providers);
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
   

    // Get Service Provider by Phone Number
@GetMapping("/serviceprovider")
public ResponseEntity<ServiceProvider> getProviderDetailsByPhoneNumber(@RequestParam String phoneNumber) {
    ServiceProvider provider = serviceProviderRepo.findByPhoneNumber(phoneNumber);
    if (provider != null) {
        return ResponseEntity.ok(provider);
    }
    return ResponseEntity.notFound().build();
}
@PutMapping("/serviceprovider")
public ResponseEntity<ServiceProvider> updateProviderByPhoneNumber(
    @RequestParam String phoneNumber, @RequestBody ServiceProvider provider
) {
    ServiceProvider existing = serviceProviderRepo.findByPhoneNumber(phoneNumber);
    if (existing == null) {
        return ResponseEntity.notFound().build();
    }

    existing.setEmail(provider.getEmail());
    existing.setPhoneNumber(provider.getPhoneNumber());
    existing.setLocation(provider.getLocation());
    existing.setServiceType(provider.getServiceType());
    existing.setProfilePicture(provider.getProfilePicture());
    existing.setBusinessName(provider.getBusinessName());
    // ... more fields you want to allow updating

    return ResponseEntity.ok(serviceProviderRepo.save(existing));
}

@PutMapping("/{service_provider_id}/details")
public ResponseEntity<?> updateProviderDetails(
    @PathVariable String service_provider_id,
    @RequestBody Map<String, Object> updates) {
    
    ServiceProvider provider = serviceProviderRepo.findByServiceProviderId(service_provider_id);
    if (provider == null) {
        return ResponseEntity.notFound().build();
    }

    // Partial updates - only modify fields that are provided
    if (updates.containsKey("email")) {
        provider.setEmail((String) updates.get("email"));
    }
    if (updates.containsKey("phoneNumber")) {
        provider.setPhoneNumber((String) updates.get("phoneNumber"));
    }
    if (updates.containsKey("location")) {
        try {
            // Safely handle the location update
            Object locationObj = updates.get("location");
            if (locationObj instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> locationMap = (Map<String, Object>) locationObj;
                
                // Get or create location object
                Location location = provider.getLocation() != null 
                    ? provider.getLocation() 
                    : new Location();
                
                // Update latitude if present
                if (locationMap.containsKey("latitude")) {
                    Object latObj = locationMap.get("latitude");
                    if (latObj instanceof Number) {
                        location.setLatitude(((Number) latObj).doubleValue());
                    }
                }
                
                // Update longitude if present
                if (locationMap.containsKey("longitude")) {
                    Object lngObj = locationMap.get("longitude");
                    if (lngObj instanceof Number) {
                        location.setLongitude(((Number) lngObj).doubleValue());
                    }
                }
                
                provider.setLocation(location);
            }
        } catch (Exception e) {
            // Log the error if needed
            return ResponseEntity.badRequest().body("Invalid location format");
        }
    }
    if (updates.containsKey("serviceType")) {
        provider.setServiceType((String) updates.get("serviceType"));
    }
    if (updates.containsKey("businessName")) {
        provider.setBusinessName((String) updates.get("businessName"));
    }
    // Add more fields as needed

    ServiceProvider updatedProvider = serviceProviderRepo.save(provider);
    return ResponseEntity.ok(updatedProvider);
}

//update availability by service provider id
// Assuming availability is a boolean field in the ServiceProvider model 
@PutMapping("/{service_provider_id}/availability")
public ResponseEntity<?> updateAvailability(
        @PathVariable String service_provider_id,
        @RequestBody Map<String, Boolean> body) {
    
    boolean isAvailable = body.getOrDefault("available", false);

    ServiceProvider provider = serviceProviderRepo.findByServiceProviderId(service_provider_id);
    if (provider == null) {
        return ResponseEntity.notFound().build();
    }

    provider.setAvailable(isAvailable);
    serviceProviderRepo.save(provider);

    return ResponseEntity.ok("Availability updated");
}

    @PutMapping("/by-id/{id}/availability")
public ResponseEntity<String> updateAvailability(
    @PathVariable Long id, 
    @RequestBody Map<String, Boolean> availability
) {
    Optional<ServiceProvider> providerOpt = serviceProviderRepo.findById(id);
    if (providerOpt.isEmpty()) {
        return ResponseEntity.notFound().build();
    }
    ServiceProvider provider = providerOpt.get();
    provider.setAvailability(availability); // Update the availability map
    serviceProviderRepo.save(provider); // Save the updated provider
    return ResponseEntity.ok("Availability updated successfully");
}

 @PostMapping("/logout")
    public ResponseEntity<String> logoutServiceProvider(HttpServletRequest request) {
        try {
            // Invalidate the session
            request.getSession().invalidate();
            return ResponseEntity.ok("Logout successful");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Internal Server Error during logout");
        }
    }
    
}

