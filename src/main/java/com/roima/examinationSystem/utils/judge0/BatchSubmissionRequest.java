package com.roima.examinationSystem.utils.judge0;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BatchSubmissionRequest {
    private String source_code;
    private int language_id;
    private String stdin;
    private String expected_output;
}
