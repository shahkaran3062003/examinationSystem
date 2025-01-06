package com.roima.examinationSystem.controller.admin;


import com.roima.examinationSystem.exception.ExcelReaderException;
import com.roima.examinationSystem.exception.FetchException;
import com.roima.examinationSystem.exception.InvalidValueException;
import com.roima.examinationSystem.exception.ResourceNotFoundException;
import com.roima.examinationSystem.response.ApiResponse;
import com.roima.examinationSystem.service.admin.dataImportManagement.DataImportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.adminPrefix}/data-import")
public class DataImportController {

    private final DataImportService dataImportService;


    @PostMapping("/student")
    public ResponseEntity<ApiResponse> importStudentData(
            @RequestPart(value = "file") MultipartFile file,
            @RequestPart(value = "action") String action
            ) {

        try{

            dataImportService.importStudentData(file,action);
            return ResponseEntity.ok().body(new ApiResponse("success", action+" action completed successfully"));

        } catch (ExcelReaderException | ResourceNotFoundException | InvalidValueException e) {
            return ResponseEntity.badRequest().body(new ApiResponse("error", e.getMessage()));
        }
    }

    @PostMapping("/mcq-questions")
    public ResponseEntity<ApiResponse> importMcqQuestions(
        @RequestPart(value = "file") MultipartFile file,
        @RequestPart(value = "action") String action
    ){
        try{
            dataImportService.importMcqQuestions(file,action);
            return ResponseEntity.ok().body(new ApiResponse("success", action+" action completed successfully"));
        }catch (ExcelReaderException | InvalidValueException | ResourceNotFoundException e){
            return ResponseEntity.badRequest().body(new ApiResponse("error", e.getMessage()));
        }
    }

    @PostMapping("/programming-questions")
    public ResponseEntity<ApiResponse> importProgrammingQuestions(
        @RequestPart(value = "file") MultipartFile file,
        @RequestPart(value = "action") String action
    ){
        try{
            dataImportService.importProgrammingQuestions(file,action);
            return ResponseEntity.ok().body(new ApiResponse("success", action+" action completed successfully"));
        }catch (ExcelReaderException | InvalidValueException | ResourceNotFoundException | FetchException e){
            System.out.println(e.getClass().toString());
            return ResponseEntity.badRequest().body(new ApiResponse("error", e.getMessage()));
        }
    }
}
