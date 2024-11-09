package com.roima.examinationSystem.controller;

import com.roima.examinationSystem.dto.CollegeDto;
import com.roima.examinationSystem.exception.ResourceExistsException;
import com.roima.examinationSystem.exception.ResourceNotFoundException;
import com.roima.examinationSystem.model.College;
import com.roima.examinationSystem.request.AddCollegeRequest;
import com.roima.examinationSystem.request.UpdateCollegeRequest;
import com.roima.examinationSystem.response.ApiResponse;
import com.roima.examinationSystem.service.college.CollegeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/college")
public class CollegeController {

    private final CollegeService collegeService;

    @GetMapping("/get/all")
    public ResponseEntity<ApiResponse> getAllColleges() {
            return ResponseEntity.ok(new ApiResponse("Success", collegeService.getConvertedDtoList(collegeService.getAllColleges())));
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<ApiResponse> getCollegeById(@PathVariable int id) {
        try{
            College college = collegeService.getCollegeById(id);
            CollegeDto collegeDto = collegeService.convertToDto(college);
            return ResponseEntity.ok(new ApiResponse("Success", collegeDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.internalServerError().body(new ApiResponse("Error", e.getMessage()));
        }
    }

    @GetMapping("/get/name")
    public ResponseEntity<ApiResponse> getCollegeByName(@RequestParam(name = "name") String name) {
        try{
            College college = collegeService.getCollegeByName(name);
            CollegeDto collegeDto = collegeService.convertToDto(college);
            return ResponseEntity.ok(new ApiResponse("Success", collegeDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.internalServerError().body(new ApiResponse("Error", e.getMessage()));
        }
    }


    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addCollege(@RequestBody @Valid AddCollegeRequest request) {
        try{
            collegeService.addCollege(request);
            return ResponseEntity.ok(new ApiResponse("Success", "College added successfully!"));
        } catch (ResourceExistsException  e) {
            return ResponseEntity.internalServerError().body(new ApiResponse("Error", e.getMessage()));
        }

    }


    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse> updateCollege(@RequestBody @Valid UpdateCollegeRequest request, @PathVariable int id) {
        try{
            collegeService.updateCollege(request, id);
            return ResponseEntity.ok(new ApiResponse("Success", "College updated successfully!"));

        } catch (ResourceExistsException | ResourceNotFoundException e) {
            return ResponseEntity.internalServerError().body(new ApiResponse("Error", e.getMessage()));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteCollege(@PathVariable int id) {
        try{
            collegeService.deleteCollegeById(id);
            return ResponseEntity.ok(new ApiResponse("Success", "College deleted successfully!"));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.internalServerError().body(new ApiResponse("Error", e.getMessage()));
        }
    }
}
