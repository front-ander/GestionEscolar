package com.registro2.CRUD.controller;

import com.registro2.CRUD.model.Usuario;
import com.registro2.CRUD.services.UsuarioService;
import com.registro2.CRUD.services.PersonaService;
import com.registro2.CRUD.services.EstudianteService;
import com.registro2.CRUD.services.ProfesorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {

    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private PersonaService personaService;
    
    @Autowired
    private EstudianteService estudianteService;
    
    @Autowired
    private ProfesorService profesorService;

    // Redirigir la página principal al login
    @GetMapping("/")
    public String redirectToLogin() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String mostrarLogin(HttpSession session) {
        // Si ya está logueado, redirigir según su rol
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario != null) {
            if ("ADMIN".equals(usuario.getRol())) {
                return "redirect:/dashboard";
            } else {
                return "redirect:/dashboard-secretario";
            }
        }
        return "login";
    }

    @PostMapping("/login")
    public String procesarLogin(@RequestParam String username, 
                               @RequestParam String password, 
                               HttpServletRequest request,
                               Model model) {
        
        // Validación básica contra inyección
        if (username == null || password == null || 
            username.trim().isEmpty() || password.trim().isEmpty()) {
            model.addAttribute("error", "Usuario y contraseña son requeridos");
            return "login";
        }

        // Limitar longitud para prevenir ataques
        if (username.length() > 50 || password.length() > 100) {
            model.addAttribute("error", "Credenciales inválidas");
            return "login";
        }

        Usuario usuario = usuarioService.autenticar(username.trim(), password);
        
        if (usuario != null) {
            // Invalidar sesión anterior y crear nueva
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }
            session = request.getSession(true);
            
            session.setAttribute("usuario", usuario);
            session.setAttribute("rol", usuario.getRol());
            session.setAttribute("username", usuario.getUsername());
            
            // Redirigir según el rol
            if ("ADMIN".equals(usuario.getRol())) {
                return "redirect:/dashboard";
            } else {
                return "redirect:/dashboard-secretario";
            }
        } else {
            model.addAttribute("error", "Usuario o contraseña incorrectos");
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/login?logout=true";
    }

    // Dashboard para admin con datos reales
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null || !"ADMIN".equals(usuario.getRol())) {
            return "redirect:/login";
        }
        
        // Obtener datos reales de la base de datos
        long totalPersonas = personaService.contarTodas();
        long totalEstudiantes = estudianteService.contarTodos();
        long totalProfesores = profesorService.contarTodos();
        
        // Agregar datos al modelo
        model.addAttribute("usuario", usuario);
        model.addAttribute("totalPersonas", totalPersonas);
        model.addAttribute("totalEstudiantes", totalEstudiantes);
        model.addAttribute("totalProfesores", totalProfesores);
        model.addAttribute("totalCursos", 12); // Valor fijo por ahora
        
        return "index";
    }

    @GetMapping("/dashboard-secretario")
    public String dashboardSecretario(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null || !"SECRETARIO".equals(usuario.getRol())) {
            return "redirect:/login";
        }
        
        // Obtener datos reales para secretario
        long totalPersonas = personaService.contarTodas();
        long totalEstudiantes = estudianteService.contarTodos();
        long totalProfesores = profesorService.contarTodos();
        
        model.addAttribute("usuario", usuario);
        model.addAttribute("totalPersonas", totalPersonas);
        model.addAttribute("totalEstudiantes", totalEstudiantes);
        model.addAttribute("totalProfesores", totalProfesores);
        model.addAttribute("totalCursos", 12);
        
        return "dashboard-secretario";
    }
}