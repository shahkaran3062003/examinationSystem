package com.roima.examinationSystem.service.admin.dataImportManagement;


import com.roima.examinationSystem.exception.ExcelReaderException;
import com.roima.examinationSystem.exception.FetchException;
import com.roima.examinationSystem.exception.InvalidValueException;
import com.roima.examinationSystem.exception.ResourceNotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IDataImportService {

    void importStudentData(MultipartFile file, String action) throws ExcelReaderException, ResourceNotFoundException, InvalidValueException;
    void importMcqQuestions(MultipartFile file, String action) throws ExcelReaderException, ResourceNotFoundException, InvalidValueException;
    void importProgrammingQuestions(MultipartFile file, String action) throws ExcelReaderException, ResourceNotFoundException, InvalidValueException, FetchException;
}
