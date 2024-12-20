package com.roima.examinationSystem.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class McqOptionsDto {
    private int id;
    private int option_number;
    private String text;
    private String image;
}
