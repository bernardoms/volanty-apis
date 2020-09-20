package com.bernardoms.cavapi.controller;

import com.bernardoms.cavapi.exception.CarNotOnRequestedCavException;
import com.bernardoms.cavapi.exception.CavNotFoundException;
import com.bernardoms.cavapi.exception.NoAvailabilitiesException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
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

    @ExceptionHandler({Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    private Object handleException(Exception ex, HttpServletRequest request) {
        log.error("error on process the request: " + request.getRequestURI(), ex);
        return mountError(ex);
    }

    @ExceptionHandler({NoAvailabilitiesException.class, CavNotFoundException.class, CarNotOnRequestedCavException.class})
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    private Object handleNoAvailabilitiesException(Exception ex, HttpServletRequest request) {
        log.info("no availability!");
        return mountError(ex);
    }

    @ExceptionHandler({ResourceAccessException.class})
    @ResponseStatus(HttpStatus.REQUEST_TIMEOUT)
    private Object handleTimeoutException(ResourceAccessException ex, HttpServletRequest request) {
        log.info("timeout processing the request" + request.getRequestURI());
        return mountError(ex);
    }

    @ExceptionHandler({HttpClientErrorException.class})
    private ResponseEntity<Object> handleClientException(HttpClientErrorException ex, HttpServletRequest request) {
        log.info("error 4xx calling the client " + request.getRequestURI());
       return new ResponseEntity<>(mountError(ex), ex.getStatusCode());
    }

    @ExceptionHandler({HttpServerErrorException.class})
    private ResponseEntity<Object> handleServerException(HttpServerErrorException ex, HttpServletRequest request) {
        log.error("error 5xx calling the client: " + request.getRequestURI(), ex);
        return new ResponseEntity<>(mountError(ex), ex.getStatusCode());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected Object handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, String> details = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(field -> details.put(field.getField(), field.getDefaultMessage()));

        log.info("error on the request validation {}", details);

        return Map.of(DESCRIPTION, details);
    }

    private HashMap<Object, Object> mountError(Exception e) {
        var error = new HashMap<>();
        error.put(DESCRIPTION, e.getMessage());
        return error;
    }
}
