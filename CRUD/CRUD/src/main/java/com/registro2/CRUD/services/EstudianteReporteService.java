package com.registro2.CRUD.services;

import com.registro2.CRUD.dao.EstudianteDAO;
import com.registro2.CRUD.model.Estudiante;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EstudianteReporteService {
    @Autowired
    private EstudianteDAO estudianteDAO;

    public List<Estudiante> obtenerTodosEstudiantes() {
        return estudianteDAO.findAll();
    }

    public List<Estudiante> obtenerEstudiantesPaginados(int pagina, int tamano) {
        int offset = (pagina - 1) * tamano;
        return estudianteDAO.findAllPaged(offset, tamano);
    }

    public int contarEstudiantes() {
        return estudianteDAO.countAll();
    }
} 