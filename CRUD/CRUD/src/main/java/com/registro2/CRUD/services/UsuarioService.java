package com.registro2.CRUD.services;

import com.registro2.CRUD.model.Usuario;
import com.registro2.CRUD.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario autenticar(String username, String password) {
        try {
            return usuarioRepository.findByUsernameAndPassword(username, password);
        } catch (Exception e) {
            System.err.println("Error en autenticación: " + e.getMessage());
            return null;
        }
    }

    public Usuario buscarPorUsername(String username) {
        try {
            return usuarioRepository.findByUsername(username);
        } catch (Exception e) {
            System.err.println("Error al buscar usuario: " + e.getMessage());
            return null;
        }
    }

    public Usuario guardar(Usuario usuario) {
        try {
            return usuarioRepository.save(usuario);
        } catch (Exception e) {
            System.err.println("Error al guardar usuario: " + e.getMessage());
            return null;
        }
    }

    // Método para crear usuarios por defecto
    public void crearUsuariosPorDefecto() {
        try {
            if (usuarioRepository.findByUsername("admin") == null) {
                Usuario admin = new Usuario("admin", "admin123", "ADMIN");
                usuarioRepository.save(admin);
                System.out.println("Usuario admin creado");
            }
            
            if (usuarioRepository.findByUsername("secretario") == null) {
                Usuario secretario = new Usuario("secretario", "secretario123", "SECRETARIO");
                usuarioRepository.save(secretario);
                System.out.println("Usuario secretario creado");
            }
        } catch (Exception e) {
            System.err.println("Error al crear usuarios por defecto: " + e.getMessage());
        }
    }
}