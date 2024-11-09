package com.roima.examinationSystem.request;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddCollegeRequest {
    private String name;
    private String address;
    private String email;

    public AddCollegeRequest(String name, String email) {
        this.name=name;
        this.email = email;
        this.address = "";
    }

}
