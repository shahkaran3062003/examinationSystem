package com.roima.examinationSystem.service.mcqOptions;

import com.roima.examinationSystem.exception.ResourceNotFoundException;
import com.roima.examinationSystem.model.McqOptions;

public interface IMcqOptionsService {

    McqOptions addMcqOptions(int option_number, String text);

    void deleteMcqOptions(int id) throws ResourceNotFoundException;

    void updateMcqOptions(int option_number,String text, int id) throws ResourceNotFoundException;

}
