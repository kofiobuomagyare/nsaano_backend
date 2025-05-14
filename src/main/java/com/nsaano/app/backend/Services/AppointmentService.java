package com.nsaano.app.backend.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nsaano.app.backend.Repo.AppointmentRepo;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepo appointmentRepo;

    public int getCompletedJobsCount(Long providerId) {
        return appointmentRepo.countCompletedJobsByProviderId(providerId);
    }
}
