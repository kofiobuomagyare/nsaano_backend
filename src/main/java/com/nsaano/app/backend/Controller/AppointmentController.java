package com.nsaano.app.backend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.nsaano.app.backend.Models.Appointment;
import com.nsaano.app.backend.Repo.AppointmentRepo;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {
    
    @Autowired
    private AppointmentRepo appointmentRepo;
    
    @PostMapping("/create")
    public String createAppointment(@RequestBody Appointment appointment) {
        appointmentRepo.save(appointment);
        return "Appointment created successfully";
    }
    
    @PutMapping("/update/{id}")
    public String updateAppointment(@PathVariable Long id, @RequestBody Appointment updatedAppointment) {
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
    public String cancelAppointment(@PathVariable Long id) {
        if (appointmentRepo.existsById(id)) {
            appointmentRepo.deleteById(id);
            return "Appointment canceled successfully";
        }
        return "Appointment not found";
    }
    
    @GetMapping("/all")
    public List<Appointment> getAllAppointments() {
        return appointmentRepo.findAll();
    }
}