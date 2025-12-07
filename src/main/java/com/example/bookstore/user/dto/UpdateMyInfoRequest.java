package com.example.bookstore.user.dto;

import jakarta.validation.constraints.Size;

public class UpdateMyInfoRequest {

    @Size(max = 100)
    private String name;

    @Size(max = 20)
    private String phoneNumber;

    private String address;

    @Size(max = 100)
    private String region;

    // "male", "female", "other"
    private String gender;

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public String getRegion() {
        return region;
    }

    public String getGender() {
        return gender;
    }
}
