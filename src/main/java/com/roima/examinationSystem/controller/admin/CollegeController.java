package com.roima.examinationSystem.controller.admin;

import com.roima.examinationSystem.exception.ResourceExistsException;
import com.roima.examinationSystem.exception.ResourceNotFoundException;
import com.roima.examinationSystem.request.AddCollegeRequest;
import com.roima.examinationSystem.request.UpdateCollegeRequest;
import com.roima.examinationSystem.response.ApiResponse;
import com.roima.examinationSystem.service.admin.userCollegeManagement.UserCollegeManagementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("${api.adminPrefix}/college")
public class CollegeController {

    private final UserCollegeManagementService userCollegeManagementService;

    @GetMapping("/get/all")
    public ResponseEntity<ApiResponse> getAllColleges() {
            return ResponseEntity.ok(new ApiResponse("Success", userCollegeManagementService.getAllColleges()));
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<ApiResponse> getCollegeById(@PathVariable int id) {
        try{
            return ResponseEntity.ok(new ApiResponse("Success", userCollegeManagementService.getCollegeById(id)));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.internalServerError().body(new ApiResponse("Error", e.getMessage()));
        }
    }

    @GetMapping("/get/name")
    public ResponseEntity<ApiResponse> getCollegeByName(@RequestParam(name = "name") String name) {
        try{
            return ResponseEntity.ok(new ApiResponse("Success", userCollegeManagementService.getCollegeByName(name)));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.internalServerError().body(new ApiResponse("Error", e.getMessage()));
        }
    }


    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addCollege(@RequestBody @Valid AddCollegeRequest request) {
        try{
            userCollegeManagementService.addCollege(request);
            return ResponseEntity.ok(new ApiResponse("Success", "College added successfully!"));
        } catch (ResourceExistsException  e) {
            return ResponseEntity.internalServerError().body(new ApiResponse("Error", e.getMessage()));
        }

    }


    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse> updateCollege(@RequestBody @Valid UpdateCollegeRequest request, @PathVariable int id) {
        try{
            userCollegeManagementService.updateCollege(request, id);
            return ResponseEntity.ok(new ApiResponse("Success", "College updated successfully!"));

        } catch (ResourceExistsException | ResourceNotFoundException e) {
            return ResponseEntity.internalServerError().body(new ApiResponse("Error", e.getMessage()));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteCollege(@PathVariable int id) {
        try{
            userCollegeManagementService.deleteCollegeById(id);
            return ResponseEntity.ok(new ApiResponse("Success", "College deleted successfully!"));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.internalServerError().body(new ApiResponse("Error", e.getMessage()));
        }
    }
}
