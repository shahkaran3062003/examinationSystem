package com.roima.examinationSystem.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddCollegeRequest {

    @NotNull
    @NotBlank
    @Size(min = 3, max = 30,message = "Name must be between 3 and 30 characters")
    private String name;


    private String address;

    @NotNull
    @Email(message = "Invalid email address")
    private String email;

    public AddCollegeRequest(String name, String email) {
        this.name=name;
        this.email = email;
        this.address = "";
    }

}
