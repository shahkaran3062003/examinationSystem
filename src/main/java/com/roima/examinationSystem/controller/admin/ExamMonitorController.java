package com.roima.examinationSystem.controller.admin;


import com.roima.examinationSystem.exception.ResourceNotFoundException;
import com.roima.examinationSystem.response.ApiResponse;
import com.roima.examinationSystem.service.admin.examManagement.ExamManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.adminPrefix}/exam-monitor")
public class ExamMonitorController {

    private final ExamManagementService examManagementService;

    @GetMapping("/get/student/{studentId}/exam/{examId}")
    public ResponseEntity<ApiResponse> getStudentExamMonitorDetails(@PathVariable int studentId, @PathVariable int examId) {
        try{
            return ResponseEntity.ok(new ApiResponse("success", examManagementService.getStudentExamMonitorDetails(studentId,examId)));
        }catch (ResourceNotFoundException e) {
            return ResponseEntity.badRequest().body(new ApiResponse("error", e.getMessage()));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteById(@PathVariable int id){
        try{
            examManagementService.deleteExamMonitorById(id);
            return ResponseEntity.ok(new ApiResponse("success","Student exam monitor details deleted successfully" ));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.badRequest().body(new ApiResponse("error", e.getMessage()));
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body(new ApiResponse("error", e.getMessage()));
        }
    }

    @DeleteMapping("/delete/student/{studentId}/exam/{examId}")
    public ResponseEntity<ApiResponse> deleteStudentExamMonitorDetails(@PathVariable int studentId,@PathVariable int examId) {
        try{
            examManagementService.deleteExamMonitorByStudentIdAndExamId(studentId,examId);
            return ResponseEntity.ok(new ApiResponse("success","Student exam monitor details deleted successfully" ));
        }catch (ResourceNotFoundException | IOException e) {
            return ResponseEntity.badRequest().body(new ApiResponse("error", e.getMessage()));
        }
    }

    @DeleteMapping("/delete/exam/{examId}")
    public ResponseEntity<ApiResponse> deleteStudentExamMonitorDetailsByExamId(@PathVariable int examId) {
        try{
            examManagementService.deleteExamMonitorByExamId(examId);
            return ResponseEntity.ok(new ApiResponse("success","Student exam monitor details deleted successfully" ));
        }catch (ResourceNotFoundException | IOException e) {
            return ResponseEntity.badRequest().body(new ApiResponse("error", e.getMessage()));
        }
    }
}
