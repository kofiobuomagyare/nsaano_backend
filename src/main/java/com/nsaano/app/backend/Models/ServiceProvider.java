package com.nsaano.app.backend.Models;

import jakarta.persistence.*;
import java.text.DecimalFormat;

@Entity
public class ServiceProvider {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    
    @Column(nullable = false)
    private String businessName;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(nullable = false)
    private String phoneNumber;
    
    @Column(nullable = false)
    private String password;
    
    @Column(nullable = false)
    private String serviceType;
    
    @Column(nullable = false)
    private String location;
    
    @Column
    private String profilePicture;
    
    @Column(nullable = false)
    private String role = "SERVICE_PROVIDER";
    
    @Column(unique = true)
    private String service_provider_id; // Added service_provider_id column
    
    // Getters and Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getService_provider_id() {
        return service_provider_id; // Getter for service_provider_id
    }

    public void setService_provider_id(String service_provider_id) {
        this.service_provider_id = service_provider_id;
    }

    // Method to generate the service_provider_id in the "nsaserv001" format
    public static String generateServiceProviderId(long id) {
        DecimalFormat df = new DecimalFormat("000");
        return "nsaserv" + df.format(id);
    }

    // This method will be called before persisting the entity (before saving)
    @PrePersist
    public void prePersist() {
        // Ensure that the service_provider_id is generated before saving
        this.service_provider_id = generateServiceProviderId(this.id);
    }
}
