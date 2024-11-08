package com.roima.examinationSystem.request;

import com.roima.examinationSystem.model.Role;
import lombok.*;


@Data
@NoArgsConstructor
public class AddUserRequest {
    private String username;
    private String email;
    private String password;
    private String role;

    public AddUserRequest(String username, String email, String password, String role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }
}
