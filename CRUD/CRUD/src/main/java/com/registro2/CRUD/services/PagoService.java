package com.registro2.CRUD.services;

import com.registro2.CRUD.model.Pago;
import com.registro2.CRUD.model.Estudiante;
import com.registro2.CRUD.repository.PagoRepository;
import com.registro2.CRUD.repository.EstudianteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PagoService {

    @Autowired
    private PagoRepository pagoRepository;
    
    @Autowired
    private EstudianteRepository estudianteRepository;

    // CRUD básico
    public List<Pago> listarTodos() {
        return pagoRepository.findAll();
    }

    public Pago guardar(Pago pago) {
        return pagoRepository.save(pago);
    }

    public Pago obtenerPorId(Long id) {
        return pagoRepository.findById(id).orElse(null);
    }

    public void eliminar(Long id) {
        pagoRepository.deleteById(id);
    }
    
    // Métodos específicos para estudiantes
    public List<Pago> listarPorEstudiante(Long estudianteId) {
        return pagoRepository.findByEstudianteIdOrderByFechaDesc(estudianteId);
    }
    
    public Pago registrarPagoEstudiante(Long estudianteId, Date fecha, double monto, 
                                       String metodoPago, String mes, int anio, String observaciones) {
        Optional<Estudiante> estudianteOpt = estudianteRepository.findById(estudianteId);
        if (estudianteOpt.isPresent()) {
            Pago pago = new Pago();
            pago.setEstudiante(estudianteOpt.get());
            pago.setFecha(fecha);
            pago.setMonto(monto);
            pago.setMetodoPago(metodoPago);
            pago.setEstado("PAGADO");
            pago.setMes(mes);
            pago.setAnio(anio);
            pago.setObservaciones(observaciones);
            return pagoRepository.save(pago);
        }
        return null;
    }
    
    // Crear pagos pendientes para un estudiante
    public void crearPagosPendientesEstudiante(Long estudianteId, double montoMensual, int anio) {
        Optional<Estudiante> estudianteOpt = estudianteRepository.findById(estudianteId);
        if (estudianteOpt.isPresent()) {
            String[] meses = {"ENERO", "FEBRERO", "MARZO", "ABRIL", "MAYO", "JUNIO", 
                             "JULIO", "AGOSTO", "SEPTIEMBRE", "OCTUBRE", "NOVIEMBRE", "DICIEMBRE"};
            
            for (String mes : meses) {
                // Verificar si ya existe un pago para este mes y año
                List<Pago> pagosExistentes = pagoRepository.findByEstudianteIdAndMesAndAnio(estudianteId, mes, anio);
                if (pagosExistentes.isEmpty()) {
                    Pago pago = new Pago();
                    pago.setEstudiante(estudianteOpt.get());
                    pago.setFecha(new Date());
                    pago.setMonto(montoMensual);
                    pago.setMetodoPago("PENDIENTE");
                    pago.setEstado("PENDIENTE");
                    pago.setMes(mes);
                    pago.setAnio(anio);
                    pago.setObservaciones("Pago mensual pendiente");
                    pagoRepository.save(pago);
                }
            }
        }
    }
    
    // Métodos de consulta
    public List<Pago> listarPorEstado(String estado) {
        return pagoRepository.findByEstadoOrderByFechaDesc(estado);
    }
    
    public List<Pago> listarPorMesYAnio(String mes, int anio) {
        return pagoRepository.findByMesAndAnioOrderByFechaDesc(mes, anio);
    }
    
    public List<Pago> listarPendientesPorEstudiante(Long estudianteId) {
        return pagoRepository.findByEstudianteIdAndEstado(estudianteId, "PENDIENTE");
    }
    
    // Métodos de estadísticas
    public long contarPorEstado(String estado) {
        return pagoRepository.countByEstado(estado);
    }
    
    public long contarPorEstudianteYEstado(Long estudianteId, String estado) {
        return pagoRepository.countByEstudianteIdAndEstado(estudianteId, estado);
    }
    
    public Double sumarMontoPorEstudianteYEstado(Long estudianteId, String estado) {
        return pagoRepository.sumMontoByEstudianteIdAndEstado(estudianteId, estado);
    }
    
    public Double sumarMontoPorMesYAnio(String mes, int anio) {
        return pagoRepository.sumMontoByMesAndAnio(mes, anio);
    }
    
    // Verificar si ya existe pago para el mes
    public boolean existePagoEstudiante(Long estudianteId, String mes, int anio) {
        List<Pago> pagos = pagoRepository.findByEstudianteIdAndMesAndAnio(estudianteId, mes, anio);
        return !pagos.isEmpty();
    }
    
    // Obtener total de pagos por estudiante
    public double obtenerTotalPagosEstudiante(Long estudianteId) {
        Double total = pagoRepository.sumMontoByEstudianteIdAndEstado(estudianteId, "PAGADO");
        return total != null ? total : 0.0;
    }
    
    // Obtener total de pagos pendientes por estudiante
    public double obtenerTotalPendientesEstudiante(Long estudianteId) {
        Double total = pagoRepository.sumMontoByEstudianteIdAndEstado(estudianteId, "PENDIENTE");
        return total != null ? total : 0.0;
    }
    
    // Contar totales
    public long contarTodos() {
        return pagoRepository.count();
    }
}
