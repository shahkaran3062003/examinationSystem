package com.roima.examinationSystem.utils;

import com.roima.examinationSystem.model.Difficulty;
import com.roima.examinationSystem.model.Exam;
import com.roima.examinationSystem.model.ExamCategoryDetails;
import com.roima.examinationSystem.model.QuestionType;
import com.roima.examinationSystem.request.AddUpdateExamCategoryDetailsRequest;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class Utils {

    private final Map<Difficulty,Integer> difficultyMap = new HashMap<>()
    {{
        put(Difficulty.EASY,1);
        put(Difficulty.MEDIUM,2);
        put(Difficulty.HARD,4);
    }};



    /**
     This method calculates the total difficulty points by summing up the difficulty points of all MCQ questions in the exam.
     It then calculates the exam difficulty ratio by dividing the total difficulty points by the total number of MCQ questions.
     The method returns true if the exam difficulty ratio is within the allowed range for the given exam difficulty, and false otherwise.

     This method used when adding new ExamCategoryDetails
     */
    public boolean isExamDifficultyCorrect(Exam exam, AddUpdateExamCategoryDetailsRequest request){

        Difficulty difficulty = Difficulty.valueOf(request.getDifficulty());
        int newDifficulty = difficultyMap.get(difficulty);
        int currentDifficultyPoints = 0;
        int totalMCQQuestions = 0;
        float examDifficultyRatio;
        Difficulty examDifficulty = exam.getDifficulty();

        for(ExamCategoryDetails ecd : exam.getExamCategoryDetails()){


            if(ecd.getCategory().getQuestionType() == QuestionType.MCQ){

                totalMCQQuestions += ecd.getCount();
                currentDifficultyPoints += (ecd.getCount() * difficultyMap.get(ecd.getDifficulty()));
            }
        }



        currentDifficultyPoints += request.getCount() * newDifficulty;
        totalMCQQuestions += request.getCount();

        examDifficultyRatio = (float)currentDifficultyPoints/ (float)totalMCQQuestions;

        System.out.println(" "+examDifficultyRatio+", "+currentDifficultyPoints+","+totalMCQQuestions +","+(examDifficultyRatio-1.33));


        // 2 Easy = 1 Medium == 1.33 difficulty (1/2 Medium question allowed for Easy difficulty)
        // 8 Easy = 1 Hard == 1.33 difficulty (1/8 Hard question allowed for Easy difficulty)
        if(examDifficulty == Difficulty.EASY && ((examDifficultyRatio - 1.33) <= 0.01)){
            return true;
        }

        // 4 Medium = 1 Hard == 2.4 difficulty (1/4 Hard question allowed for Medium difficulty)
        else if(examDifficulty == Difficulty.MEDIUM && ((examDifficultyRatio - 2.4) <= 0.01)){
            return true;
        }
        else if(examDifficulty == Difficulty.HARD && ((examDifficultyRatio-4.0) <= 0.01)){
            return true;
        }

        return false;
    }



    /**
     This method calculates the total difficulty points by summing up the difficulty points of all MCQ questions in the exam.
     It then calculates the exam difficulty ratio by dividing the total difficulty points by the total number of MCQ questions.
     The method returns true if the exam difficulty ratio is within the allowed range for the given exam difficulty, and false otherwise.

     This method is used when updating an ExamCategoryDetails
     */
    public boolean isExamDifficultyCorrect(Exam exam, AddUpdateExamCategoryDetailsRequest request, int endId){

        Difficulty difficulty = Difficulty.valueOf(request.getDifficulty());
        int newDifficulty = difficultyMap.get(difficulty);
        int currentDifficultyPoints = 0;
        int totalMCQQuestions = 0;
        float examDifficultyRatio;
        Difficulty examDifficulty = exam.getDifficulty();

        for(ExamCategoryDetails ecd : exam.getExamCategoryDetails()){

            if(ecd.getCategory().getQuestionType() == QuestionType.MCQ){

                if(ecd.getId() == endId){
                    continue;
                }

                totalMCQQuestions += ecd.getCount();
                currentDifficultyPoints += (ecd.getCount() * difficultyMap.get(ecd.getDifficulty()));
            }
        }



        currentDifficultyPoints += request.getCount() * newDifficulty;
        totalMCQQuestions += request.getCount();

        examDifficultyRatio = (float)currentDifficultyPoints/ (float)totalMCQQuestions;

        System.out.println(" "+examDifficultyRatio+", "+currentDifficultyPoints+","+totalMCQQuestions +","+(examDifficultyRatio-1.33));


        // 2 Easy = 1 Medium == 1.33 difficulty (1/2 Medium question allowed for Easy difficulty)
        // 8 Easy = 1 Hard == 1.33 difficulty (1/8 Hard question allowed for Easy difficulty)
        if(examDifficulty == Difficulty.EASY && ((examDifficultyRatio - 1.33) <= 0.01)){
            return true;
        }

        // 4 Medium = 1 Hard == 2.4 difficulty (1/4 Hard question allowed for Medium difficulty)
        else if(examDifficulty == Difficulty.MEDIUM && ((examDifficultyRatio - 2.4) <= 0.01)){
            return true;
        }
        else if(examDifficulty == Difficulty.HARD && ((examDifficultyRatio-4.0) <= 0.01)){
            return true;
        }

        return false;
    }
}
