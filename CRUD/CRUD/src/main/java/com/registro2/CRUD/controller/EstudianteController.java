package com.registro2.CRUD.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.registro2.CRUD.model.Estudiante;
import com.registro2.CRUD.services.EstudianteService;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/estudiantes")
public class EstudianteController {

    @Autowired
    private EstudianteService estudianteService;

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
    public String listarEstudiantes(Model model, HttpSession session) {
        String redirect = verificarAcceso(session, false);
        if (redirect != null) return redirect;
        
        model.addAttribute("estudiantes", estudianteService.listarTodos());
        model.addAttribute("rol", session.getAttribute("rol"));
        return "estudiante-list";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevoEstudiante(Model model, HttpSession session) {
        String redirect = verificarAcceso(session, true); // Solo admin
        if (redirect != null) return redirect;
        
        model.addAttribute("estudiante", new Estudiante());
        return "estudiante-form";
    }

    @PostMapping
    public String guardarEstudiante(Estudiante estudiante, HttpSession session) {
        String redirect = verificarAcceso(session, true); // Solo admin
        if (redirect != null) return redirect;
        
        // Validación adicional
        if (estudiante.getNombre() == null || estudiante.getNombre().trim().isEmpty() ||
            estudiante.getApellido() == null || estudiante.getApellido().trim().isEmpty()) {
            return "redirect:/estudiantes/nuevo?error=campos-requeridos";
        }
        
        estudianteService.guardar(estudiante);
        return "redirect:/estudiantes";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditarEstudiante(@PathVariable Long id, Model model, HttpSession session) {
        String redirect = verificarAcceso(session, true); // Solo admin
        if (redirect != null) return redirect;
        
        if (id == null || id <= 0) {
            return "redirect:/estudiantes?error=id-invalido";
        }
        
        Estudiante estudiante = estudianteService.obtenerPorId(id);
        if (estudiante == null) {
            return "redirect:/estudiantes?error=estudiante-no-encontrado";
        }
        
        model.addAttribute("estudiante", estudiante);
        return "estudiante-form";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarEstudiante(@PathVariable Long id, HttpSession session) {
        String redirect = verificarAcceso(session, true); // Solo admin
        if (redirect != null) return redirect;
        
        if (id == null || id <= 0) {
            return "redirect:/estudiantes?error=id-invalido";
        }
        
        try {
            estudianteService.eliminar(id);
        } catch (Exception e) {
            return "redirect:/estudiantes?error=error-eliminar";
        }
        
        return "redirect:/estudiantes";
    }
}