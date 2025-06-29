package com.registro2.CRUD.repository;

import com.registro2.CRUD.model.Asistencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Date;
import java.util.List;

public interface AsistenciaRepository extends JpaRepository<Asistencia, Long> {
    
    // Buscar asistencias por estudiante
    List<Asistencia> findByEstudianteIdOrderByFechaDesc(Long estudianteId);
    
    // Buscar asistencias por profesor
    List<Asistencia> findByProfesorIdOrderByFechaDesc(Long profesorId);
    
    // Buscar asistencias por fecha
    List<Asistencia> findByFechaOrderByHoraEntrada(Date fecha);
    
    // Buscar asistencias por tipo (ESTUDIANTE o PROFESOR)
    List<Asistencia> findByTipoOrderByFechaDesc(String tipo);
    
    // Buscar asistencias por estudiante y fecha
    List<Asistencia> findByEstudianteIdAndFecha(Long estudianteId, Date fecha);
    
    // Buscar asistencias por profesor y fecha
    List<Asistencia> findByProfesorIdAndFecha(Long profesorId, Date fecha);
    
    // Contar asistencias por estado
    long countByEstado(String estado);
    
    // Contar asistencias por estudiante y estado
    long countByEstudianteIdAndEstado(Long estudianteId, String estado);
    
    // Contar asistencias por profesor y estado
    long countByProfesorIdAndEstado(Long profesorId, String estado);
} 