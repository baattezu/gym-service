package org.saltaonelove.exception;

import org.saltaonelove.exception.exceptions.CustomException;
import org.saltaonelove.util.ExceptionCodeMapper;
import org.saltaonelove.util.logging.LoggingUtil;
import org.saltaonelove.util.RequestContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    public static final LoggingUtil log = LoggingUtil.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getErrorCode(), ex.getMessage());
        log.logError(RequestContext.getMethod(), RequestContext.getEndpoint(), ex, errorResponse);
        return ResponseEntity.status(ex.getErrorCode()).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Validation failed", errors);
        log.logError(RequestContext.getMethod(), RequestContext.getEndpoint(), ex, errorResponse);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllExceptions(Exception ex) {
        HttpStatus status = ExceptionCodeMapper.getHttpStatusForException(ex);
        ErrorResponse errorResponse = new ErrorResponse(status.value(), ex.getMessage());
        log.logError(RequestContext.getMethod(), RequestContext.getEndpoint(), ex, errorResponse);
        return ResponseEntity.status(status).body(errorResponse);
    }

}
