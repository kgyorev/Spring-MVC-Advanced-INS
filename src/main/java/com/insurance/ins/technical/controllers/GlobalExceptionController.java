package com.insurance.ins.technical.controllers;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import static com.insurance.ins.technical.store.WebConstants.DEFAULT_ERROR_MESSAGE;


@ControllerAdvice
public class GlobalExceptionController {

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
