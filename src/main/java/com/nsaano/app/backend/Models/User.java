package com.nsaano.app.backend.Models;

import jakarta.persistence.*;
import java.text.DecimalFormat;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String first_name;

    @Column
    private String last_name;

    @Column
    private int age;

    @Column
    private String gender;

    @Column
    private String email;

    @Column
    private String password;

    @Column(name = "phone_number")
    private String phoneNumber;


    @Column
    private String address;

    @Column
    private String bio;

    @Column(name = "profile_picture", columnDefinition = "TEXT")
    private String profilePicture;



    @Column
    private String role;

    @Column(unique = true)
    private String user_id; // Added user_id column
    @Column(name = "is_available")
    private Boolean isAvailable = false; // Initialize with default value

    // Getters and setters

    public long getId() {
        return id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public int getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getPhone_number() {
        return phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public String getBio() {
        return bio;
    }

    public String getProfile_picture() {
        return profilePicture;
    }

    public String getRole() {
        return role;
    }

    public String getUser_id() {
        return user_id; // Getter for user_id
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPhone_number(String phone_number) {
        this.phoneNumber = phone_number;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public void setProfile_picture(String profile_picture) {
        this.profilePicture = profile_picture;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setUser_id(String user_id) {  // Setter for user_id
        this.user_id = user_id;
    }

    // Method to generate the user_id in the "nsa001" format
    public static String generateUserId(long id) {
        DecimalFormat df = new DecimalFormat("000");
        return "nsa" + df.format(id);
    }

    // This method will be called before persisting the entity (before saving)
    @PostPersist
    public void postPersist() {
        this.user_id = generateUserId(this.id);
    }
    
public boolean isAvailable() {
    return isAvailable;
}
public void setAvailable(boolean available) {
    isAvailable = available;
}
}

