package com.roima.examinationSystem.service.judge0;

import com.roima.examinationSystem.dto.LanguageDto;
import com.roima.examinationSystem.dto.judge0.Judge0LanguageDto;
import com.roima.examinationSystem.exception.FetchException;
import com.roima.examinationSystem.exception.InvalidValueException;
import com.roima.examinationSystem.exception.ResourceNotFoundException;
import com.roima.examinationSystem.model.Language;
import com.roima.examinationSystem.model.ProgrammingQuestions;
import com.roima.examinationSystem.model.ProgrammingTestCase;
import com.roima.examinationSystem.model.TestCaseType;
import com.roima.examinationSystem.repository.LanguageRepository;
import com.roima.examinationSystem.repository.ProgrammingQuestionsRepository;
import com.roima.examinationSystem.request.AddLanguageRequest;
import com.roima.examinationSystem.request.CodeExecutionRequest;
import com.roima.examinationSystem.request.CodeRunTestRequest;
import com.roima.examinationSystem.utils.judge0.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class Judge0Service implements IJudge0Service {

    private final RestTemplate restTemplate;
    private final Judge0Util judge0Util;
    private final ProgrammingQuestionsRepository programmingQuestionsRepository;
    private final LanguageRepository languageRepository;


    @Value("${judge0.url}")
    private String JUDGE0_URL;


    @Override
    public List<Judge0LanguageDto> getAllJudge0Languages() throws FetchException {
        String URL = JUDGE0_URL+"/languages/all";
        ResponseEntity<Judge0LanguageDto[]> response = restTemplate.exchange(URL, HttpMethod.GET,null, Judge0LanguageDto[].class);

        if(response.getStatusCode().isError()){
            throw new FetchException("Failed to fetch languages from Judge0");
        }

        return List.of(response.getBody());
    }


    @Override
    public boolean isLanguageSupported(AddLanguageRequest request) {

        try {
            String URL = JUDGE0_URL + "/languages/" + request.getJudge0Id();
            ResponseEntity<LanguageDto> response = restTemplate.exchange(
                    URL,
                    HttpMethod.GET,
                    null,
                    LanguageDto.class
            );

            if (response.getStatusCode().isError()) {
                return false;
            }

            LanguageDto language = response.getBody();

            if( language!= null &&  (!language.getName().equals(request.getName()))){
                return false;
            }
            return true;

        }catch (HttpClientErrorException e){
            return false;
        }
    }

    @Override
    public List<BatchSubmissionResult> batchSubmissions() throws FetchException, InvalidValueException, ResourceNotFoundException {

        ProgrammingQuestions programmingQuestions = programmingQuestionsRepository.findById(1).orElseThrow(()-> new ResourceNotFoundException("Programming Question not found!"));

        BatchData batchData = new BatchData();
        batchData.setCode(programmingQuestions.getImplementation());
        batchData.setJudge0LanguageId(programmingQuestions.getImplementationLanguage().getJudge0Id());

        List<BatchTestCase> batchTestCaseList = new ArrayList<>();

        for(ProgrammingTestCase testCase: programmingQuestions.getProgrammingTestCase()){

            BatchTestCase batchTestCase = new BatchTestCase(testCase.getInput(),testCase.getOutput());
            batchTestCaseList.add(batchTestCase);
        }

        batchData.setTestCaseList(batchTestCaseList);


        return judge0Util.batchSubmissions(batchData);

    }

    @Override
    public List<BatchSubmissionResult> codeExecution(CodeExecutionRequest request) throws InvalidValueException, FetchException, ResourceNotFoundException {

        Language language = languageRepository.findById(request.getLanguageId()).orElseThrow(()-> new ResourceNotFoundException("Language not found!"));

        BatchData batchData = new BatchData();
        batchData.setCode(request.getCode());
        batchData.setJudge0LanguageId(language.getJudge0Id());
        List<BatchTestCase> batchTestCaseList = new ArrayList<>();
        batchTestCaseList.add(new BatchTestCase(request.getInput(),request.getOutput()));
        batchData.setTestCaseList(batchTestCaseList);

        return judge0Util.batchSubmissions(batchData);

    }

    @Override
    public List<BatchSubmissionResult> runTestCases(CodeRunTestRequest request) throws InvalidValueException, FetchException, ResourceNotFoundException {
        Language language = languageRepository.findById(request.getLanguageId()).orElseThrow(()-> new ResourceNotFoundException("Language not found!"));
        ProgrammingQuestions programmingQuestions = programmingQuestionsRepository.findById(request.getProgrammingQuestionsId()).orElseThrow(()-> new ResourceAccessException("Programming question not found!"));

        BatchData batchData = new BatchData();
        batchData.setCode(request.getCode());
        batchData.setJudge0LanguageId(language.getJudge0Id());

        List<BatchTestCase> batchTestCaseList = new ArrayList<>();
        for(ProgrammingTestCase testCase: programmingQuestions.getProgrammingTestCase()){
            if(testCase.getType()!= TestCaseType.HIDDEN){
                batchTestCaseList.add(new BatchTestCase(testCase.getInput(),testCase.getOutput()));
            }
        }

        batchData.setTestCaseList(batchTestCaseList);

        return judge0Util.batchSubmissions(batchData);

    }
}
