package com.roima.examinationSystem.customValidators;

import jakarta.validation.Payload;

public @interface ValidList {
    String message() default "Field must be a List";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
