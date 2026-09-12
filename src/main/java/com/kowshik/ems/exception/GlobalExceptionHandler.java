package com.kowshik.ems.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.kowshik.ems.exception.EmployeeNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public String handleException(
            Exception exception,
            Model model) {

        model.addAttribute(
                "errorMessage",
                exception.getMessage()
        );

        return "error";
    }

    @ExceptionHandler(EmployeeNotFoundException.class)
    public String handleEmployeeNotFound(
            EmployeeNotFoundException exception,
            Model model) {

        model.addAttribute(
                "errorMessage",
                exception.getMessage()
        );

        return "error";
    }
}