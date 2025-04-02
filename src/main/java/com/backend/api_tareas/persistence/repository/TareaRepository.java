package com.backend.api_tareas.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.backend.api_tareas.persistence.entity.Tarea;

import java.util.List;
import java.util.Optional;

@Repository
public interface TareaRepository extends JpaRepository<Tarea, Long> {
    List<Tarea> findByCompletadaTrue();
    List<Tarea> findByCompletadaFalse();
    // Para verificar existencia por título (usado en crearTarea)
    Optional<Tarea> findByTitulo(String titulo);
}
