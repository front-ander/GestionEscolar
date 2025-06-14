package com.registro2.CRUD.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.registro2.CRUD.model.Nota;
import com.registro2.CRUD.repository.NotaRepository;

@Service
public class NotaService {

    @Autowired
    private NotaRepository notaRepository;

    public List<Nota> getAllNotas() {
        return notaRepository.findAll();
    }

    public Optional<Nota> getNotaById(Long id) {
        return notaRepository.findById(id);
    }

    public Nota saveNota(Nota nota) {
        return notaRepository.save(nota);
    }

    public void deleteNota(Long id) {
        notaRepository.deleteById(id);
    }
}