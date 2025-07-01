# Sistema de Gestión Escolar - CRUD

Este proyecto es una aplicación web de gestión escolar desarrollada con Spring Boot, Thymeleaf y MySQL. Permite la administración de estudiantes, profesores, asistencias, pagos, notas, cursos y personas (apoderados).

## Características

- Gestión de estudiantes y profesores
- Registro y consulta de asistencias
- Control de pagos y generación de reportes
- Gestión de notas y cursos
- Roles de usuario: Administrador y Secretario (con permisos de solo lectura)
- Interfaz moderna y responsiva con Bootstrap y JavaScript
- Pruebas unitarias y de integración

## Tecnologías Utilizadas

- Java 17
- Spring Boot 3
- Spring Data JPA
- Thymeleaf
- MySQL
- Bootstrap 5
- JavaScript (ES6)
- JUnit 5, Mockito

## Estructura del Proyecto

```
CRUDcopia/
  CRUD/
    CRUD/
      src/
        main/
          java/com/registro2/CRUD/
          resources/
            static/
            templates/
        test/
          java/com/registro2/CRUD/
      pom.xml
      README.md
```

## Configuración Inicial

1. **Clona el repositorio:**
   ```bash
   git clone https://github.com/tuusuario/tu-repo.git
   cd CRUDcopia/CRUD/CRUD
   ```

3. **Instala las dependencias y compila:**
   ```bash
   ./mvnw clean install
   ```

4. **Ejecuta la aplicación:**
   ```bash
   ./mvnw spring-boot:run
   ```

5. **Accede a la aplicación:**
   - Abre [http://localhost:8080](http://localhost:8080) en tu navegador.

## Pruebas

- Ejecuta todas las pruebas:
  ```bash
  ./mvnw test
  ```
- Pruebas unitarias principales:
  - [`AsistenciaTest.java`](src/test/java/com/registro2/CRUD/model/AsistenciaTest.java)
  - [`AsistenciaAdvancedTest.java`](src/test/java/com/registro2/CRUD/model/AsistenciaAdvancedTest.java)

## Estructura de Carpetas

- `src/main/java/com/registro2/CRUD/` - Código fuente Java (controladores, servicios, modelos, repositorios)
- `src/main/resources/templates/` - Vistas Thymeleaf (HTML)
- `src/main/resources/static/` - Archivos estáticos (CSS, JS, imágenes)
- `src/test/java/com/registro2/CRUD/` - Pruebas unitarias

## Usuarios y Roles

- **Administrador:** Acceso completo a todas las funciones.
- **Secretario:** Acceso de solo lectura (consulta).

## Créditos

Desarrollado por Anderson y colaboradores.

---

**Licencia:** MIT
