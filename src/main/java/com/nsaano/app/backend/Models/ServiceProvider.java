package com.nsaano.app.backend.Models;

import jakarta.persistence.*;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "service_providers")
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
    
    @Embedded
    private Location location; // Using an embedded entity for latitude & longitude
    
    @Column
    private String profilePicture;

    @Column
    private String description; // Added field for provider description
    
    @Column
    private Double rating; // Rating for the provider
    
    @ElementCollection
    private List<String> services; // List of services offered
    
    @ElementCollection
    @CollectionTable(name = "provider_availability")
    private Map<String, Boolean> availability; // Service provider availability
    
    @Column
    private Double pricePerHour; // Pricing per hour for services
    
    @Column(unique = true)
    private String service_provider_id; // Unique provider ID

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

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public List<String> getServices() {
        return services;
    }

    public void setServices(List<String> services) {
        this.services = services;
    }

    public Map<String, Boolean> getAvailability() {
        return availability;
    }

    public void setAvailability(Map<String, Boolean> availability) {
        this.availability = availability;
    }

    public Double getPricePerHour() {
        return pricePerHour;
    }

    public void setPricePerHour(Double pricePerHour) {
        this.pricePerHour = pricePerHour;
    }

    public String getService_provider_id() {
        return service_provider_id;
    }

    public void setService_provider_id(String service_provider_id) {
        this.service_provider_id = service_provider_id;
    }

    // Method to generate the service_provider_id in the "nsaserv001" format
    public static String generateServiceProviderId(long id) {
        DecimalFormat df = new DecimalFormat("000");
        return "nsaserv" + df.format(id);
    }

    // Automatically generate service_provider_id before saving
    @PrePersist
    public void prePersist() {
        this.service_provider_id = generateServiceProviderId(this.id);
    }
}
