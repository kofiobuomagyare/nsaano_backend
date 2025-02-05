package com.nsaano.app.backend.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import com.nsaano.app.backend.Models.ServiceProvider;

public interface ServiceProviderRepo extends JpaRepository<ServiceProvider, Long> {
    ServiceProvider findByEmail(String email);
}
