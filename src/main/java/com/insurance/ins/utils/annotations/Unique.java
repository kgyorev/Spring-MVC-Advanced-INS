package com.insurance.ins.utils.annotations;

import com.insurance.ins.utils.interfaces.FieldValueExists;
import com.insurance.ins.utils.validators.fields.UniqueValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueValidator.class)
@Documented
public @interface Unique {
    String message() default "Field must be Unique";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    Class<? extends FieldValueExists> service();
    String serviceQualifier() default "";
    String fieldName();
}