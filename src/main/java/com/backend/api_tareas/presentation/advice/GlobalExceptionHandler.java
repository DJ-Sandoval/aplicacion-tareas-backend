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

    // Manejo de validaciones
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.toList());

        return new ErrorResponse(
                "VALIDATION_ERROR",
                "Error de validación en los datos de entrada",
                HttpStatus.BAD_REQUEST.value(),
                errors
        );
    }

    // Manejo de recurso no encontrado
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleResourceNotFound(ResourceNotFoundException ex) {
        return new ErrorResponse(
                "NOT_FOUND",
                ex.getMessage(),
                HttpStatus.NOT_FOUND.value()
        );
    }

    // Manejo de conflictos (por ejemplo, al crear un recurso que ya existe)
    @ExceptionHandler(ResourceAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleResourceAlreadyExists(ResourceAlreadyExistsException ex) {
        return new ErrorResponse(
                "CONFLICT",
                ex.getMessage(),
                HttpStatus.CONFLICT.value()
        );
    }

    // Manejo de acceso denegado
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleAccessDenied(AccessDeniedException ex) {
        return new ErrorResponse(
                "FORBIDDEN",
                "No tienes permiso para acceder a este recurso",
                HttpStatus.FORBIDDEN.value()
        );
    }

    // Manejo de excepciones generales
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleAllExceptions(Exception ex) {
        return new ErrorResponse(
                "INTERNAL_SERVER_ERROR",
                "Ocurrió un error inesperado",
                HttpStatus.INTERNAL_SERVER_ERROR.value()
        );
    }
}
