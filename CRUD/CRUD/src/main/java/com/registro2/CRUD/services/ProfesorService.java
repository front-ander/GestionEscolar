package com.registro2.CRUD.services;

import com.registro2.CRUD.model.Profesor;
import com.registro2.CRUD.repository.ProfesorRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProfesorService {

    @Autowired
    private ProfesorRepository profesorRepository;

    public List<Profesor> listarTodos() {
        return profesorRepository.findAll();
    }

    public Profesor guardar(Profesor profesor) {
        return profesorRepository.save(profesor);
    }

    public Profesor obtenerPorId(Long id) {
        return profesorRepository.findById(id).orElse(null);
    }

    public void eliminar(Long id) {
        profesorRepository.deleteById(id);
    }
    
    // Nuevo método para contar
    public long contarTodos() {
        return profesorRepository.count();
    }
}