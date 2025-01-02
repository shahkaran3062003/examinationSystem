package com.roima.examinationSystem.service.judge0;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.roima.examinationSystem.dto.judge0.Judge0LanguageDto;
import com.roima.examinationSystem.exception.FetchException;
import com.roima.examinationSystem.exception.InvalidValueException;
import com.roima.examinationSystem.exception.ResourceNotFoundException;
import com.roima.examinationSystem.request.AddLanguageRequest;
import com.roima.examinationSystem.request.CodeExecutionRequest;
import com.roima.examinationSystem.request.CodeRunTestRequest;
import com.roima.examinationSystem.utils.judge0.BatchSubmissionResponse;
import com.roima.examinationSystem.utils.judge0.BatchSubmissionResult;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.List;

public interface IJudge0Service {
    List<Judge0LanguageDto> getAllJudge0Languages() throws FetchException;

    boolean isLanguageSupported(AddLanguageRequest request);

    List<BatchSubmissionResult> batchSubmissions() throws ResourceNotFoundException, FetchException, InvalidValueException;

    List<BatchSubmissionResult> codeExecution(CodeExecutionRequest request) throws InvalidValueException, FetchException, ResourceNotFoundException;

    List<BatchSubmissionResult> runTestCases(CodeRunTestRequest request) throws InvalidValueException, FetchException, ResourceNotFoundException;
}
