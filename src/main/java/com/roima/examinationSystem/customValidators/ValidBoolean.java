package com.roima.examinationSystem.customValidators;

import jakarta.validation.Payload;

public @interface ValidBoolean {
    String message() default "Field must be a Boolean";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
