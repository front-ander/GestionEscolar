package com.registro2.CRUD.controller;

import com.registro2.CRUD.model.Asistencia;
import com.registro2.CRUD.model.Estudiante;
import com.registro2.CRUD.model.Profesor;
import com.registro2.CRUD.services.AsistenciaService;
import com.registro2.CRUD.services.EstudianteService;
import com.registro2.CRUD.services.ProfesorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/asistencias")
public class AsistenciaController {

    @Autowired
    private AsistenciaService asistenciaService;
    
    @Autowired
    private EstudianteService estudianteService;
    
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

    // Listar todas las asistencias
    @GetMapping
    public String listarAsistencias(Model model, HttpSession session) {
        String redirect = verificarAcceso(session, false);
        if (redirect != null) return redirect;
        
        List<Asistencia> asistencias = asistenciaService.listarTodas();
        model.addAttribute("asistencias", asistencias);
        model.addAttribute("rol", session.getAttribute("rol"));
        
        // Estadísticas
        long totalAsistencias = asistenciaService.contarTodas();
        long presentes = asistenciaService.contarPorEstado("PRESENTE");
        long ausentes = asistenciaService.contarPorEstado("AUSENTE");
        long tardanzas = asistenciaService.contarPorEstado("TARDANZA");
        
        model.addAttribute("totalAsistencias", totalAsistencias);
        model.addAttribute("presentes", presentes);
        model.addAttribute("ausentes", ausentes);
        model.addAttribute("tardanzas", tardanzas);
        
        return "asistencia-list";
    }

    // Mostrar formulario para nueva asistencia
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevaAsistencia(Model model, HttpSession session) {
        String redirect = verificarAcceso(session, true); // Solo admin
        if (redirect != null) return redirect;
        
        List<Estudiante> estudiantes = estudianteService.listarTodos();
        List<Profesor> profesores = profesorService.listarTodos();
        
        model.addAttribute("asistencia", new Asistencia());
        model.addAttribute("estudiantes", estudiantes);
        model.addAttribute("profesores", profesores);
        model.addAttribute("fechaActual", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        
        return "asistencia-form";
    }

    // Guardar nueva asistencia
    @PostMapping
    public String guardarAsistencia(@RequestParam(required = false) Long estudianteId,
                                   @RequestParam(required = false) Long profesorId,
                                   @RequestParam String fecha,
                                   @RequestParam(required = false) String horaEntrada,
                                   @RequestParam(required = false) String horaSalida,
                                   @RequestParam String estado,
                                   @RequestParam(required = false) String observaciones,
                                   HttpSession session) {
        String redirect = verificarAcceso(session, true); // Solo admin
        if (redirect != null) return redirect;
        
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date fechaAsistencia = sdf.parse(fecha);
            
            if (estudianteId != null) {
                // Registrar asistencia de estudiante
                asistenciaService.registrarAsistenciaEstudiante(
                    estudianteId, fechaAsistencia, horaEntrada, 
                    horaSalida, estado, observaciones
                );
            } else if (profesorId != null) {
                // Registrar asistencia de profesor
                asistenciaService.registrarAsistenciaProfesor(
                    profesorId, fechaAsistencia, horaEntrada, 
                    horaSalida, estado, observaciones
                );
            }
            
            return "redirect:/asistencias?success=asistencia-registrada";
        } catch (Exception e) {
            return "redirect:/asistencias/nuevo?error=error-registrar";
        }
    }

    // Mostrar formulario para editar asistencia
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditarAsistencia(@PathVariable Long id, Model model, HttpSession session) {
        String redirect = verificarAcceso(session, true); // Solo admin
        if (redirect != null) return redirect;
        
        if (id == null || id <= 0) {
            return "redirect:/asistencias?error=id-invalido";
        }
        
        Asistencia asistencia = asistenciaService.obtenerPorId(id);
        if (asistencia == null) {
            return "redirect:/asistencias?error=asistencia-no-encontrada";
        }
        
        List<Estudiante> estudiantes = estudianteService.listarTodos();
        List<Profesor> profesores = profesorService.listarTodos();
        
        model.addAttribute("asistencia", asistencia);
        model.addAttribute("estudiantes", estudiantes);
        model.addAttribute("profesores", profesores);
        
        return "asistencia-form";
    }

