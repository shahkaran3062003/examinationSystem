package com.roima.examinationSystem.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminExamMonitorDto {

    private int id;

    private LocalDateTime monitorTime;

    private String screenImage;

    private String userImage;

}
