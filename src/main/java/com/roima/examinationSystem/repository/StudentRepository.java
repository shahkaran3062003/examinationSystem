package com.roima.examinationSystem.repository;

import com.roima.examinationSystem.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface StudentRepository extends JpaRepository<Student,Integer> {

    Optional<Student> findByUserEmail(String email);
    boolean existsByUserEmail(String email);

    List<Student> findAllByCollegeId(int id);
}
