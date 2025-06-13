package com.registro2.CRUD.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.registro2.CRUD.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Usuario findByUsername(String username);
    Usuario findByUsernameAndPassword(String username, String password);
}
