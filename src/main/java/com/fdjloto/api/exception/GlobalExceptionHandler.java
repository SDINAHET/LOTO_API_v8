package com.fdjloto.api.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 🎟 Ticket not found → 404
    @ExceptionHandler(TicketNotFoundException.class)
    public ResponseEntity<String> handleTicketNotFound(TicketNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }

    // 📝 Validation JPA → 400
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> handleValidation(ConstraintViolationException ex) {
        return ResponseEntity
                .badRequest()
                .body("Validation error");
    }
}
