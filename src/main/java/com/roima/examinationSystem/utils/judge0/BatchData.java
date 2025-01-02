package com.roima.examinationSystem.utils.judge0;

import com.roima.examinationSystem.model.ProgrammingTestCase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class BatchData {
    private int judge0LanguageId;
    private String code;
    private List<BatchTestCase> testCaseList;

}
