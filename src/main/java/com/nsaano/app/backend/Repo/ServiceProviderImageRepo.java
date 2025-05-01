package com.nsaano.app.backend.Repo;

import com.nsaano.app.backend.Models.ServiceProvider;
import com.nsaano.app.backend.Models.ServiceProviderImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ServiceProviderImageRepo extends JpaRepository<ServiceProviderImage, Long> {
    List<ServiceProviderImage> findByServiceProvider(ServiceProvider serviceProvider);
    long countByServiceProvider(ServiceProvider serviceProvider);
}