package com.roima.examinationSystem.service.college;

import com.roima.examinationSystem.dto.CollegeDto;
import com.roima.examinationSystem.exception.CollegeExistsException;
import com.roima.examinationSystem.exception.CollegeNotFoundException;
import com.roima.examinationSystem.exception.InvalidParametersException;
import com.roima.examinationSystem.model.College;
import com.roima.examinationSystem.request.AddCollegeRequest;
import com.roima.examinationSystem.request.UpdateCollegeRequest;

import java.util.List;

public interface ICollegeService {

    List<College> getAllColleges();

    College getCollegeById(int id) throws CollegeNotFoundException;

    College getCollegeByName(String name) throws CollegeNotFoundException;

    void addCollege(AddCollegeRequest college) throws CollegeExistsException, InvalidParametersException;

    void updateCollege(UpdateCollegeRequest college, int id) throws CollegeExistsException, CollegeNotFoundException, InvalidParametersException;

    void deleteCollegeById(int id) throws CollegeNotFoundException;


}
