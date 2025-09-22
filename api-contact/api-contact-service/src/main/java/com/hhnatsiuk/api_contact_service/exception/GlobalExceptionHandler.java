package com.hhnatsiuk.api_contact_service.exception;

import com.hhnatsiuk.api_contact_if.model.generated.ErrorDetailDTO;
import com.hhnatsiuk.api_contact_if.model.generated.ErrorResponseDTO;
import com.hhnatsiuk.api_contact_if.model.generated.ValidationErrorResponseDTO;
import com.hhnatsiuk.api_contact_service.exception.global.ApiException;
import com.hhnatsiuk.api_contact_service.exception.global.ValidationException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import org.springframework.security.core.AuthenticationException;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponseDTO> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        List<ErrorDetailDTO> details = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fe -> new ErrorDetailDTO()
                        .field(fe.getField())
                        .error(fe.getDefaultMessage()))
                .collect(Collectors.toList());

        ValidationErrorResponseDTO dto = new ValidationErrorResponseDTO()
                .code(HttpStatus.BAD_REQUEST.value())
                .message("Validation failed for one or more fields")
                .details(details);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(dto);
    }

    // 400 - validation on @PathVariable/@RequestParam (@Validated)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ValidationErrorResponseDTO> handleConstraintViolation(ConstraintViolationException ex) {
        List<ErrorDetailDTO> details = ex.getConstraintViolations()
                .stream()
                .map(this::toErrorDetail)
                .collect(Collectors.toList());

        ValidationErrorResponseDTO dto = new ValidationErrorResponseDTO()
                .code(HttpStatus.BAD_REQUEST.value())
                .message("Validation failed for one or more parameters")
                .details(details);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(dto);
    }

    // 400 - bad/malformed JSON
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponseDTO> handleMalformedJson(HttpMessageNotReadableException ex) {
        ErrorResponseDTO dto = new ErrorResponseDTO()
                .code(HttpStatus.BAD_REQUEST.value())
                .message("Malformed JSON request");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(dto);
    }

    // 400 - wrong param type
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponseDTO> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String param = ex.getName();
        String expected = ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "unknown";
        ErrorResponseDTO dto = new ErrorResponseDTO()
                .code(HttpStatus.BAD_REQUEST.value())
                .message("Parameter '" + param + "' has invalid value; expected type: " + expected);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(dto);
    }

    // 400 - missing required request parameter
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponseDTO> handleMissingParam(MissingServletRequestParameterException ex) {
        ErrorResponseDTO dto = new ErrorResponseDTO()
                .code(HttpStatus.BAD_REQUEST.value())
                .message("Missing required request parameter: " + ex.getParameterName());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(dto);
    }

    // 401
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponseDTO> handleAuthenticationException(AuthenticationException ex) {
        ErrorResponseDTO dto = new ErrorResponseDTO()
                .code(HttpStatus.UNAUTHORIZED.value())
                .message("Authentication failed");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(dto);
    }

    // 403
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponseDTO> handleAccessDenied(AccessDeniedException ex) {
        ErrorResponseDTO dto = new ErrorResponseDTO()
                .code(HttpStatus.FORBIDDEN.value())
                .message("Forbidden");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(dto);
    }

    // Custom API exception with explicit HTTP code
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponseDTO> handleApiException(ApiException ex) {
        HttpStatus status = HttpStatus.resolve(ex.getCode());
        if (status == null) status = HttpStatus.INTERNAL_SERVER_ERROR;

        ErrorResponseDTO dto = new ErrorResponseDTO()
                .code(status.value())
                .message(ex.getMessage());

        return new ResponseEntity<>(dto, status);
    }

    // Custom validation exception with details (aligns with ValidationErrorResponse)
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ValidationErrorResponseDTO> handleValidationException(ValidationException ex) {
        HttpStatus status = HttpStatus.resolve(ex.getCode());
        if (status == null) status = HttpStatus.UNPROCESSABLE_ENTITY;

        ValidationErrorResponseDTO dto = new ValidationErrorResponseDTO()
                .code(status.value())
                .message(ex.getMessage())
                .details(ex.getDetails());

        return new ResponseEntity<>(dto, status);
    }

    // 405 (optional override; DefaultHandlerExceptionResolver also covers this)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponseDTO> handleMethodNotAllowed(HttpRequestMethodNotSupportedException ex) {
        ErrorResponseDTO dto = new ErrorResponseDTO()
                .code(HttpStatus.METHOD_NOT_ALLOWED.value())
                .message("Method not allowed");
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(dto);
    }

    // 500 - fallback
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleAllOthers(Exception ex) {
        ErrorResponseDTO dto = new ErrorResponseDTO()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message("Unexpected error occurred");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(dto);
    }

    private ErrorDetailDTO toErrorDetail(ConstraintViolation<?> v) {
        String field = v.getPropertyPath() != null ? v.getPropertyPath().toString() : null;
        return new ErrorDetailDTO()
                .field(field)
                .error(v.getMessage());
    }

}
