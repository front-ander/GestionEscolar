package com.registro2.CRUD.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.registro2.CRUD.model.Estudiante;
import com.registro2.CRUD.model.Pago;
import com.registro2.CRUD.services.EstudianteService;
import com.registro2.CRUD.services.PagoService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/pagos")
public class PagoController {

    @Autowired
    private PagoService pagoService;
    
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

    // Listar todos los pagos
    @GetMapping
    public String listarPagos(Model model, HttpSession session) {
        String redirect = verificarAcceso(session, false);
        if (redirect != null) return redirect;
        
        List<Pago> pagos = pagoService.listarTodos();
        model.addAttribute("pagos", pagos);
        model.addAttribute("rol", session.getAttribute("rol"));
        
        // Estadísticas
        long totalPagos = pagoService.contarTodos();
        long pagosPagados = pagoService.contarPorEstado("PAGADO");
        long pagosPendientes = pagoService.contarPorEstado("PENDIENTE");
        long pagosVencidos = pagoService.contarPorEstado("VENCIDO");
        
        // Total de montos
        Double totalMontoPagado = pagoService.sumarMontoPorMesYAnio("ENERO", Calendar.getInstance().get(Calendar.YEAR));
        if (totalMontoPagado == null) totalMontoPagado = 0.0;
        
        model.addAttribute("totalPagos", totalPagos);
        model.addAttribute("pagosPagados", pagosPagados);
        model.addAttribute("pagosPendientes", pagosPendientes);
        model.addAttribute("pagosVencidos", pagosVencidos);
        model.addAttribute("totalMontoPagado", totalMontoPagado);
        
        return "pago-list";
    }

    // Mostrar formulario para nuevo pago
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevoPago(Model model, HttpSession session) {
        String redirect = verificarAcceso(session, true); // Solo admin
        if (redirect != null) return redirect;
        
        List<Estudiante> estudiantes = estudianteService.listarTodos();
        
        model.addAttribute("pago", new Pago());
        model.addAttribute("estudiantes", estudiantes);
        model.addAttribute("fechaActual", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        
        // Meses disponibles
        String[] meses = {"ENERO", "FEBRERO", "MARZO", "ABRIL", "MAYO", "JUNIO", 
                         "JULIO", "AGOSTO", "SEPTIEMBRE", "OCTUBRE", "NOVIEMBRE", "DICIEMBRE"};
        model.addAttribute("meses", meses);
        
        // Años disponibles (actual y siguiente)
        int anioActual = Calendar.getInstance().get(Calendar.YEAR);
        model.addAttribute("anioActual", anioActual);
        model.addAttribute("anioSiguiente", anioActual + 1);
        
        return "pago-form";
    }

    // Guardar nuevo pago
    @PostMapping
    public String guardarPago(@RequestParam Long estudianteId,
                             @RequestParam String fecha,
                             @RequestParam double monto,
                             @RequestParam String metodoPago,
                             @RequestParam String estado,
                             @RequestParam String mes,
                             @RequestParam int anio,
                             @RequestParam(required = false) String observaciones,
                             HttpSession session) {
        String redirect = verificarAcceso(session, true); // Solo admin
        if (redirect != null) return redirect;
        
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date fechaPago = sdf.parse(fecha);
            
            pagoService.registrarPagoEstudiante(
                estudianteId, fechaPago, monto, 
                metodoPago, mes, anio, observaciones
            );
            
            return "redirect:/pagos?success=pago-registrado";
        } catch (Exception e) {
            return "redirect:/pagos/nuevo?error=error-registrar";
        }
    }

    // Mostrar formulario para editar pago
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditarPago(@PathVariable Long id, Model model, HttpSession session) {
        String redirect = verificarAcceso(session, true); // Solo admin
        if (redirect != null) return redirect;
        
        if (id == null || id <= 0) {
            return "redirect:/pagos?error=id-invalido";
        }
        
        Pago pago = pagoService.obtenerPorId(id);
        if (pago == null) {
            return "redirect:/pagos?error=pago-no-encontrado";
        }
        
        List<Estudiante> estudiantes = estudianteService.listarTodos();
        
        model.addAttribute("pago", pago);
        model.addAttribute("estudiantes", estudiantes);
        
        // Meses disponibles
        String[] meses = {"ENERO", "FEBRERO", "MARZO", "ABRIL", "MAYO", "JUNIO", 
                         "JULIO", "AGOSTO", "SEPTIEMBRE", "OCTUBRE", "NOVIEMBRE", "DICIEMBRE"};
        model.addAttribute("meses", meses);
        
        return "pago-form";
    }

