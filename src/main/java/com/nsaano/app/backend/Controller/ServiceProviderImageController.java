package com.nsaano.app.backend.Controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.*;
import java.io.IOException;

@RestController
@RequestMapping("/api/providers")
public class ServiceProviderImageController {
    
    private static final String IMAGE_DIRECTORY = "uploads/";

    @PostMapping("/{id}/upload-image")
    public String uploadImage(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        try {
            // Create a directory for each service provider using their ID
            Path providerDir = Paths.get(IMAGE_DIRECTORY + id);
            Files.createDirectories(providerDir);

            // Define the file path (including provider's ID as a folder)
            Path filePath = providerDir.resolve(file.getOriginalFilename());

            // Save the file
            Files.write(filePath, file.getBytes());

            return "Image uploaded successfully: " + filePath.toString();
        } catch (IOException e) {
            return "Error uploading image: " + e.getMessage();
        }
    }
}
