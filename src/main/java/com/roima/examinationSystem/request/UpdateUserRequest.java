package com.roima.examinationSystem.request;

import com.roima.examinationSystem.model.Role;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdateUserRequest {
    private String username;
    private String email;
    private String password;
    private String role;

    public UpdateUserRequest(String username, String email, String password, String role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }
}
