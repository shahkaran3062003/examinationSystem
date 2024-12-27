package com.roima.examinationSystem.service.judge0;

import com.roima.examinationSystem.dto.judge0.Judge0LanguageDto;
import com.roima.examinationSystem.exception.FetchException;
import com.roima.examinationSystem.request.AddLanguageRequest;

import java.util.List;

public interface IJudge0Service {
    List<Judge0LanguageDto> getAllJudge0Languages() throws FetchException;

    boolean isLanguageSupported(AddLanguageRequest request);
}
