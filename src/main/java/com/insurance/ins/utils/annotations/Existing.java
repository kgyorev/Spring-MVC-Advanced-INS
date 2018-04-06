package com.insurance.ins.utils.annotations;

import com.insurance.ins.utils.interfaces.FieldValueExists;
import com.insurance.ins.utils.validators.fields.ExistingValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ExistingValidator.class)
@Documented
public @interface Existing {
    String message() default "Value must be Existing";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    Class<? extends FieldValueExists> service();
    String serviceQualifier() default "";
    String fieldName();
}