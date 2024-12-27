package com.roima.examinationSystem.service.judge0;

import com.roima.examinationSystem.dto.LanguageDto;
import com.roima.examinationSystem.dto.judge0.Judge0LanguageDto;
import com.roima.examinationSystem.exception.FetchException;
import com.roima.examinationSystem.request.AddLanguageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;


@Service
@RequiredArgsConstructor
public class Judge0Service implements IJudge0Service {

    private final RestTemplate restTemplate;


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


}
