package com.registro2.CRUD.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.registro2.CRUD.model.Curso;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Long> {
}