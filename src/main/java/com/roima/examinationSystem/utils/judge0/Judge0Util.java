package com.roima.examinationSystem.utils.judge0;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.roima.examinationSystem.exception.FetchException;
import com.roima.examinationSystem.exception.InvalidValueException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;


import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.*;


@Component
@RequiredArgsConstructor
public class Judge0Util {


    private final RestTemplate restTemplate;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);


    @Value("${judge0RapidAPI.url}")
    private final String BASE_URL=null;


    @Value("${rapidAPI.host}")
    private final String API_HOST = null;

    @Value("${rapidAPI.key}")
    private final String API_KEY=null;


    public List<BatchSubmissionResult> batchSubmissions(BatchData batchData) throws FetchException, InvalidValueException {

        try{

            System.out.println(batchData);

            HttpHeaders headers = getJudge0Headers();

            Map<String,List<BatchSubmissionRequest>> body = new HashMap<>();

            List<BatchSubmissionRequest> batchSubmissionRequestList = new ArrayList<>();

            for(BatchTestCase testCase : batchData.getTestCaseList()){
                BatchSubmissionRequest batchSubmissionRequest = new BatchSubmissionRequest(
                        convertToBASE64(batchData.getCode()),
                        batchData.getJudge0LanguageId(),
                        convertToBASE64(testCase.getInput()),
                        convertToBASE64(testCase.getOutput())
                );
                batchSubmissionRequestList.add(batchSubmissionRequest);
            }

            body.put("submissions",batchSubmissionRequestList);
            String bodyString = new ObjectMapper().writeValueAsString(body);

            HttpEntity<String> request = new HttpEntity<>(bodyString, headers);


            String requestURL = BASE_URL+"/submissions/batch";

            String url  = UriComponentsBuilder.newInstance()
                    .uri(new URI(requestURL))
                    .queryParam("base64_encoded",true)
                    .build()
                    .toUri()
                    .toURL()
                    .toString();


            ResponseEntity<List<BatchSubmissionResponse>> response = restTemplate.exchange(
              url,
              HttpMethod.POST,
              request,
              new ParameterizedTypeReference<List<BatchSubmissionResponse>>(){}
            );


            if(response.getStatusCode().isError() || response.getBody()==null){
                System.out.println(response.getBody());
                throw new FetchException("Failed to fetch results from Judge0");

            }

            return getBatchSubmissionResult(response.getBody());

        }catch (HttpClientErrorException | MalformedURLException | URISyntaxException e){
            System.out.println(e.getMessage());
            throw new FetchException("Failed to fetch results from Judge0");
        }catch (JsonProcessingException e) {
            System.out.println(e.getMessage());
            throw new InvalidValueException("Invalid body JSON!");
        }

    }



    public List<BatchSubmissionResult> getBatchSubmissionResult(List<BatchSubmissionResponse> batchSubmissionResponses) throws FetchException {


        System.out.println(batchSubmissionResponses);

        StringBuilder tokenBuilder = new StringBuilder();
        int len = batchSubmissionResponses.size();

        for(int i=0;i<len;i++){
            tokenBuilder.append(batchSubmissionResponses.get(i).getToken());
            if(i!=len-1){
                tokenBuilder.append(",");
            }
        }


        HttpHeaders headers = getJudge0Headers();
        HttpEntity<String> request = new HttpEntity<>(headers);


            String requestURL = BASE_URL+"/submissions/batch";

        try{
            String url  = UriComponentsBuilder.newInstance()
                    .uri(new URI(requestURL))
                    .queryParam("tokens",tokenBuilder.toString())
                    .queryParam("base64_encoded",true)
                    .queryParam("fields","token,stderr,status,stdout")
                    .build()
                    .toUri()
                    .toURL()
                    .toString();


            CompletableFuture<List<BatchSubmissionResult>> result = new CompletableFuture<>();

            @RequiredArgsConstructor
            class FetchBatchSubmissionResults implements Runnable{

                private final ScheduledFuture<?> scheduledTask;

                @Override
                public void run() {
                    try{
                        ResponseEntity<Map<String, List<BatchSubmissionResult>>> response = restTemplate.exchange(
                                url,
                                HttpMethod.GET,
                                request,
                                new ParameterizedTypeReference<Map<String, List<BatchSubmissionResult>>>() {}
                        );

                        System.out.println(response.getBody());

                        if(response.getStatusCode() == HttpStatus.OK && response.getBody() != null && response.getBody().get("submissions") != null){

                            List<BatchSubmissionResult> batchSubmissionResultList = response.getBody().get("submissions");

                            System.out.println(batchSubmissionResultList);
                            boolean allCompleted = true;

                            for(BatchSubmissionResult batchSubmissionResult : batchSubmissionResultList ){
                                if(batchSubmissionResult.getStatus().getId()<=2){
                                    allCompleted = false;
                                    break;
                                }

                            }

                            if(allCompleted) {

                                for(BatchSubmissionResult batchSubmissionResult : batchSubmissionResultList ){
                                    if(batchSubmissionResult.getStderr()!=null){
                                        batchSubmissionResult.setStderr(convertFromBASE64(batchSubmissionResult.getStderr()));
                                    }

                                    if(batchSubmissionResult.getStdout()!=null) {
                                        batchSubmissionResult.setStdout(convertFromBASE64(batchSubmissionResult.getStdout()));
                                    }
                                }

                                result.complete(batchSubmissionResultList);
                                scheduledTask.cancel(false);
                            }
                        }

                    }catch (Exception e){
                        result.completeExceptionally(e);
                        scheduledTask.cancel(true);
                    }
                }
            }


            ScheduledFuture<?> scheduledTask = scheduler.scheduleAtFixedRate(
                    new FetchBatchSubmissionResults(null),
                    0,
                    2,
                    TimeUnit.SECONDS
            );


            try {
                return result.get(30, TimeUnit.SECONDS);
            } catch (TimeoutException | InterruptedException | ExecutionException e) {
                scheduledTask.cancel(true);
                System.out.println(e.getMessage());
                throw new FetchException("Failed to fetch results from Judge0");
            }



        } catch (MalformedURLException | URISyntaxException | HttpClientErrorException | FetchException e) {
            System.out.println(e.getMessage());
            throw new FetchException("Failed to fetch results from Judge0");
        }
    }


    public boolean isAllTestCasesPassed(List<BatchSubmissionResult> batchSubmissionResultList){
        for(BatchSubmissionResult batchSubmissionResult : batchSubmissionResultList){
            if(batchSubmissionResult.getStatus().getId()!=3){
                return false;
            }
        }
        return true;
    }


    private String convertToBASE64(String code){
        return Base64.getEncoder().encodeToString(code.getBytes());
    }

    private String convertFromBASE64(String code){
        return new String(Base64.getDecoder().decode(code));
    }

    private HttpHeaders getJudge0Headers(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-rapidapi-host", API_HOST);
        headers.set("x-rapidapi-key",API_KEY);
        return  headers;
    }



}
