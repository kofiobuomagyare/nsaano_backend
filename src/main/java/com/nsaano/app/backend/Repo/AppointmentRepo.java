package com.nsaano.app.backend.Repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nsaano.app.backend.Models.Appointment;

public interface AppointmentRepo extends JpaRepository<Appointment, Appointment.AppointmentId> {

    @Query("SELECT COUNT(a) FROM Appointment a WHERE a.serviceProvider.id = :providerId AND a.status = 'completed'")
int countCompletedJobsByProviderId(@Param("providerId") Long providerId);

    List<Appointment> findByUserId(Long userId);
    List<Appointment> findByServiceProviderId(Long serviceProviderId);

}