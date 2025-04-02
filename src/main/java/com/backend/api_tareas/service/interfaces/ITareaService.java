package com.backend.api_tareas.service.interfaces;

import com.backend.api_tareas.presentation.dto.TareaDTO;

import java.util.List;

public interface ITareaService {
    TareaDTO crearTarea(TareaDTO tareaDTO);
    List<TareaDTO> obtenerTodasLasTareas();
    TareaDTO obtenerTareaPorId(Long id);
    List<TareaDTO> obtenerTareasCompletadas();
    List<TareaDTO> obtenerTareasPendientes();
    TareaDTO marcarTareaComoCompletada(Long id);
    void eliminarTarea(Long id);
}
