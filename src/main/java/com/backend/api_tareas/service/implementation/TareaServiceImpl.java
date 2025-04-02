package com.backend.api_tareas.service.implementation;

import com.backend.api_tareas.persistence.entity.Tarea;
import com.backend.api_tareas.persistence.repository.TareaRepository;
import com.backend.api_tareas.presentation.dto.TareaDTO;
import com.backend.api_tareas.service.exception.ResourceAlreadyExistsException;
import com.backend.api_tareas.service.exception.ResourceNotFoundException;
import com.backend.api_tareas.service.interfaces.ITareaService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TareaServiceImpl implements ITareaService {

    @Autowired
    private TareaRepository tareaRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public TareaDTO crearTarea(TareaDTO tareaDTO) {
        // Validación de datos de entrada
        if (tareaDTO.getTitulo() == null || tareaDTO.getTitulo().trim().isEmpty()) {
            throw new IllegalArgumentException("El título de la tarea no puede estar vacío");
        }

        if (tareaDTO.getDescripcion() != null && tareaDTO.getDescripcion().length() < 10) {
            throw new IllegalArgumentException("La descripción debe tener al menos 10 caracteres");
        }

        // Verificar si ya existe una tarea con el mismo título
        tareaRepository.findByTitulo(tareaDTO.getTitulo())
                .ifPresent(t -> {
                    throw new ResourceAlreadyExistsException("Ya existe una tarea con el título: " + tareaDTO.getTitulo());
                });

        Tarea tarea = new Tarea();
        tarea.setTitulo(tareaDTO.getTitulo());
        tarea.setDescripcion(tareaDTO.getDescripcion());
        tarea.setCompletada(tareaDTO.isCompletada());

        Tarea nuevaTarea = tareaRepository.save(tarea);
        return convertirATareaDTO(nuevaTarea);
    }


    private TareaDTO convertirATareaDTO(Tarea tarea) {
        return modelMapper.map(tarea, TareaDTO.class);
    }

    @Override
    public List<TareaDTO> obtenerTodasLasTareas() {
        List<Tarea> tareas = tareaRepository.findAll();
        return tareas.stream()
                .map(tarea -> modelMapper.map(tarea, TareaDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public TareaDTO obtenerTareaPorId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID de tarea no válido");
        }

        Tarea tarea = tareaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tarea no encontrada con id: " + id));

        return modelMapper.map(tarea, TareaDTO.class);
    }

    @Override
    public List<TareaDTO> obtenerTareasCompletadas() {
        List<Tarea> tareas = tareaRepository.findByCompletadaTrue();
        return tareas.stream()
                .map(tarea -> modelMapper.map(tarea, TareaDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<TareaDTO> obtenerTareasPendientes() {
        List<Tarea> tareas = tareaRepository.findByCompletadaFalse();
        return tareas.stream()
                .map(tarea -> modelMapper.map(tarea, TareaDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public TareaDTO marcarTareaComoCompletada(Long id) {
        Tarea tarea = tareaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tarea no encontrada con id: " + id));

        if (tarea.isCompletada()) {
            throw new IllegalStateException("La tarea ya está marcada como completada");
        }

        tarea.setCompletada(true);
        Tarea tareaActualizada = tareaRepository.save(tarea);
        return modelMapper.map(tareaActualizada, TareaDTO.class);
    }

    @Override
    public void eliminarTarea(Long id) {
        if (!tareaRepository.existsById(id)) {
            throw new ResourceNotFoundException("No se puede eliminar. Tarea no encontrada con id: " + id);
        }

        try {
            tareaRepository.deleteById(id);
        } catch (EmptyResultDataAccessException ex) {
            throw new ResourceNotFoundException("Tarea no encontrada con id: " + id);
        } catch (DataIntegrityViolationException ex) {
            throw new IllegalStateException("No se puede eliminar la tarea debido a restricciones de integridad");
        }
    }
}
