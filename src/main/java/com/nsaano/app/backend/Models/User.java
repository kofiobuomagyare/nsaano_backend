package com.nsaano.app.backend.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.*;

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
    @Column
    private String phone_number;
    @Column
    private String address;
    @Column
    private String bio;
    @Column
    private String profile_picture;
    @Column
    private String role;
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
        return phone_number;
    }
    public String getAddress() {
        return address;
    }
    public String getBio() {
        return bio;
    }
    public String getProfile_picture() {
        return profile_picture;
    }
    public String getRole() {
        return role;
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
        this.phone_number = phone_number;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public void setBio(String bio) {
        this.bio = bio;
    }
    public void setProfile_picture(String profile_picture) {
        this.profile_picture = profile_picture;
    }
    public void setRole(String role) {
        this.role = role;
    }

}
