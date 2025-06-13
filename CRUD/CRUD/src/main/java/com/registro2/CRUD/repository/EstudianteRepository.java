/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.registro2.CRUD.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.registro2.CRUD.model.Estudiante;

/**
 *
 * @author Anderson
 */
public interface EstudianteRepository extends JpaRepository<Estudiante, Long> {
    
}