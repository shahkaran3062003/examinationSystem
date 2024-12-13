package com.roima.examinationSystem.repository;

import com.roima.examinationSystem.exception.ResourceNotFoundException;
import com.roima.examinationSystem.model.Role;
import com.roima.examinationSystem.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

    Optional<User> findByEmail(String email);

    List<User> findByRole(Role role);

    boolean existsByEmail(@NotNull @NotBlank @Email String email);
}
