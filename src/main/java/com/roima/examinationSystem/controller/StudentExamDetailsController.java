package com.roima.examinationSystem.controller;


import com.roima.examinationSystem.exception.ResourceNotFoundException;
import com.roima.examinationSystem.response.ApiResponse;
import com.roima.examinationSystem.service.studentExamDetails.StudentExamDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/student-exam-details")
public class StudentExamDetailsController {

    private final StudentExamDetailsService studentExamDetailsService;

    @GetMapping("/get/all/student/{id}")
    public ResponseEntity<ApiResponse> getAllStudentExamDetailsByStudentId(@PathVariable int id) {
        return ResponseEntity.ok(new ApiResponse("success", studentExamDetailsService.getAllStudentExamDetailsByStudentId(id)));
    }

    @GetMapping("/get/student/{studentId}/exam/{examId}")
    public ResponseEntity<ApiResponse> getStudentExamDetailsByStudentIdAndExamId(@PathVariable int studentId, @PathVariable int examId) {
        try {
            return ResponseEntity.ok(new ApiResponse("success", studentExamDetailsService.getStudentExamDetailsByStudentIdAndExamId(studentId, examId)));
        }catch (ResourceNotFoundException e) {
            return ResponseEntity.internalServerError().body(new ApiResponse("error", e.getMessage()));
        }
    }

    @GetMapping("/get/all/exam/{id}")
    public ResponseEntity<ApiResponse> getAllStudentExamDetailsByExamId(@PathVariable int id) {
        return ResponseEntity.ok(new ApiResponse("success", studentExamDetailsService.getAllStudentExamDetailsByExamId(id)));
    }
}
