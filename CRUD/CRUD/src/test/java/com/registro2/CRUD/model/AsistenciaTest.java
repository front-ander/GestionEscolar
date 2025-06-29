package com.registro2.CRUD.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

@DisplayName("Pruebas unitarias para la clase Asistencia")
class AsistenciaTest {

    private Asistencia asistencia;
    private Date fechaPrueba;
    private Estudiante estudiantePrueba;
    private Profesor profesorPrueba;

    @BeforeEach
    void setUp() {
        // Configuración inicial para cada prueba
        fechaPrueba = new Date();
        estudiantePrueba = new Estudiante();
        estudiantePrueba.setId(1L);
        estudiantePrueba.setNombre("Juan Pérez");
        
        profesorPrueba = new Profesor();
        profesorPrueba.setId(1L);
        profesorPrueba.setNombre("María García");
        
        asistencia = new Asistencia();
    }

    @Test
    @DisplayName("Constructor por defecto debe crear una instancia válida")
    void testConstructorPorDefecto() {
        assertNotNull(asistencia);
        assertNull(asistencia.getId());
        assertNull(asistencia.getFecha());
        assertNull(asistencia.getHoraEntrada());
        assertNull(asistencia.getHoraSalida());
        assertNull(asistencia.getEstado());
        assertNull(asistencia.getObservaciones());
        assertNull(asistencia.getEstudiante());
        assertNull(asistencia.getProfesor());
        assertNull(asistencia.getTipo());
    }

    @Test
    @DisplayName("Constructor con parámetros debe inicializar correctamente")
    void testConstructorConParametros() {
        String horaEntrada = "08:00";
        String horaSalida = "16:00";
        String estado = "PRESENTE";
        String observaciones = "Asistencia normal";

        Asistencia asistenciaConParametros = new Asistencia(
            fechaPrueba, horaEntrada, horaSalida, estado, observaciones
        );

        assertNotNull(asistenciaConParametros);
        assertEquals(fechaPrueba, asistenciaConParametros.getFecha());
        assertEquals(horaEntrada, asistenciaConParametros.getHoraEntrada());
        assertEquals(horaSalida, asistenciaConParametros.getHoraSalida());
        assertEquals(estado, asistenciaConParametros.getEstado());
        assertEquals(observaciones, asistenciaConParametros.getObservaciones());
    }

    @Test
    @DisplayName("Getter y Setter de ID deben funcionar correctamente")
    void testIdGetterSetter() {
        Long id = 1L;
        asistencia.setId(id);
        assertEquals(id, asistencia.getId());
    }

    @Test
    @DisplayName("Getter y Setter de fecha deben funcionar correctamente")
    void testFechaGetterSetter() {
        asistencia.setFecha(fechaPrueba);
        assertEquals(fechaPrueba, asistencia.getFecha());
    }

    @Test
    @DisplayName("Getter y Setter de horaEntrada deben funcionar correctamente")
    void testHoraEntradaGetterSetter() {
        String horaEntrada = "08:30";
        asistencia.setHoraEntrada(horaEntrada);
        assertEquals(horaEntrada, asistencia.getHoraEntrada());
    }

    @Test
    @DisplayName("Getter y Setter de horaSalida deben funcionar correctamente")
    void testHoraSalidaGetterSetter() {
        String horaSalida = "17:30";
        asistencia.setHoraSalida(horaSalida);
        assertEquals(horaSalida, asistencia.getHoraSalida());
    }

    @Test
    @DisplayName("Getter y Setter de estado deben funcionar correctamente")
    void testEstadoGetterSetter() {
        String estado = "AUSENTE";
        asistencia.setEstado(estado);
        assertEquals(estado, asistencia.getEstado());
    }

    @Test
    @DisplayName("Getter y Setter de observaciones deben funcionar correctamente")
    void testObservacionesGetterSetter() {
        String observaciones = "Llegó tarde a clase";
        asistencia.setObservaciones(observaciones);
        assertEquals(observaciones, asistencia.getObservaciones());
    }

    @Test
    @DisplayName("Getter y Setter de estudiante deben funcionar correctamente")
    void testEstudianteGetterSetter() {
        asistencia.setEstudiante(estudiantePrueba);
        assertEquals(estudiantePrueba, asistencia.getEstudiante());
    }

    @Test
    @DisplayName("Getter y Setter de profesor deben funcionar correctamente")
    void testProfesorGetterSetter() {
        asistencia.setProfesor(profesorPrueba);
        assertEquals(profesorPrueba, asistencia.getProfesor());
    }

    @Test
    @DisplayName("Getter y Setter de tipo deben funcionar correctamente")
    void testTipoGetterSetter() {
        String tipo = "ESTUDIANTE";
        asistencia.setTipo(tipo);
        assertEquals(tipo, asistencia.getTipo());
    }

