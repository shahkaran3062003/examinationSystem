package com.roima.examinationSystem.repository;

import com.roima.examinationSystem.model.Role;
import com.roima.examinationSystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User,Integer> {

    User findByEmail(String email);

    List<User> findByRole(Role role);
}
