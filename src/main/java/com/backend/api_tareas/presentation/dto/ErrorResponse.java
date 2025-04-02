package com.backend.api_tareas.presentation.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
    private String error;
    private String message;
    private int status;
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();
    private List<String> details;

    // Constructor simplificado para los casos básicos
    public ErrorResponse(String error, String message, int status) {
        this(error, message, status, null);
    }

    public ErrorResponse(String error, String message, int status, List<String> details) {
        this.error = error;
        this.message = message;
        this.status = status;
        this.details = details;
        // timestamp se asigna automáticamente por @Builder.Default
    }
}
