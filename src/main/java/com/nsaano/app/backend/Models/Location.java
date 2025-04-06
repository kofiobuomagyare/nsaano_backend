package com.nsaano.app.backend.Models;

import jakarta.persistence.Embeddable;

@Embeddable
public class Location {
    private Double latitude;
    private Double longitude;

    // Getters and Setters
    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
