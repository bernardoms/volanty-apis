package com.bernardoms.carapi.controller;

import com.bernardoms.carapi.exception.CarNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class ExceptionController {

    private static final String DESCRIPTION = "description";

    @ExceptionHandler({BindException.class, HttpMessageNotReadableException.class, IllegalArgumentException.class, MethodArgumentTypeMismatchException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private Object handleIllegalArgumentException(Exception ex, HttpServletRequest request) {
        log.error("invalid arguments/body for processing the request: " + request.getRequestURI(), ex);
        return mountError(ex);
    }

    @ExceptionHandler({CarNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    private Object handleTimeoutException(CarNotFoundException ex, HttpServletRequest request) {
        log.info("car not found!" + request.getRequestURI());
        return mountError(ex);
    }

    private HashMap<Object, Object> mountError(Exception e) {
        var error = new HashMap<>();
        error.put(DESCRIPTION, e.getMessage());
        return error;
    }
}
