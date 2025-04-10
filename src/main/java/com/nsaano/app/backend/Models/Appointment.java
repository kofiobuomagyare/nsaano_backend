package com.nsaano.app.backend.Models;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@IdClass(Appointment.AppointmentId.class)  // Define composite key inside the class
public class Appointment {

    @Id
    @Column(nullable = false)
    private String user_id;  // Composite Key - user_id

    @Id
    @Column(nullable = false)
    private String service_provider_id;  // Composite Key - service_provider_id

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", insertable = false, updatable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "service_provider_id", referencedColumnName = "service_provider_id", insertable = false, updatable = false)
    private ServiceProvider serviceProvider;

    @Column(nullable = false)
    private Date appointmentDate;

    @Column(nullable = false)
    private String status;

    // Default constructor
    public Appointment() {}

    // Constructor for setting the composite key fields
    public Appointment(String user_id, String service_provider_id, User user, ServiceProvider serviceProvider, Date appointmentDate, String status) {
        this.user_id = user_id;
        this.service_provider_id = service_provider_id;
        this.user = user;
        this.serviceProvider = serviceProvider;
        this.appointmentDate = appointmentDate;
        this.status = status;
    }

    // Getters and Setters
    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getService_provider_id() {
        return service_provider_id;
    }

    public void setService_provider_id(String service_provider_id) {
        this.service_provider_id = service_provider_id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ServiceProvider getServiceProvider() {
        return serviceProvider;
    }

    public void setServiceProvider(ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    public Date getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(Date appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Define the composite key inline using @IdClass
    public static class AppointmentId implements Serializable {
        private String user_id;
        private String service_provider_id;

        // No-argument constructor required by JPA
        public AppointmentId() {}

        // Constructor for setting the composite key fields
        public AppointmentId(String user_id, String service_provider_id) {
            this.user_id = user_id;
            this.service_provider_id = service_provider_id;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            AppointmentId that = (AppointmentId) o;
            return Objects.equals(user_id, that.user_id) &&
                   Objects.equals(service_provider_id, that.service_provider_id);
        }

        @Override
        public int hashCode() {
            return Objects.hash(user_id, service_provider_id);
        }
    }
}
