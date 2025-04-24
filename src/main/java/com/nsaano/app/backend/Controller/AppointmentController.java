package com.nsaano.app.backend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.nsaano.app.backend.Models.Appointment;
import com.nsaano.app.backend.Models.Appointment.AppointmentId;
import com.nsaano.app.backend.Repo.AppointmentRepo;
import java.util.Date;
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
    
}