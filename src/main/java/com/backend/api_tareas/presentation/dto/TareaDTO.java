package com.backend.api_tareas.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TareaDTO {
    private Long id;
    private String titulo;
    private String descripcion;
    private boolean completada;
}