    @Test
    @DisplayName("Asistencia debe poder configurarse para estudiante")
    void testAsistenciaEstudiante() {
        asistencia.setEstudiante(estudiantePrueba);
        asistencia.setTipo("ESTUDIANTE");
        asistencia.setEstado("PRESENTE");
        asistencia.setHoraEntrada("08:00");
        asistencia.setHoraSalida("16:00");

        assertEquals(estudiantePrueba, asistencia.getEstudiante());
        assertEquals("ESTUDIANTE", asistencia.getTipo());
        assertEquals("PRESENTE", asistencia.getEstado());
        assertEquals("08:00", asistencia.getHoraEntrada());
        assertEquals("16:00", asistencia.getHoraSalida());
    }

    @Test
    @DisplayName("Asistencia debe poder configurarse para profesor")
    void testAsistenciaProfesor() {
        asistencia.setProfesor(profesorPrueba);
        asistencia.setTipo("PROFESOR");
        asistencia.setEstado("PRESENTE");
        asistencia.setHoraEntrada("07:30");
        asistencia.setHoraSalida("17:30");

        assertEquals(profesorPrueba, asistencia.getProfesor());
        assertEquals("PROFESOR", asistencia.getTipo());
        assertEquals("PRESENTE", asistencia.getEstado());
        assertEquals("07:30", asistencia.getHoraEntrada());
        assertEquals("17:30", asistencia.getHoraSalida());
    }

    @Test
    @DisplayName("Asistencia con tardanza debe configurarse correctamente")
    void testAsistenciaTardanza() {
        asistencia.setEstudiante(estudiantePrueba);
        asistencia.setTipo("ESTUDIANTE");
        asistencia.setEstado("TARDANZA");
        asistencia.setHoraEntrada("08:45");
        asistencia.setObservaciones("Llegó 45 minutos tarde");

        assertEquals("TARDANZA", asistencia.getEstado());
        assertEquals("08:45", asistencia.getHoraEntrada());
        assertEquals("Llegó 45 minutos tarde", asistencia.getObservaciones());
    }

    @Test
    @DisplayName("Asistencia ausente debe configurarse correctamente")
    void testAsistenciaAusente() {
        asistencia.setEstudiante(estudiantePrueba);
        asistencia.setTipo("ESTUDIANTE");
        asistencia.setEstado("AUSENTE");
        asistencia.setObservaciones("No se presentó a clase");

        assertEquals("AUSENTE", asistencia.getEstado());
        assertEquals("No se presentó a clase", asistencia.getObservaciones());
    }

    @Test
    @DisplayName("Asistencia completa debe tener todos los campos configurados")
    void testAsistenciaCompleta() {
        // Configurar todos los campos
        asistencia.setId(1L);
        asistencia.setFecha(fechaPrueba);
        asistencia.setEstudiante(estudiantePrueba);
        asistencia.setTipo("ESTUDIANTE");
        asistencia.setEstado("PRESENTE");
        asistencia.setHoraEntrada("08:00");
        asistencia.setHoraSalida("16:00");
        asistencia.setObservaciones("Asistencia perfecta");

        // Verificar todos los campos
        assertEquals(1L, asistencia.getId());
        assertEquals(fechaPrueba, asistencia.getFecha());
        assertEquals(estudiantePrueba, asistencia.getEstudiante());
        assertEquals("ESTUDIANTE", asistencia.getTipo());
        assertEquals("PRESENTE", asistencia.getEstado());
        assertEquals("08:00", asistencia.getHoraEntrada());
        assertEquals("16:00", asistencia.getHoraSalida());
        assertEquals("Asistencia perfecta", asistencia.getObservaciones());
    }

    @Test
    @DisplayName("Asistencia debe permitir valores nulos en campos opcionales")
    void testAsistenciaConValoresNulos() {
        asistencia.setId(1L);
        asistencia.setFecha(fechaPrueba);
        asistencia.setEstado("PRESENTE");
        // No establecer horaEntrada, horaSalida, observaciones, estudiante, profesor, tipo

        assertEquals(1L, asistencia.getId());
        assertEquals(fechaPrueba, asistencia.getFecha());
        assertEquals("PRESENTE", asistencia.getEstado());
        assertNull(asistencia.getHoraEntrada());
        assertNull(asistencia.getHoraSalida());
        assertNull(asistencia.getObservaciones());
        assertNull(asistencia.getEstudiante());
        assertNull(asistencia.getProfesor());
        assertNull(asistencia.getTipo());
    }

    @Test
    @DisplayName("Asistencia debe permitir cambiar valores después de la configuración inicial")
    void testCambioDeValores() {
        // Configuración inicial
        asistencia.setEstado("PRESENTE");
        asistencia.setHoraEntrada("08:00");
        
        // Cambiar valores
        asistencia.setEstado("TARDANZA");
        asistencia.setHoraEntrada("08:45");
        asistencia.setObservaciones("Cambio de estado por tardanza");

        assertEquals("TARDANZA", asistencia.getEstado());
        assertEquals("08:45", asistencia.getHoraEntrada());
        assertEquals("Cambio de estado por tardanza", asistencia.getObservaciones());
    }
} 