package com.pokedex.pokedex.controller.handler;

import com.pokedex.pokedex.controller.dto.response.ApiError;
import com.pokedex.pokedex.core.exception.BusinessException;
import com.pokedex.pokedex.core.exception.DuplicateResourceException;
import com.pokedex.pokedex.core.exception.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(ResourceNotFoundException ex,
                                                   HttpServletRequest req) {
        log.warn("Recurso no encontrado: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(buildError(404, ex.getErrorCode(), ex.getMessage(),
                        req.getRequestURI(), List.of()));
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ApiError> handleDuplicate(DuplicateResourceException ex,
                                                    HttpServletRequest req) {
        log.warn("Recurso duplicado: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(buildError(409, ex.getErrorCode(), ex.getMessage(),
                        req.getRequestURI(), List.of()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex,
                                                     HttpServletRequest req) {
        List<ApiError.FieldError> errores = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> new ApiError.FieldError(e.getField(), e.getDefaultMessage()))
                .toList();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(buildError(400, "VALIDATION_ERROR",
                        "Error de validación en los datos de entrada",
                        req.getRequestURI(), errores));
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiError> handleBusiness(BusinessException ex, HttpServletRequest req) {
        log.warn("Error de negocio: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(buildError(401, ex.getErrorCode(), ex.getMessage(),
                        req.getRequestURI(), List.of()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneric(Exception ex, HttpServletRequest req) {
        log.error("Error inesperado", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(buildError(500, "INTERNAL_ERROR",
                        "Ocurrió un error inesperado en el servidor",
                        req.getRequestURI(), List.of()));
    }

    private ApiError buildError(int status, String code, String msg,
                                String path, List<ApiError.FieldError> errors) {
        return new ApiError(status, code, msg, path, LocalDateTime.now(), errors);
    }
}