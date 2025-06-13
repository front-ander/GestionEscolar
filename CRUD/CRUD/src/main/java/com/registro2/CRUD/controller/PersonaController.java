/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.registro2.CRUD.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.registro2.CRUD.model.Persona;
import com.registro2.CRUD.services.PersonaService;
import com.registro2.CRUD.services.UsuarioService;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/personas")
public class PersonaController {

    @Autowired
    private PersonaService personaService;
    
    @Autowired
    private UsuarioService usuarioService;

    @PostConstruct
    public void init() {
        usuarioService.crearUsuariosPorDefecto();
    }

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
    public String listarPersonas(Model model, HttpSession session) {
        String redirect = verificarAcceso(session, false);
        if (redirect != null) return redirect;
        
        model.addAttribute("personas", personaService.listarTodas());
        model.addAttribute("rol", session.getAttribute("rol"));
        return "persona-list";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevaPersona(Model model, HttpSession session) {
        String redirect = verificarAcceso(session, true); // Solo admin
        if (redirect != null) return redirect;
        
        model.addAttribute("persona", new Persona());
        return "persona-form";
    }

    @PostMapping
    public String guardarPersona(Persona persona, HttpSession session) {
        String redirect = verificarAcceso(session, true); // Solo admin
        if (redirect != null) return redirect;
        
        // Validación adicional
        if (persona.getNombre() == null || persona.getNombre().trim().isEmpty() ||
            persona.getApellido() == null || persona.getApellido().trim().isEmpty()) {
            return "redirect:/personas/nuevo?error=campos-requeridos";
        }
        
        personaService.guardar(persona);
        return "redirect:/personas";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditarPersona(@PathVariable Long id, Model model, HttpSession session) {
        String redirect = verificarAcceso(session, true); // Solo admin
        if (redirect != null) return redirect;
        
        // Validar que el ID sea válido
        if (id == null || id <= 0) {
            return "redirect:/personas?error=id-invalido";
        }
        
        Persona persona = personaService.obtenerPorId(id);
        if (persona == null) {
            return "redirect:/personas?error=persona-no-encontrada";
        }
        
        model.addAttribute("persona", persona);
        return "persona-form";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarPersona(@PathVariable Long id, HttpSession session) {
        String redirect = verificarAcceso(session, true); // Solo admin
        if (redirect != null) return redirect;
        
        // Validar que el ID sea válido
        if (id == null || id <= 0) {
            return "redirect:/personas?error=id-invalido";
        }
        
        try {
            personaService.eliminar(id);
        } catch (Exception e) {
            return "redirect:/personas?error=error-eliminar";
        }
        
        return "redirect:/personas";
    }
}