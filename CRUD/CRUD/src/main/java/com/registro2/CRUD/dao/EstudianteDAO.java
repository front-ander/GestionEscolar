package com.registro2.CRUD.dao;

import com.registro2.CRUD.model.Estudiante;
import java.util.List;

public interface EstudianteDAO {
    List<Estudiante> findAll();
    Estudiante findById(Long id);
    void save(Estudiante estudiante);
    void update(Estudiante estudiante);
    void delete(Long id);
    List<Estudiante> findAllPaged(int offset, int limit);
    int countAll();
} 