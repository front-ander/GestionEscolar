package com.registro2.CRUD.services;

import com.registro2.CRUD.model.Estudiante;
import com.registro2.CRUD.repository.EstudianteRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EstudianteService {

    @Autowired
    private EstudianteRepository estudianteRepository;

    public List<Estudiante> listarTodos() {
        return estudianteRepository.findAll();
    }

    public Estudiante guardar(Estudiante estudiante) {
        return estudianteRepository.save(estudiante);
    }

    public Estudiante obtenerPorId(Long id) {
        return estudianteRepository.findById(id).orElse(null);
    }

    public void eliminar(Long id) {
        estudianteRepository.deleteById(id);
    }
    
    // Nuevo método para contar
    public long contarTodos() {
        return estudianteRepository.count();
    }
}