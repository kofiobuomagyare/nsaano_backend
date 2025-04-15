package com.nsaano.app.backend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.nsaano.app.backend.Models.Appointment;
import com.nsaano.app.backend.Models.Appointment.AppointmentId;
import com.nsaano.app.backend.Models.User;
import com.nsaano.app.backend.Repo.AppointmentRepo;
import com.nsaano.app.backend.Repo.UserRepo;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {
    
    @Autowired
    private UserRepo UserRepo;
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
    
    @DeleteMapping("/cancel/{id}")
    public String cancelAppointment(@PathVariable AppointmentId id) {
        if (appointmentRepo.existsById(new Appointment.AppointmentId())) {
            appointmentRepo.deleteById(id);
            return "Appointment canceled successfully";
        }
        return "Appointment not found";
    }
    
    @GetMapping("/all")
    public List<Appointment> getAllAppointments() {
        return appointmentRepo.findAll();
    }
    @GetMapping("/findUserIdByPhoneAndPassword")
    public ResponseEntity<?> findUserIdByPhoneAndPassword(@RequestParam String phone, @RequestParam String password) {
        Optional<User> user = UserRepo.findByPhoneAndPassword(phone, password); // You must implement this
        if (user.isPresent()) {
            return ResponseEntity.ok().body(Map.of("user_id", user.get().getUser_id()));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "User not found"));
        }
    }
    

}