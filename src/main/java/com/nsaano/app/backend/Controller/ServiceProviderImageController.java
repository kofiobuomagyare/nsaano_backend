package com.nsaano.app.backend.Controller;

import com.nsaano.app.backend.Models.ServiceProvider;
import com.nsaano.app.backend.Models.ServiceProviderImage;
import com.nsaano.app.backend.Repo.ServiceProviderRepo;
import com.nsaano.app.backend.Repo.ServiceProviderImageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/api/providers")
public class ServiceProviderImageController {
    
    private static final int MAX_IMAGES = 10;
    private static final Set<String> ALLOWED_EXTENSIONS = Set.of(".jpg", ".jpeg", ".png");
    private static final Map<String, String> MIME_TYPES = Map.of(
        ".jpg", "image/jpeg",
        ".jpeg", "image/jpeg",
        ".png", "image/png"
    );

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

            // Count existing images
            long imageCount = serviceProviderImageRepo.countByServiceProvider(provider);
            if (imageCount >= MAX_IMAGES) {
                response.put("status", "error");
                response.put("message", "Maximum image limit (" + MAX_IMAGES + ") reached");
                return response;
            }

            // Save image metadata to database
            ServiceProviderImage imageEntity = new ServiceProviderImage();
            imageEntity.setServiceProvider(provider);
            imageEntity.setImageData(file.getBytes());
            imageEntity.setCaption(caption != null ? caption : "");
            imageEntity.setMimeType(MIME_TYPES.get(fileExtension));
            imageEntity.setUploadDate(new Date());
            serviceProviderImageRepo.save(imageEntity);

            response.put("status", "success");
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
                imageInfo.put("id", image.getId());
                imageInfo.put("caption", image.getCaption());
                imageInfo.put("uploadDate", image.getUploadDate());
                imageInfo.put("mimeType", image.getMimeType());
                images.add(imageInfo);
            }
        }
        
        return images;
    }

    @GetMapping(value = "/image/{imageId}", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getImage(@PathVariable Long imageId) {
        Optional<ServiceProviderImage> imageOpt = serviceProviderImageRepo.findById(imageId);
        if (imageOpt.isPresent()) {
            return imageOpt.get().getImageData();
        }
        return new byte[0];
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

        // Delete record from database
        serviceProviderImageRepo.delete(imageOpt.get());
        
        response.put("status", "success");
        response.put("message", "Image deleted successfully");
        return response;
    }
}