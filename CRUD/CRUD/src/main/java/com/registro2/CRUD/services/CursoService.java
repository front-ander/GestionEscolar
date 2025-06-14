package com.registro2.CRUD.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.registro2.CRUD.model.Curso;
import com.registro2.CRUD.repository.CursoRepository;

@Service
public class CursoService {

    @Autowired
    private CursoRepository cursoRepository;

    public List<Curso> getAllCursos() {
        return cursoRepository.findAll();
    }

    public Optional<Curso> getCursoById(Long id) {
        return cursoRepository.findById(id);
    }
     
    public Curso saveCurso(Curso curso) {
        return cursoRepository.save(curso);
    }

    public void deleteCurso(Long id) {
        cursoRepository.deleteById(id);
    }
}