package com.registro2.CRUD.services;

import com.registro2.CRUD.model.Asistencia;
import com.registro2.CRUD.model.Estudiante;
import com.registro2.CRUD.model.Profesor;
import com.registro2.CRUD.repository.AsistenciaRepository;
import com.registro2.CRUD.repository.EstudianteRepository;
import com.registro2.CRUD.repository.ProfesorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class AsistenciaService {

    @Autowired
    private AsistenciaRepository asistenciaRepository;
    
    @Autowired
    private EstudianteRepository estudianteRepository;
    
    @Autowired
    private ProfesorRepository profesorRepository;

    // CRUD básico
    public List<Asistencia> listarTodas() {
        return asistenciaRepository.findAll();
    }

    public Asistencia guardar(Asistencia asistencia) {
        return asistenciaRepository.save(asistencia);
    }

    public Asistencia obtenerPorId(Long id) {
        return asistenciaRepository.findById(id).orElse(null);
    }

    public void eliminar(Long id) {
        asistenciaRepository.deleteById(id);
    }
    
    // Métodos específicos para estudiantes
    public List<Asistencia> listarPorEstudiante(Long estudianteId) {
        return asistenciaRepository.findByEstudianteIdOrderByFechaDesc(estudianteId);
    }
    
    public Asistencia registrarAsistenciaEstudiante(Long estudianteId, Date fecha, String horaEntrada, 
                                                   String horaSalida, String estado, String observaciones) {
        Optional<Estudiante> estudianteOpt = estudianteRepository.findById(estudianteId);
        if (estudianteOpt.isPresent()) {
            Asistencia asistencia = new Asistencia();
            asistencia.setEstudiante(estudianteOpt.get());
            asistencia.setFecha(fecha);
            asistencia.setHoraEntrada(horaEntrada);
            asistencia.setHoraSalida(horaSalida);
            asistencia.setEstado(estado);
            asistencia.setObservaciones(observaciones);
            asistencia.setTipo("ESTUDIANTE");
            return asistenciaRepository.save(asistencia);
        }
        return null;
    }
    
    // Métodos específicos para profesores
    public List<Asistencia> listarPorProfesor(Long profesorId) {
        return asistenciaRepository.findByProfesorIdOrderByFechaDesc(profesorId);
    }
    
    public Asistencia registrarAsistenciaProfesor(Long profesorId, Date fecha, String horaEntrada, 
                                                 String horaSalida, String estado, String observaciones) {
        Optional<Profesor> profesorOpt = profesorRepository.findById(profesorId);
        if (profesorOpt.isPresent()) {
            Asistencia asistencia = new Asistencia();
            asistencia.setProfesor(profesorOpt.get());
            asistencia.setFecha(fecha);
            asistencia.setHoraEntrada(horaEntrada);
            asistencia.setHoraSalida(horaSalida);
            asistencia.setEstado(estado);
            asistencia.setObservaciones(observaciones);
            asistencia.setTipo("PROFESOR");
            return asistenciaRepository.save(asistencia);
        }
        return null;
    }
    
    // Métodos de consulta
    public List<Asistencia> listarPorFecha(Date fecha) {
        return asistenciaRepository.findByFechaOrderByHoraEntrada(fecha);
    }
    
    public List<Asistencia> listarPorTipo(String tipo) {
        return asistenciaRepository.findByTipoOrderByFechaDesc(tipo);
    }
    
    // Métodos de estadísticas
    public long contarPorEstado(String estado) {
        return asistenciaRepository.countByEstado(estado);
    }
    
    public long contarPorEstudianteYEstado(Long estudianteId, String estado) {
        return asistenciaRepository.countByEstudianteIdAndEstado(estudianteId, estado);
    }
    
    public long contarPorProfesorYEstado(Long profesorId, String estado) {
        return asistenciaRepository.countByProfesorIdAndEstado(profesorId, estado);
    }
    
    // Verificar si ya existe asistencia para la fecha
    public boolean existeAsistenciaEstudiante(Long estudianteId, Date fecha) {
        List<Asistencia> asistencias = asistenciaRepository.findByEstudianteIdAndFecha(estudianteId, fecha);
        return !asistencias.isEmpty();
    }
    
    public boolean existeAsistenciaProfesor(Long profesorId, Date fecha) {
        List<Asistencia> asistencias = asistenciaRepository.findByProfesorIdAndFecha(profesorId, fecha);
        return !asistencias.isEmpty();
    }
    
    // Contar totales
    public long contarTodas() {
        return asistenciaRepository.count();
    }
} 