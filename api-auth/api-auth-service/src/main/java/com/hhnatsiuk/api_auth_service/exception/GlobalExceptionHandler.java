package com.hhnatsiuk.api_auth_service.exception;

import com.hhnatsiuk.api_auth_if.model.generated.ErrorDetailDTO;
import com.hhnatsiuk.api_auth_if.model.generated.ErrorResponseDTO;
//import com.hhnatsiuk.api_auth_if.model.generated.ValidationErrorResponseAllOfDetailsDTO;
import com.hhnatsiuk.api_auth_if.model.generated.ValidationErrorResponseDTO;
import com.hhnatsiuk.api_auth_service.exception.global.ApiException;
import com.hhnatsiuk.api_auth_service.exception.global.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.naming.AuthenticationException;
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
                        .error(fe.getDefaultMessage())
                )
                .collect(Collectors.toList());

        ValidationErrorResponseDTO dto = new ValidationErrorResponseDTO()
                .code(HttpStatus.BAD_REQUEST.value())
                .message("Validation failed for one or more fields")
                .details(details);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(dto);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponseDTO> handleAuthenticationException(AuthenticationException ex) {
        ErrorResponseDTO dto = new ErrorResponseDTO()
                .code(HttpStatus.UNAUTHORIZED.value())
                .message("Authentication failed: " + ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(dto);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponseDTO> handleMalformedJson(HttpMessageNotReadableException ex) {
        ErrorResponseDTO dto = new ErrorResponseDTO()
                .code(HttpStatus.BAD_REQUEST.value())
                .message("Malformed JSON request: " + ex.getLocalizedMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(dto);
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponseDTO> handleApiException(ApiException ex) {
        HttpStatus status = HttpStatus.resolve(ex.getCode());
        if (status == null) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        ErrorResponseDTO dto = new ErrorResponseDTO()
                .code(status.value())
                .message(ex.getMessage());
        return new ResponseEntity<>(dto, status);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ValidationErrorResponseDTO> handleValidationException(ValidationException ex) {
        HttpStatus status = HttpStatus.resolve(ex.getCode());
        if (status == null) {
            status = HttpStatus.UNPROCESSABLE_ENTITY;
        }

        ValidationErrorResponseDTO errorResponse = new ValidationErrorResponseDTO();
        errorResponse.setCode(status.value());
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setDetails(ex.getDetails());

        return new ResponseEntity<>(errorResponse, status);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleAllOthers(Exception ex) {
        ErrorResponseDTO dto = new ErrorResponseDTO()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message("Unexpected error occurred");
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(dto);
    }
}
