package com.nsaano.app.backend.Controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.*;
import java.io.IOException;

@RestController
@RequestMapping("/api/providers")
public class ServiceProviderImageController {
    
    private static final String IMAGE_DIRECTORY = "uploads/";

    @PostMapping("/upload-image")
    public String uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            Path filePath = Paths.get(IMAGE_DIRECTORY + file.getOriginalFilename());
            Files.createDirectories(filePath.getParent());
            Files.write(filePath, file.getBytes());
            return "Image uploaded successfully: " + filePath.toString();
        } catch (IOException e) {
            return "Error uploading image: " + e.getMessage();
        }
    }
}
