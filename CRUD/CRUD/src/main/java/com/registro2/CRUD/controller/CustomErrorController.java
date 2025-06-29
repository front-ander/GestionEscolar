package com.registro2.CRUD.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;
import java.util.logging.Logger;

@Controller
public class CustomErrorController implements ErrorController {

    private static final Logger logger = Logger.getLogger(CustomErrorController.class.getName());

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        Exception exception = (Exception) request.getAttribute("javax.servlet.error.exception");
        String requestUri = (String) request.getAttribute("javax.servlet.error.request_uri");
        String servletName = (String) request.getAttribute("javax.servlet.error.servlet_name");
        
        // Log del error para depuración
        logger.severe("Error en la aplicación: " + 
                     "Status: " + statusCode + 
                     ", URI: " + requestUri + 
                     ", Servlet: " + servletName + 
                     ", Exception: " + (exception != null ? exception.getMessage() : "N/A"));
        
        if (exception != null) {
            logger.severe("Stack trace: ");
            exception.printStackTrace();
        }
        
        model.addAttribute("statusCode", statusCode != null ? statusCode : "N/A");
        model.addAttribute("errorMessage", exception != null ? exception.getMessage() : "Error desconocido");
        model.addAttribute("requestUri", requestUri != null ? requestUri : "N/A");
        model.addAttribute("servletName", servletName != null ? servletName : "N/A");
        
        return "error";
    }
}
