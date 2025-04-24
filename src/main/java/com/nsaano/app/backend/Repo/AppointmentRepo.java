package com.nsaano.app.backend.Repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.nsaano.app.backend.Models.Appointment;

public interface AppointmentRepo extends JpaRepository<Appointment, Appointment.AppointmentId> {

    List<Appointment> findByUserId(Long userId);
    List<Appointment> findByServiceProviderId(String serviceProviderId);

}