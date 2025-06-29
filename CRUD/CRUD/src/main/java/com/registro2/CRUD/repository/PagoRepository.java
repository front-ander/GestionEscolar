package com.registro2.CRUD.repository;

import com.registro2.CRUD.model.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface PagoRepository extends JpaRepository<Pago, Long> {
    
    // Buscar pagos por estudiante
    List<Pago> findByEstudianteIdOrderByFechaDesc(Long estudianteId);
    
    // Buscar pagos por estado
    List<Pago> findByEstadoOrderByFechaDesc(String estado);
    
    // Buscar pagos por mes y año
    List<Pago> findByMesAndAnioOrderByFechaDesc(String mes, int anio);
    
    // Buscar pagos por estudiante, mes y año
    List<Pago> findByEstudianteIdAndMesAndAnio(Long estudianteId, String mes, int anio);
    
    // Buscar pagos pendientes por estudiante
    List<Pago> findByEstudianteIdAndEstado(Long estudianteId, String estado);
    
    // Contar pagos por estado
    long countByEstado(String estado);
    
    // Contar pagos por estudiante y estado
    long countByEstudianteIdAndEstado(Long estudianteId, String estado);
    
    // Sumar montos de pagos por estudiante y estado
    @Query("SELECT SUM(p.monto) FROM Pago p WHERE p.estudiante.id = :estudianteId AND p.estado = :estado")
    Double sumMontoByEstudianteIdAndEstado(@Param("estudianteId") Long estudianteId, @Param("estado") String estado);
    
    // Sumar montos de pagos por mes y año
    @Query("SELECT SUM(p.monto) FROM Pago p WHERE p.mes = :mes AND p.anio = :anio AND p.estado = 'PAGADO'")
    Double sumMontoByMesAndAnio(@Param("mes") String mes, @Param("anio") int anio);
} 