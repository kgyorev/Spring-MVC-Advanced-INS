package com.insurance.ins.utils.annotations;

import com.insurance.ins.utils.validators.fields.PastDateValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = PastDateValidator.class)
public @interface PastDate {
    String message() default "Invalid date.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
