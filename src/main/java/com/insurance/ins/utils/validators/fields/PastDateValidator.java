package com.insurance.ins.utils.validators.fields;

import com.insurance.ins.utils.annotations.PastDate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class PastDateValidator implements ConstraintValidator<PastDate, LocalDate> {
    @Override
    public boolean isValid(LocalDate date, ConstraintValidatorContext constraintValidatorContext) {
        LocalDate today = LocalDate.now();
        return date != null && !date.isAfter(today);
    }
}

