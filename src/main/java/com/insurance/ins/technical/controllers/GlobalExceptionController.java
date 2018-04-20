package com.insurance.ins.technical.controllers;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;



@ControllerAdvice
public class GlobalExceptionController {
    private static final String DEFAULT_ERROR_MESSAGE = "There was an error with your request. Please Call Application Administrator for Support.";

    @ExceptionHandler(RuntimeException.class)
    public String getException(RuntimeException e,Model model) {
        if(e instanceof AccessDeniedException){
            return "error/unauthorized";
        }
        String errorMessage =
                e.getClass().isAnnotationPresent(ResponseStatus.class)
                        ? e.getClass().getAnnotation(ResponseStatus.class).reason()
                        : DEFAULT_ERROR_MESSAGE;
        model.addAttribute("error",errorMessage);
        return "error/error-template";
    }
}
