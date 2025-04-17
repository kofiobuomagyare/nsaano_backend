package com.nsaano.app.backend.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import com.nsaano.app.backend.Models.ServiceProvider;

@Repository
public interface ServiceProviderRepo extends JpaRepository<ServiceProvider, Long> {

    // Find a service provider by email
    ServiceProvider findByEmail(String email);

    // Find nearby service providers using latitude, longitude, and radius (in km)
    @Query("SELECT sp FROM ServiceProvider sp WHERE " +
           "6371 * acos(cos(radians(:latitude)) * cos(radians(sp.location.latitude)) * " +
           "cos(radians(sp.location.longitude) - radians(:longitude)) + " +
           "sin(radians(:latitude)) * sin(radians(sp.location.latitude))) <= :radius")
    List<ServiceProvider> findNearbyProviders(
        @Param("latitude") double latitude,
        @Param("longitude") double longitude,
        @Param("radius") double radius
    );

    ServiceProvider findByPhoneNumber(String phoneNumber);
    List<ServiceProvider> findByServiceTypeIn(List<String> serviceTypes);
}
