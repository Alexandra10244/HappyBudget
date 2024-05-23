package com.hbadget.happy_budget.exceptions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class ExceptionHandlerAdvice extends ResponseEntityExceptionHandler {
    private final ObjectMapper objectMapper;

    @ExceptionHandler(BudgetAlreadyExistsException.class)
    public ResponseEntity<String> budgetAlreadyExistException(BudgetAlreadyExistsException budgetAlreadyExistsException) {
        return new ResponseEntity<>(objectToString(Map.of("message", budgetAlreadyExistsException.getMessage())), BAD_REQUEST);
    }
    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<String> insufficientFundsException(InsufficientFundsException insufficientFundsException) {
        return new ResponseEntity<>(objectToString(Map.of("message", insufficientFundsException.getMessage())), NOT_FOUND);
    }
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> userNotFoundException(UserNotFoundException userNotFoundException) {
        return new ResponseEntity<>(objectToString(Map.of("message", userNotFoundException.getMessage())), NOT_FOUND);
    }

    @ExceptionHandler(BudgetNotFoundException.class)
    public ResponseEntity<String> budgetNotFoundException(BudgetNotFoundException budgetNotFoundException) {
        return new ResponseEntity<>(objectToString(Map.of("message", budgetNotFoundException.getMessage())), NOT_FOUND);
    }

    @ExceptionHandler(ExpenseNotFoundException.class)
    public ResponseEntity<String> expenseNotFoundException(ExpenseNotFoundException expenseNotFoundException) {
        return new ResponseEntity<>(objectToString(Map.of("message", expenseNotFoundException.getMessage())), NOT_FOUND);
    }

    @ExceptionHandler(IncomeNotFoundException.class)
    public ResponseEntity<String> incomeNotFoundException(IncomeNotFoundException incomeNotFoundException) {
        return new ResponseEntity<>(objectToString(Map.of("message", incomeNotFoundException.getMessage())), NOT_FOUND);
    }

    @ExceptionHandler(InvalidPhoneNumberException.class)
    public ResponseEntity<String> invalidPhoneNumberException(InvalidPhoneNumberException invalidPhoneNumberException) {
        return new ResponseEntity<>(objectToString(Map.of("message", invalidPhoneNumberException.getMessage())), CONFLICT);
    }

    @ExceptionHandler(InvalidEmailFormatException.class)
    public ResponseEntity<String> invalidEmailFormatException(InvalidEmailFormatException invalidEmailFormatException) {
        return new ResponseEntity<>(objectToString(Map.of("message", invalidEmailFormatException.getMessage())), CONFLICT);
    }

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<String> authException(AuthException authException) {
        return new ResponseEntity<>(objectToString(Map.of("message", authException.getMessage())), BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex, WebRequest request) {
        Map<String, String> errors = new LinkedHashMap<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            String propertyPath = violation.getPropertyPath().toString();
            String fieldName = propertyPath.substring(propertyPath.lastIndexOf('.') + 1);
            String message = violation.getMessage();
            errors.put(fieldName, message);
        }
        return new ResponseEntity<>(objectToString(errors), HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, String> errors = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            String defaultMessage = Objects.requireNonNull(error.getDefaultMessage());
            errors.put(error.getField(), defaultMessage);
        });
        return new ResponseEntity<>(objectToString(errors), HttpStatus.BAD_REQUEST);
    }

    private String objectToString(Object response) {
        try {
            return objectMapper.writeValueAsString(response);
        } catch (JsonProcessingException exception) {
            log.error("Error processing response to string");
            return "Internal error";
        }
    }


}
