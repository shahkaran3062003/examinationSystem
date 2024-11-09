package com.roima.examinationSystem.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCollegeRequest {
    private String name;
    private String address;
    private String email;

}
