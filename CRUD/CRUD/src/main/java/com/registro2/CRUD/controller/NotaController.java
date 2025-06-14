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
import com.registro2.CRUD.services.CursoService;
import com.registro2.CRUD.services.EstudianteService;
import com.registro2.CRUD.services.NotaService;

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
    public String getAllNotas(Model model) {
        model.addAttribute("notas", notaService.getAllNotas());
        return "nota-list";
    }

    @GetMapping("/new")
    public String showNotaForm(Model model) {
        model.addAttribute("nota", new Nota());
        model.addAttribute("estudiantes", estudianteService.getAllEstudiantes());
        model.addAttribute("cursos", cursoService.getAllCursos());
        return "nota-form";
    }

    @PostMapping
    public String saveNota(@ModelAttribute("nota") Nota nota) {
        notaService.saveNota(nota);
        return "redirect:/notas";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Nota nota = notaService.getNotaById(id).orElseThrow(() -> new IllegalArgumentException("Invalid nota Id:" + id));
        model.addAttribute("nota", nota);
        model.addAttribute("estudiantes", estudianteService.getAllEstudiantes());
        model.addAttribute("cursos", cursoService.getAllCursos());
        return "nota-form";
    }

    @PostMapping("/{id}")
    public String updateNota(@PathVariable Long id, @ModelAttribute("nota") Nota nota) {
        nota.setId(id);
        notaService.saveNota(nota);
        return "redirect:/notas";
    }

    @GetMapping("/delete/{id}")
    public String deleteNota(@PathVariable("id") Long id) {
        notaService.deleteNota(id);
        return "redirect:/notas";
    }
}