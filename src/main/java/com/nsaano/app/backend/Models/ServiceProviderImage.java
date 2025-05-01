package com.nsaano.app.backend.Models;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "service_provider_images")
public class ServiceProviderImage {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "service_provider_id", referencedColumnName = "id")
    private ServiceProvider serviceProvider;
    
    @Column(nullable = false)
    private String imagePath;
    
    private String caption;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "upload_date", nullable = false)
    private Date uploadDate;
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public ServiceProvider getServiceProvider() { return serviceProvider; }
    public void setServiceProvider(ServiceProvider serviceProvider) { this.serviceProvider = serviceProvider; }
    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }
    public String getCaption() { return caption; }
    public void setCaption(String caption) { this.caption = caption; }
    public Date getUploadDate() { return uploadDate; }
    public void setUploadDate(Date uploadDate) { this.uploadDate = uploadDate; }
}