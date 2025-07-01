package com.registro2.CRUD;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.registro2.CRUD.services.AsistenciaService;
import com.registro2.CRUD.services.EstudianteService;
import com.registro2.CRUD.services.PagoService;
import com.registro2.CRUD.services.ProfesorService;

@SpringBootApplication
public class CrudApplication {

	private static final Logger logger = Logger.getLogger(CrudApplication.class.getName());

	public static void main(String[] args) {
		SpringApplication.run(CrudApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(
			@Autowired AsistenciaService asistenciaService,
			@Autowired PagoService pagoService,
			@Autowired EstudianteService estudianteService,
			@Autowired ProfesorService profesorService) {
		return args -> {
			try {
				logger.info("Iniciando verificación de servicios...");
				
				// Verificar que los servicios están disponibles
				logger.info("Verificando servicio de estudiantes...");
				estudianteService.listarTodos();
				logger.info("Servicio de estudiantes OK");
				
				logger.info("Verificando servicio de profesores...");
				profesorService.listarTodos();
				logger.info("Servicio de profesores OK");
				
				logger.info("Verificando servicio de asistencias...");
				asistenciaService.listarTodas();
				logger.info("Servicio de asistencias OK");
				
				logger.info("Verificando servicio de pagos...");
				pagoService.listarTodos();
				logger.info("Servicio de pagos OK");
				
				logger.info("Todos los servicios están funcionando correctamente");
				
			} catch (Exception e) {
				logger.severe("Error durante la inicialización: " + e.getMessage());
				e.printStackTrace();
			}
		};
	}
}