    // Actualizar pago
    @PostMapping("/actualizar")
    public String actualizarPago(@RequestParam Long id,
                                @RequestParam Long estudianteId,
                                @RequestParam String fecha,
                                @RequestParam double monto,
                                @RequestParam String metodoPago,
                                @RequestParam String estado,
                                @RequestParam String mes,
                                @RequestParam int anio,
                                @RequestParam(required = false) String observaciones,
                                HttpSession session) {
        String redirect = verificarAcceso(session, true); // Solo admin
        if (redirect != null) return redirect;
        
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date fechaPago = sdf.parse(fecha);
            
            Pago pago = pagoService.obtenerPorId(id);
            if (pago == null) {
                return "redirect:/pagos?error=pago-no-encontrado";
            }
            
            Estudiante estudiante = estudianteService.obtenerPorId(estudianteId);
            
            pago.setEstudiante(estudiante);
            pago.setFecha(fechaPago);
            pago.setMonto(monto);
            pago.setMetodoPago(metodoPago);
            pago.setEstado(estado);
            pago.setMes(mes);
            pago.setAnio(anio);
            pago.setObservaciones(observaciones);
            
            pagoService.guardar(pago);
            return "redirect:/pagos?success=pago-actualizado";
        } catch (Exception e) {
            return "redirect:/pagos?error=error-actualizar";
        }
    }

    // Eliminar pago
    @GetMapping("/eliminar/{id}")
    public String eliminarPago(@PathVariable Long id, HttpSession session) {
        String redirect = verificarAcceso(session, true); // Solo admin
        if (redirect != null) return redirect;
        
        if (id == null || id <= 0) {
            return "redirect:/pagos?error=id-invalido";
        }
        
        try {
            pagoService.eliminar(id);
            return "redirect:/pagos?success=pago-eliminado";
        } catch (Exception e) {
            return "redirect:/pagos?error=error-eliminar";
        }
    }

    // Ver pagos por estudiante
    @GetMapping("/estudiante/{estudianteId}")
    public String verPagosEstudiante(@PathVariable Long estudianteId, Model model, HttpSession session) {
        String redirect = verificarAcceso(session, false);
        if (redirect != null) return redirect;
        
        List<Pago> pagos = pagoService.listarPorEstudiante(estudianteId);
        Estudiante estudiante = estudianteService.obtenerPorId(estudianteId);
        
        if (estudiante == null) {
            return "redirect:/pagos?error=estudiante-no-encontrado";
        }
        
        long pagosPagados = pagoService.contarPorEstudianteYEstado(estudianteId, "PAGADO");
        long pagosPendientes = pagoService.contarPorEstudianteYEstado(estudianteId, "PENDIENTE");
        long pagosVencidos = pagoService.contarPorEstudianteYEstado(estudianteId, "VENCIDO");
        
        double totalPagado = pagoService.obtenerTotalPagosEstudiante(estudianteId);
        double totalPendiente = pagoService.obtenerTotalPendientesEstudiante(estudianteId);
        
        model.addAttribute("pagos", pagos);
        model.addAttribute("estudiante", estudiante);
        model.addAttribute("pagosPagados", pagosPagados);
        model.addAttribute("pagosPendientes", pagosPendientes);
        model.addAttribute("pagosVencidos", pagosVencidos);
        model.addAttribute("totalPagado", totalPagado);
        model.addAttribute("totalPendiente", totalPendiente);
        model.addAttribute("rol", session.getAttribute("rol"));
        
        return "pago-estudiante";
    }

    // Crear pagos pendientes para un estudiante
    @GetMapping("/crear-pendientes/{estudianteId}")
    public String crearPagosPendientes(@PathVariable Long estudianteId, 
                                      @RequestParam double montoMensual,
                                      @RequestParam int anio,
                                      HttpSession session) {
        String redirect = verificarAcceso(session, true); // Solo admin
        if (redirect != null) return redirect;
        
        try {
            pagoService.crearPagosPendientesEstudiante(estudianteId, montoMensual, anio);
            return "redirect:/pagos/estudiante/" + estudianteId + "?success=pagos-pendientes-creados";
        } catch (Exception e) {
            return "redirect:/pagos/estudiante/" + estudianteId + "?error=error-crear-pendientes";
        }
    }

    // Ver pagos por estado
    @GetMapping("/estado/{estado}")
    public String verPagosPorEstado(@PathVariable String estado, Model model, HttpSession session) {
        String redirect = verificarAcceso(session, false);
        if (redirect != null) return redirect;
        
        List<Pago> pagos = pagoService.listarPorEstado(estado);
        
        model.addAttribute("pagos", pagos);
        model.addAttribute("estado", estado);
        model.addAttribute("rol", session.getAttribute("rol"));
        
        return "pago-estado";
    }

    // Ver pagos por mes y año
    @GetMapping("/mes")
    public String verPagosPorMes(@RequestParam String mes, 
                                 @RequestParam int anio, 
                                 Model model, HttpSession session) {
        String redirect = verificarAcceso(session, false);
        if (redirect != null) return redirect;
        
        List<Pago> pagos = pagoService.listarPorMesYAnio(mes, anio);
        Double totalMes = pagoService.sumarMontoPorMesYAnio(mes, anio);
        
        model.addAttribute("pagos", pagos);
        model.addAttribute("mes", mes);
        model.addAttribute("anio", anio);
        model.addAttribute("totalMes", totalMes != null ? totalMes : 0.0);
        model.addAttribute("rol", session.getAttribute("rol"));
        
        return "pago-mes";
    }

    // Marcar pago como pagado
    @GetMapping("/marcar-pagado/{id}")
    public String marcarPagoComoPagado(@PathVariable Long id, HttpSession session) {
        String redirect = verificarAcceso(session, true); // Solo admin
        if (redirect != null) return redirect;
        
        try {
            Pago pago = pagoService.obtenerPorId(id);
            if (pago != null) {
                pago.setEstado("PAGADO");
                pago.setFecha(new Date());
                pagoService.guardar(pago);
                return "redirect:/pagos?success=pago-marcado-pagado";
            }
            return "redirect:/pagos?error=pago-no-encontrado";
        } catch (Exception e) {
            return "redirect:/pagos?error=error-marcar-pagado";
        }
    }
} 