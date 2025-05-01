package com.nsaano.app.backend.Controller;

import com.nsaano.app.backend.Models.ServiceProvider;
import com.nsaano.app.backend.Models.ServiceProviderImage;
import com.nsaano.app.backend.Repo.ServiceProviderRepo;
import com.nsaano.app.backend.Repo.ServiceProviderImageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.nio.file.*;
import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/api/providers")
public class ServiceProviderImageController {
    
    private static final String IMAGE_DIRECTORY = "uploads/service_images/";
    private static final int MAX_IMAGES = 10;
    private static final Set<String> ALLOWED_EXTENSIONS = Set.of(".jpg", ".jpeg", ".png");

    @Autowired
    private ServiceProviderRepo serviceProviderRepo;

    @Autowired
    private ServiceProviderImageRepo serviceProviderImageRepo;

    @PostMapping("/{service_provider_id}/upload-service-image")
    public Map<String, Object> uploadServiceImage(
            @PathVariable String service_provider_id,
            @RequestParam("file") MultipartFile file,
            @RequestParam(required = false) String caption) {
        
        Map<String, Object> response = new HashMap<>();
        
        ServiceProvider provider = serviceProviderRepo.findByServiceProviderId(service_provider_id);
        if (provider == null) {
            response.put("status", "error");
            response.put("message", "Service provider not found");
            return response;
        }

        try {
            if (file.isEmpty()) {
                response.put("status", "error");
                response.put("message", "File is empty");
                return response;
            }

            String originalFilename = Objects.requireNonNull(file.getOriginalFilename());
            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf('.')).toLowerCase();
            
            if (!ALLOWED_EXTENSIONS.contains(fileExtension)) {
                response.put("status", "error");
                response.put("message", "Only JPG, JPEG, and PNG files are allowed");
                return response;
            }

            Path providerDir = Paths.get(IMAGE_DIRECTORY + service_provider_id);
            Files.createDirectories(providerDir);

            // Count existing images using the service provider entity
            long imageCount = serviceProviderImageRepo.countByServiceProvider(provider);
            if (imageCount >= MAX_IMAGES) {
                response.put("status", "error");
                response.put("message", "Maximum image limit (" + MAX_IMAGES + ") reached");
                return response;
            }

            String uniqueFilename = System.currentTimeMillis() + "_" + UUID.randomUUID().toString().substring(0, 8) + fileExtension;
            Path filePath = providerDir.resolve(uniqueFilename);

            BufferedImage image = ImageIO.read(file.getInputStream());
            if (image == null) {
                response.put("status", "error");
                response.put("message", "Invalid image file");
                return response;
            }

            String formatName = fileExtension.equals(".png") ? "png" : "jpg";
            ImageIO.write(image, formatName, filePath.toFile());

            // Save image metadata to database
            ServiceProviderImage imageEntity = new ServiceProviderImage();
            imageEntity.setServiceProvider(provider); // Set the relationship
            imageEntity.setImagePath("/service-images/" + service_provider_id + "/" + uniqueFilename);
            imageEntity.setCaption(caption != null ? caption : "");
            imageEntity.setUploadDate(new Date());
            serviceProviderImageRepo.save(imageEntity);

            response.put("status", "success");
            response.put("imageUrl", imageEntity.getImagePath());
            response.put("imageId", imageEntity.getId());
            response.put("caption", imageEntity.getCaption());
            return response;

        } catch (IOException e) {
            response.put("status", "error");
            response.put("message", "Error processing image: " + e.getMessage());
            return response;
        }
    }

    @GetMapping("/{service_provider_id}/service-images")
    public List<Map<String, Object>> getServiceImages(@PathVariable String service_provider_id) {
        List<Map<String, Object>> images = new ArrayList<>();
        
        ServiceProvider provider = serviceProviderRepo.findByServiceProviderId(service_provider_id);
        if (provider != null) {
            List<ServiceProviderImage> dbImages = serviceProviderImageRepo.findByServiceProvider(provider);
            
            for (ServiceProviderImage image : dbImages) {
                Map<String, Object> imageInfo = new HashMap<>();
                imageInfo.put("url", image.getImagePath());
                imageInfo.put("id", image.getId());
                imageInfo.put("caption", image.getCaption());
                imageInfo.put("uploadDate", image.getUploadDate());
                images.add(imageInfo);
            }
        }
        
        return images;
    }

    @DeleteMapping("/{service_provider_id}/delete-service-image/{imageId}")
    public Map<String, String> deleteServiceImage(
            @PathVariable String service_provider_id,
            @PathVariable Long imageId) {
        
        Map<String, String> response = new HashMap<>();
        
        Optional<ServiceProviderImage> imageOpt = serviceProviderImageRepo.findById(imageId);
        if (!imageOpt.isPresent()) {
            response.put("status", "error");
            response.put("message", "Image not found");
            return response;
        }

        ServiceProviderImage image = imageOpt.get();
        try {
            // Delete file from storage
            Path imagePath = Paths.get("." + image.getImagePath());
            if (Files.exists(imagePath)) {
                Files.delete(imagePath);
            }
            
            // Delete record from database
            serviceProviderImageRepo.delete(image);
            
            response.put("status", "success");
            response.put("message", "Image deleted successfully");
        } catch (IOException e) {
            response.put("status", "error");
            response.put("message", "Error deleting image: " + e.getMessage());
        }
        
        return response;
    }
}