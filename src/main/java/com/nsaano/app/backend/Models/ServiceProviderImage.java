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
    
    @Lob
    @Column(nullable = false, columnDefinition = "LONGBLOB")
    private byte[] imageData;
    
    private String caption;
    
    private String mimeType;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "upload_date", nullable = false)
    private Date uploadDate;
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public ServiceProvider getServiceProvider() { return serviceProvider; }
    public void setServiceProvider(ServiceProvider serviceProvider) { this.serviceProvider = serviceProvider; }
    public byte[] getImageData() { return imageData; }
    public void setImageData(byte[] imageData) { this.imageData = imageData; }
    public String getCaption() { return caption; }
    public void setCaption(String caption) { this.caption = caption; }
    public Date getUploadDate() { return uploadDate; }
    public void setUploadDate(Date uploadDate) { this.uploadDate = uploadDate; }
    public String getMimeType() { return mimeType; }
    public void setMimeType(String mimeType) { this.mimeType = mimeType; }
}