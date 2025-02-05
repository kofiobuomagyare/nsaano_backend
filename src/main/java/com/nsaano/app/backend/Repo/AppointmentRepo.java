package com.nsaano.app.backend.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import com.nsaano.app.backend.Models.Appointment;

public interface AppointmentRepo extends JpaRepository<Appointment, Long> {
}