package com.softserve.itacademy.todolist.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<?> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
//        logger.error(ex + " " + ex.getMessage());
//        return ResponseEntity.badRequest().body(ex.getBindingResult().toString());
//    }

    @ExceptionHandler(NullEntityReferenceException.class)
    public ResponseEntity<?> handleNullEntityReferenceException(NullEntityReferenceException ex) {
        logger.error(ex);
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handleEntityNotFoundException(EntityNotFoundException ex) {
        logger.error(ex);
        return ResponseEntity.notFound().build();
    }

    // todo: add more handlers
    // todo: NoSuchElementException
}
