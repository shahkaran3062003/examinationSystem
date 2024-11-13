package com.roima.examinationSystem.repository;

import com.roima.examinationSystem.model.Role;
import com.roima.examinationSystem.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

    User findByEmail(String email);

    List<User> findByRole(Role role);

    boolean existsByEmail(@NotNull @NotBlank @Email String email);
}
