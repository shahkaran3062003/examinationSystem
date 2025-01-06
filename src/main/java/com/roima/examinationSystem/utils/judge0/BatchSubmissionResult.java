package com.roima.examinationSystem.utils.judge0;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BatchSubmissionResult{
    private String token;
    private String stderr;
    private Status status;
    private String stdout;

}
