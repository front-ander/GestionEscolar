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
import com.registro2.CRUD.services.CursoService;

@Controller
@RequestMapping("/cursos")
public class CursoController {

    @Autowired
    private CursoService cursoService;

    @GetMapping
    public String getAllCursos(Model model) {
        model.addAttribute("cursos", cursoService.getAllCursos());
        return "curso-list";
    }

    @GetMapping("/new")
    public String showCursoForm(Model model) {
        model.addAttribute("curso", new Curso());
        return "curso-form";
    }

    @PostMapping
    public String saveCurso(@ModelAttribute("curso") Curso curso) {
        cursoService.saveCurso(curso);
        return "redirect:/cursos";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Curso curso = cursoService.getCursoById(id).orElseThrow(() -> new IllegalArgumentException("Invalid curso Id:" + id));
        model.addAttribute("curso", curso);
        return "curso-form";
    }

    @PostMapping("/{id}")
    public String updateCurso(@PathVariable Long id, @ModelAttribute("curso") Curso curso) {
        curso.setId(id);
        cursoService.saveCurso(curso);
        return "redirect:/cursos";
    }

    @GetMapping("/delete/{id}")
    public String deleteCurso(@PathVariable("id") Long id) {
        cursoService.deleteCurso(id);
        return "redirect:/cursos";
    }
}