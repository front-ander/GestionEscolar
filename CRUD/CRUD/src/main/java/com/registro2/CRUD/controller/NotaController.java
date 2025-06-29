package com.registro2.CRUD.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.registro2.CRUD.model.Nota;
import com.registro2.CRUD.model.Usuario;
import com.registro2.CRUD.services.CursoService;
import com.registro2.CRUD.services.EstudianteService;
import com.registro2.CRUD.services.NotaService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/notas")
public class NotaController {

    @Autowired
    private NotaService notaService;

    @Autowired
    private EstudianteService estudianteService;

    @Autowired
    private CursoService cursoService;

    @GetMapping
    public String getAllNotas(Model model, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/login";
        }
        
        model.addAttribute("notas", notaService.getAllNotas());
        model.addAttribute("rol", usuario.getRol());
        return "nota-list";
    }

    @GetMapping("/new")
    public String showNotaForm(Model model, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/login";
        }
        
        model.addAttribute("nota", new Nota());
        model.addAttribute("estudiantes", estudianteService.getAllEstudiantes());
        model.addAttribute("cursos", cursoService.getAllCursos());
        model.addAttribute("rol", usuario.getRol());
        return "nota-form";
    }

    @PostMapping
    public String saveNota(@ModelAttribute("nota") Nota nota, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/login";
        }
        
        notaService.saveNota(nota);
        return "redirect:/notas?success=nota-registrada";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/login";
        }
        
        Nota nota = notaService.getNotaById(id).orElseThrow(() -> new IllegalArgumentException("Invalid nota Id:" + id));
        model.addAttribute("nota", nota);
        model.addAttribute("estudiantes", estudianteService.getAllEstudiantes());
        model.addAttribute("cursos", cursoService.getAllCursos());
        model.addAttribute("rol", usuario.getRol());
        return "nota-form";
    }

    @PostMapping("/{id}")
    public String updateNota(@PathVariable Long id, @ModelAttribute("nota") Nota nota, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/login";
        }
        
        nota.setId(id);
        notaService.saveNota(nota);
        return "redirect:/notas?success=nota-actualizada";
    }

    @GetMapping("/delete/{id}")
    public String deleteNota(@PathVariable("id") Long id, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/login";
        }
        
        notaService.deleteNota(id);
        return "redirect:/notas?success=nota-eliminada";
    }
}