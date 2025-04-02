package com.backend.api_tareas.presentation.advice;

import com.backend.api_tareas.presentation.dto.ErrorResponse;
import com.backend.api_tareas.service.exception.ResourceAlreadyExistsException;
import com.backend.api_tareas.service.exception.ResourceNotFoundException;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
@Tag(name = "Gestión de Errores", description = "Manejo centralizado de excepciones")
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.toList());

        return ErrorResponse.builder()
                .error("VALIDATION_ERROR")
                .message("Error de validación en los datos de entrada")
                .status(HttpStatus.BAD_REQUEST.value())
                .details(errors)
                .build();
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleResourceNotFound(ResourceNotFoundException ex) {
        return new ErrorResponse(
                "NOT_FOUND",
                ex.getMessage(),
                HttpStatus.NOT_FOUND.value()
        );
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleResourceAlreadyExists(ResourceAlreadyExistsException ex) {
        return new ErrorResponse(
                "CONFLICT",
                ex.getMessage(),
                HttpStatus.CONFLICT.value()
        );
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleAccessDenied(AccessDeniedException ex) {
        return new ErrorResponse(
                "FORBIDDEN",
                "No tienes permiso para acceder a este recurso",
                HttpStatus.FORBIDDEN.value()
        );
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleAllExceptions(Exception ex) {
        return ErrorResponse.builder()
                .error("INTERNAL_SERVER_ERROR")
                .message("Ocurrió un error inesperado")
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .details(List.of(ex.getMessage())) // Opcional: incluir el mensaje de error
                .build();
    }
}