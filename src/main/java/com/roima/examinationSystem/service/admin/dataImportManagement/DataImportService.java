package com.roima.examinationSystem.service.admin.dataImportManagement;

import com.roima.examinationSystem.exception.ExcelReaderException;
import com.roima.examinationSystem.exception.FetchException;
import com.roima.examinationSystem.exception.InvalidValueException;
import com.roima.examinationSystem.exception.ResourceNotFoundException;
import com.roima.examinationSystem.model.*;
import com.roima.examinationSystem.repository.*;
import com.roima.examinationSystem.utils.excel.ExcelUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Service
@RequiredArgsConstructor
public class DataImportService implements IDataImportService{

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final McqQuestionsRepository mcqQuestionsRepository;
    private final McqOptionsRepository mcqOptionsRepository;
    private final ProgrammingQuestionsRepository programmingQuestionsRepository;
    private final ProgrammingTestCaseRepository programmingTestCaseRepository;
    private final ExcelUtils excelUtils;


    private boolean isValidAction(String action){
        return action.equals("TEST") || action.equals("SUBMIT");
    }


    @Override
    public void importStudentData(MultipartFile file, String action) throws ExcelReaderException, ResourceNotFoundException, InvalidValueException {
        if(!isValidAction(action)){
            throw new InvalidValueException("Invalid action!");
        }

        List<Student> studentList = excelUtils.getStudentData(file);
        int len = studentList.size();
        for(int i=0;i<len;i++){

            if( userRepository.findByEmail(studentList.get(i).getUser().getEmail()).isPresent()){
                System.out.println(studentList.get(i).getUser().getEmail());
                throw new ExcelReaderException("User with same email already exists!");
            }

            for(int j=i+1;j<len;j++){
                if(studentList.get(i).getUser().getEmail().equals(studentList.get(j).getUser().getEmail())){
                    throw new ExcelReaderException("Excel file contains duplicate student emails!");
                }
            }
        }

        if(action.equals("SUBMIT")) {
            studentRepository.saveAll(studentList);
        }

    }

    @Override
    public void importMcqQuestions(MultipartFile file, String action) throws ExcelReaderException, ResourceNotFoundException, InvalidValueException {

        if(!isValidAction(action)){
            throw new InvalidValueException("Invalid action!");
        }

        List<McqQuestions> mcqQuestionsList = excelUtils.getMcqQuestions(file);

        if(action.equals("SUBMIT")) {
            for(McqQuestions mcqQuestions : mcqQuestionsList){

                mcqQuestionsRepository.save(mcqQuestions);
                for(McqOptions options: mcqQuestions.getMcqOptions()){
                    options.setMcqQuestions(mcqQuestions);
                    mcqOptionsRepository.save(options);
                }
//                mcqQuestionsRepository.save(mcqQuestions);
            }
        }


    }

    @Override
    public void importProgrammingQuestions(MultipartFile file, String action) throws ExcelReaderException, ResourceNotFoundException, InvalidValueException, FetchException {
        if(!isValidAction(action)){
            throw new InvalidValueException("Invalid action!");
        }

        List<ProgrammingQuestions> programmingQuestionsList = excelUtils.getProgrammingQuestions(file);

        if(action.equals("SUBMIT")) {
            for(ProgrammingQuestions programmingQuestions : programmingQuestionsList){
                programmingQuestionsRepository.save(programmingQuestions);
                for(ProgrammingTestCase testCase: programmingQuestions.getProgrammingTestCase()){
                    testCase.setProgrammingQuestions(programmingQuestions);
                    programmingTestCaseRepository.save(testCase);
                }
            }
        }
    }
}
