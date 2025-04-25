package com.nsaano.app.backend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.nsaano.app.backend.Models.Appointment;
import com.nsaano.app.backend.Models.Appointment.AppointmentId;
import com.nsaano.app.backend.Repo.AppointmentRepo;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {
    
  
    @Autowired
    private AppointmentRepo appointmentRepo;
    
    @PostMapping("/create")
    public ResponseEntity<?> createAppointment(@RequestBody Appointment appointment) {
        System.out.println("Creating appointment: " + appointment);
    
        // Check if the appointment date is in the past
        if (appointment.getAppointmentDate().before(new Date())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body("{\"message\": \"Appointment date cannot be in the past.\"}");
        }
    
        try {
            appointmentRepo.save(appointment);
            return ResponseEntity.status(HttpStatus.CREATED)
                                 .body("{\"message\": \"Appointment created successfully\"}");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("{\"message\": \"Failed to create appointment: " + e.getMessage() + "\"}");
        }
    }
    

    @PutMapping("/update/{id}")
    public String updateAppointment(@PathVariable AppointmentId id, @RequestBody Appointment updatedAppointment) {
        Optional<Appointment> appointment = appointmentRepo.findById(id);
        if (appointment.isPresent()) {
            Appointment existingAppointment = appointment.get();
            existingAppointment.setAppointmentDate(updatedAppointment.getAppointmentDate());
            existingAppointment.setStatus(updatedAppointment.getStatus());
            appointmentRepo.save(existingAppointment);
            return "Appointment updated successfully";
        }
        return "Appointment not found";
    }
    
    @DeleteMapping("/cancel")
public ResponseEntity<String> cancelAppointment(
    @RequestParam String user_id, 
    @RequestParam String service_provider_id
) {
    Appointment.AppointmentId id = new Appointment.AppointmentId(user_id, service_provider_id);
    if (appointmentRepo.existsById(id)) {
        appointmentRepo.deleteById(id);
        return ResponseEntity.ok("Appointment canceled successfully");
    }
    return ResponseEntity.status(404).body("Appointment not found");
}

   
    @PutMapping("/update/status")
    public ResponseEntity<String> updateStatus(
        @RequestParam String user_id, 
        @RequestParam String service_provider_id,
        @RequestBody Map<String, String> body
    ) {
        Appointment.AppointmentId id = new Appointment.AppointmentId(user_id, service_provider_id);
        Optional<Appointment> appointment = appointmentRepo.findById(id);
        if (appointment.isPresent()) {
            Appointment a = appointment.get();
            a.setStatus(body.get("status"));
            appointmentRepo.save(a);
            return ResponseEntity.ok("Status updated");
        }
        return ResponseEntity.status(404).body("Appointment not found");
    }
    
    @GetMapping("/{service_provider_id}/appointments")
    public ResponseEntity<?> getAppointmentsByProviderId(@PathVariable String service_provider_id) {
        Long numericId = Long.parseLong(service_provider_id.replace("nsaserv", ""));
        List<Appointment> appointments = appointmentRepo.findByServiceProviderId(numericId);

    if (appointments.isEmpty()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                             .body("{\"message\": \"No appointments found for this service provider.\"}");
    }

    return ResponseEntity.ok(appointments);
}

@GetMapping("/users/{user_id}/appointments")
public ResponseEntity<?> getAppointmentsByUserId(@PathVariable String user_id) {
    try {
        // Remove "nsa" prefix (assuming format is "nsa123")
        String numericalPart = user_id.replace("nsa", ""); 
        Long userId = Long.parseLong(numericalPart);
        
        List<Appointment> appointments = appointmentRepo.findByUserId(userId);
        
        if (appointments.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("{\"message\": \"No appointments found for this user.\"}");
        }
        
        return ResponseEntity.ok(appointments);
    } catch (NumberFormatException e) {
        return ResponseEntity.badRequest()
            .body("{\"message\": \"Invalid user ID format. Expected 'nsa123'.\"}");
    } catch (Exception e) {
        return ResponseEntity.internalServerError()
            .body("{\"message\": \"Error: " + e.getMessage() + "\"}");
    }
}
@PutMapping("/{service_provider_id}/appointments/{user_id}/status")
public ResponseEntity<?> updateAppointmentStatus(
        @PathVariable String service_provider_id, 
        @PathVariable String user_id, // user_id should be part of the URL path
        @RequestBody Map<String, String> body) {

    // Create a composite ID using user_id and service_provider_id
    Appointment.AppointmentId id = new Appointment.AppointmentId(user_id, service_provider_id);

    // Fetch the appointment based on the composite key
    Optional<Appointment> appointmentOpt = appointmentRepo.findById(id);

    if (!appointmentOpt.isPresent()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                             .body("{\"message\": \"Appointment not found.\"}");
    }

    Appointment appointment = appointmentOpt.get();

    // Check if the appointment belongs to the provided service provider
    if (!appointment.getService_provider_id().equals(service_provider_id)) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                             .body("{\"message\": \"You do not have permission to update this appointment.\"}");
    }

    // Update the appointment status
    String newStatus = body.get("status");
    if (newStatus == null || newStatus.isEmpty()) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body("{\"message\": \"Status must be provided.\"}");
    }

    appointment.setStatus(newStatus);
    appointmentRepo.save(appointment);

    return ResponseEntity.ok("{\"message\": \"Appointment status updated successfully.\"}");
}

}