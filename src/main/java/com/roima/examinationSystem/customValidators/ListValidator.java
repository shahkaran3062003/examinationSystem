package com.roima.examinationSystem.customValidators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;

public class ListValidator implements ConstraintValidator<ValidList, Object> {
    @Override
    public void initialize(ValidList constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {
        if (value instanceof List) {
            List<?> list = (List<?>) value;

            System.out.println("Woring....");

            for (Object item : list) {
                if (!(item instanceof String)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}
