package com.roima.examinationSystem.service.mcqOptions;

import com.roima.examinationSystem.exception.ResourceNotFoundException;
import com.roima.examinationSystem.model.McqOptions;
import com.roima.examinationSystem.model.McqQuestions;
import com.roima.examinationSystem.repository.McqOptionsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class McqOptionsService implements IMcqOptionsService {

    private final McqOptionsRepository mcqOptionsRepository;

    @Override
    public McqOptions addMcqOptions(int option_number, String text) {
        McqOptions mcqOption = new McqOptions(option_number,text);
        return mcqOptionsRepository.save(mcqOption);
    }

    @Override
    public void deleteMcqOptions(int id) throws ResourceNotFoundException {
        McqOptions mcqoptions =  mcqOptionsRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Mcq Option not found!"));
        McqQuestions mcqQuestions = mcqoptions.getMcqQuestions();

        if(mcqQuestions.getCorrect_option() == mcqoptions.getOption_number()){
            throw new ResourceNotFoundException("Cannot delete correct option!");
        }

        mcqOptionsRepository.delete(mcqoptions);

    }

    @Override
    public void updateMcqOptions(int option_number, String text, int id) throws ResourceNotFoundException {
            McqOptions mcqoptions =  mcqOptionsRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Mcq Option not found!"));
            mcqoptions.setOption_number(option_number);
            mcqoptions.setText(text);
            mcqOptionsRepository.save(mcqoptions);
    }
}
