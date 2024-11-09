package com.roima.examinationSystem.service.college;

import com.roima.examinationSystem.exception.ResourceExistsException;
import com.roima.examinationSystem.exception.ResourceNotFoundException;
import com.roima.examinationSystem.model.College;
import com.roima.examinationSystem.request.AddCollegeRequest;
import com.roima.examinationSystem.request.UpdateCollegeRequest;

import java.util.List;

public interface ICollegeService {

    List<College> getAllColleges();

    College getCollegeById(int id) throws ResourceNotFoundException;

    College getCollegeByName(String name) throws ResourceNotFoundException;

    void addCollege(AddCollegeRequest college) throws ResourceExistsException;

    void updateCollege(UpdateCollegeRequest college, int id) throws ResourceExistsException, ResourceNotFoundException;

    void deleteCollegeById(int id) throws ResourceNotFoundException;


}
