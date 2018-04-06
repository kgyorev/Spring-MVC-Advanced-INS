package com.insurance.ins.utils.validators.fields;

import com.insurance.ins.utils.annotations.Existing;
import com.insurance.ins.utils.interfaces.FieldValueExists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ExistingValidator implements ConstraintValidator<Existing, Object> {
    @Autowired
    private ApplicationContext applicationContext;

    private FieldValueExists service;
    private String fieldName;

    @Override
    public void initialize(Existing existing) {
        Class<? extends FieldValueExists> clazz = existing.service();
        this.fieldName = existing.fieldName();
        String serviceQualifier = existing.serviceQualifier();

        if (!serviceQualifier.equals("")) {
            this.service = this.applicationContext.getBean(serviceQualifier, clazz);
        } else {
            this.service = this.applicationContext.getBean(clazz);
        }
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        return this.service.fieldValueExists(o, this.fieldName);
    }
}
