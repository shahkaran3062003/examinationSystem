package com.roima.examinationSystem.repository;

import com.roima.examinationSystem.model.LoginLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface LoginLogRepository extends JpaRepository<LoginLog,Integer> {

    List<LoginLog> findAllByUserId(int userId);

}