    // Actualizar asistencia
    @PostMapping("/actualizar")
    public String actualizarAsistencia(@RequestParam Long id,
                                      @RequestParam(required = false) Long estudianteId,
                                      @RequestParam(required = false) Long profesorId,
                                      @RequestParam String fecha,
                                      @RequestParam(required = false) String horaEntrada,
                                      @RequestParam(required = false) String horaSalida,
                                      @RequestParam String estado,
                                      @RequestParam(required = false) String observaciones,
                                      HttpSession session) {
        String redirect = verificarAcceso(session, true); // Solo admin
        if (redirect != null) return redirect;
        
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date fechaAsistencia = sdf.parse(fecha);
            
            Asistencia asistencia = asistenciaService.obtenerPorId(id);
            if (asistencia == null) {
                return "redirect:/asistencias?error=asistencia-no-encontrada";
            }
            
            // Asignar el estudiante o profesor según corresponda
            if (estudianteId != null) {
                Estudiante estudiante = estudianteService.obtenerPorId(estudianteId);
                asistencia.setEstudiante(estudiante);
                asistencia.setProfesor(null);
                asistencia.setTipo("ESTUDIANTE");
            } else if (profesorId != null) {
                Profesor profesor = profesorService.obtenerPorId(profesorId);
                asistencia.setProfesor(profesor);
                asistencia.setEstudiante(null);
                asistencia.setTipo("PROFESOR");
            }
            
            asistencia.setFecha(fechaAsistencia);
            asistencia.setHoraEntrada(horaEntrada);
            asistencia.setHoraSalida(horaSalida);
            asistencia.setEstado(estado);
            asistencia.setObservaciones(observaciones);
            
            asistenciaService.guardar(asistencia);
            return "redirect:/asistencias?success=asistencia-actualizada";
        } catch (Exception e) {
            return "redirect:/asistencias?error=error-actualizar";
        }
    }

    // Eliminar asistencia
    @GetMapping("/eliminar/{id}")
    public String eliminarAsistencia(@PathVariable Long id, HttpSession session) {
        String redirect = verificarAcceso(session, true); // Solo admin
        if (redirect != null) return redirect;
        
        if (id == null || id <= 0) {
            return "redirect:/asistencias?error=id-invalido";
        }
        
        try {
            asistenciaService.eliminar(id);
            return "redirect:/asistencias?success=asistencia-eliminada";
        } catch (Exception e) {
            return "redirect:/asistencias?error=error-eliminar";
        }
    }

    // Ver asistencias por estudiante
    @GetMapping("/estudiante/{estudianteId}")
    public String verAsistenciasEstudiante(@PathVariable Long estudianteId, Model model, HttpSession session) {
        String redirect = verificarAcceso(session, false);
        if (redirect != null) return redirect;
        
        List<Asistencia> asistencias = asistenciaService.listarPorEstudiante(estudianteId);
        Estudiante estudiante = estudianteService.obtenerPorId(estudianteId);
        
        if (estudiante == null) {
            return "redirect:/asistencias?error=estudiante-no-encontrado";
        }
        
        long presentes = asistenciaService.contarPorEstudianteYEstado(estudianteId, "PRESENTE");
        long ausentes = asistenciaService.contarPorEstudianteYEstado(estudianteId, "AUSENTE");
        long tardanzas = asistenciaService.contarPorEstudianteYEstado(estudianteId, "TARDANZA");
        
        model.addAttribute("asistencias", asistencias);
        model.addAttribute("estudiante", estudiante);
        model.addAttribute("presentes", presentes);
        model.addAttribute("ausentes", ausentes);
        model.addAttribute("tardanzas", tardanzas);
        model.addAttribute("rol", session.getAttribute("rol"));
        
        return "asistencia-estudiante";
    }

    // Ver asistencias por profesor
    @GetMapping("/profesor/{profesorId}")
    public String verAsistenciasProfesor(@PathVariable Long profesorId, Model model, HttpSession session) {
        String redirect = verificarAcceso(session, false);
        if (redirect != null) return redirect;
        
        List<Asistencia> asistencias = asistenciaService.listarPorProfesor(profesorId);
        Profesor profesor = profesorService.obtenerPorId(profesorId);
        
        if (profesor == null) {
            return "redirect:/asistencias?error=profesor-no-encontrado";
        }
        
        long presentes = asistenciaService.contarPorProfesorYEstado(profesorId, "PRESENTE");
        long ausentes = asistenciaService.contarPorProfesorYEstado(profesorId, "AUSENTE");
        long tardanzas = asistenciaService.contarPorProfesorYEstado(profesorId, "TARDANZA");
        
        model.addAttribute("asistencias", asistencias);
        model.addAttribute("profesor", profesor);
        model.addAttribute("presentes", presentes);
        model.addAttribute("ausentes", ausentes);
        model.addAttribute("tardanzas", tardanzas);
        model.addAttribute("rol", session.getAttribute("rol"));
        
        return "asistencia-profesor";
    }

    // Ver asistencias por fecha
    @GetMapping("/fecha")
    public String verAsistenciasPorFecha(@RequestParam String fecha, Model model, HttpSession session) {
        String redirect = verificarAcceso(session, false);
        if (redirect != null) return redirect;
        
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date fechaAsistencia = sdf.parse(fecha);
            
            List<Asistencia> asistencias = asistenciaService.listarPorFecha(fechaAsistencia);
            
            model.addAttribute("asistencias", asistencias);
            model.addAttribute("fecha", fecha);
            model.addAttribute("rol", session.getAttribute("rol"));
            
            return "asistencia-fecha";
        } catch (Exception e) {
            return "redirect:/asistencias?error=fecha-invalida";
        }
    }
}
