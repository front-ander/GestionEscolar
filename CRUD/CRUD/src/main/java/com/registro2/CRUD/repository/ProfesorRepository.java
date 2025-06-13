package com.registro2.CRUD.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.registro2.CRUD.model.Profesor;

public interface ProfesorRepository extends JpaRepository<Profesor, Long> {
    
}