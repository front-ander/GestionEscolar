package com.registro2.CRUD.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.registro2.CRUD.model.Profesor;
import com.registro2.CRUD.services.ProfesorService;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/profesores")
public class ProfesorController {

    @Autowired
    private ProfesorService profesorService;

    // Método para verificar sesión y rol
    private String verificarAcceso(HttpSession session, boolean requiereAdmin) {
        String usuario = (String) session.getAttribute("username");
        String rol = (String) session.getAttribute("rol");
        
        if (usuario == null || rol == null) {
            return "redirect:/login";
        }
        
        if (requiereAdmin && !"ADMIN".equals(rol)) {
            return "redirect:/dashboard-secretario?error=sin-permisos";
        }
        
        return null; // Acceso permitido
    }

    @GetMapping
    public String listarProfesores(Model model, HttpSession session) {
        String redirect = verificarAcceso(session, false);
        if (redirect != null) return redirect;
        
        model.addAttribute("profesores", profesorService.listarTodos());
        model.addAttribute("rol", session.getAttribute("rol"));
        return "profesor-list";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevoProfesor(Model model, HttpSession session) {
        String redirect = verificarAcceso(session, true); // Solo admin
        if (redirect != null) return redirect;
        
        model.addAttribute("profesor", new Profesor());
        return "profesor-form";
    }

    @PostMapping
    public String guardarProfesor(Profesor profesor, HttpSession session) {
        String redirect = verificarAcceso(session, true); // Solo admin
        if (redirect != null) return redirect;
        
        // Validación adicional
        if (profesor.getNombre() == null || profesor.getNombre().trim().isEmpty() ||
            profesor.getApellido() == null || profesor.getApellido().trim().isEmpty()) {
            return "redirect:/profesores/nuevo?error=campos-requeridos";
        }
        
        profesorService.guardar(profesor);
        return "redirect:/profesores";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditarProfesor(@PathVariable Long id, Model model, HttpSession session) {
        String redirect = verificarAcceso(session, true); // Solo admin
        if (redirect != null) return redirect;
        
        if (id == null || id <= 0) {
            return "redirect:/profesores?error=id-invalido";
        }
        
        Profesor profesor = profesorService.obtenerPorId(id);
        if (profesor == null) {
            return "redirect:/profesores?error=profesor-no-encontrado";
        }
        
        model.addAttribute("profesor", profesor);
        return "profesor-form";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarProfesor(@PathVariable Long id, HttpSession session) {
        String redirect = verificarAcceso(session, true); // Solo admin
        if (redirect != null) return redirect;
        
        if (id == null || id <= 0) {
            return "redirect:/profesores?error=id-invalido";
        }
        
        try {
            profesorService.eliminar(id);
        } catch (Exception e) {
            return "redirect:/profesores?error=error-eliminar";
        }
        
        return "redirect:/profesores";
    }
}