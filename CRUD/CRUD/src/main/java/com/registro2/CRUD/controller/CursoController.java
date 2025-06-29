package com.registro2.CRUD.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.registro2.CRUD.model.Curso;
import com.registro2.CRUD.model.Usuario;
import com.registro2.CRUD.services.CursoService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/cursos")
public class CursoController {

    @Autowired
    private CursoService cursoService;

    @GetMapping
    public String getAllCursos(Model model, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/login";
        }
        
        model.addAttribute("cursos", cursoService.getAllCursos());
        model.addAttribute("rol", usuario.getRol());
        return "curso-list";
    }

    @GetMapping("/new")
    public String showCursoForm(Model model, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/login";
        }
        
        model.addAttribute("curso", new Curso());
        model.addAttribute("rol", usuario.getRol());
        return "curso-form";
    }

    @PostMapping
    public String saveCurso(@ModelAttribute("curso") Curso curso, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/login";
        }
        
        cursoService.saveCurso(curso);
        return "redirect:/cursos?success=curso-registrado";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/login";
        }
        
        Curso curso = cursoService.getCursoById(id).orElseThrow(() -> new IllegalArgumentException("Invalid curso Id:" + id));
        model.addAttribute("curso", curso);
        model.addAttribute("rol", usuario.getRol());
        return "curso-form";
    }

    @PostMapping("/{id}")
    public String updateCurso(@PathVariable Long id, @ModelAttribute("curso") Curso curso, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/login";
        }
        
        curso.setId(id);
        cursoService.saveCurso(curso);
        return "redirect:/cursos?success=curso-actualizado";
    }

    @GetMapping("/delete/{id}")
    public String deleteCurso(@PathVariable("id") Long id, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/login";
        }
        
        cursoService.deleteCurso(id);
        return "redirect:/cursos?success=curso-eliminado";
    }
}