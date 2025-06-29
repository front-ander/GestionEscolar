package com.registro2.CRUD.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Date;
import java.util.Calendar;

@DisplayName("Pruebas unitarias avanzadas para la clase Asistencia")
class AsistenciaAdvancedTest {

    private Asistencia asistencia;
    private Estudiante estudianteMock;
    private Profesor profesorMock;
    private Date fechaActual;

    @BeforeEach
    void setUp() {
        asistencia = new Asistencia();
        estudianteMock = mock(Estudiante.class);
        profesorMock = mock(Profesor.class);
        fechaActual = new Date();
        
        // Configurar mocks
        when(estudianteMock.getId()).thenReturn(1L);
        when(estudianteMock.getNombre()).thenReturn("Juan Pérez");
        when(profesorMock.getId()).thenReturn(1L);
        when(profesorMock.getNombre()).thenReturn("María García");
    }

    @Nested
    @DisplayName("Pruebas de validación de datos")
    class ValidacionDatosTest {

        @Test
        @DisplayName("Asistencia debe validar estados válidos")
        void testEstadosValidos() {
            String[] estadosValidos = {"PRESENTE", "AUSENTE", "TARDANZA"};
            
            for (String estado : estadosValidos) {
                asistencia.setEstado(estado);
                assertEquals(estado, asistencia.getEstado());
            }
        }

        @Test
        @DisplayName("Asistencia debe validar tipos válidos")
        void testTiposValidos() {
            String[] tiposValidos = {"ESTUDIANTE", "PROFESOR"};
            
            for (String tipo : tiposValidos) {
                asistencia.setTipo(tipo);
                assertEquals(tipo, asistencia.getTipo());
            }
        }

        @ParameterizedTest
        @ValueSource(strings = {"08:00", "08:30", "09:00", "17:00", "18:30"})
        @DisplayName("Asistencia debe aceptar diferentes formatos de hora")
        void testFormatosHoraValidos(String hora) {
            asistencia.setHoraEntrada(hora);
            assertEquals(hora, asistencia.getHoraEntrada());
        }

        @ParameterizedTest
        @CsvSource({
            "08:00, 16:00, PRESENTE",
            "08:45, 16:00, TARDANZA",
            "null, null, AUSENTE"
        })
        @DisplayName("Asistencia debe configurarse correctamente con diferentes escenarios")
        void testEscenariosAsistencia(String horaEntrada, String horaSalida, String estado) {
            asistencia.setHoraEntrada("null".equals(horaEntrada) ? null : horaEntrada);
            asistencia.setHoraSalida("null".equals(horaSalida) ? null : horaSalida);
            asistencia.setEstado(estado);

            if (!"null".equals(horaEntrada)) {
                assertEquals(horaEntrada, asistencia.getHoraEntrada());
            } else {
                assertNull(asistencia.getHoraEntrada());
            }

            if (!"null".equals(horaSalida)) {
                assertEquals(horaSalida, asistencia.getHoraSalida());
            } else {
                assertNull(asistencia.getHoraSalida());
            }

            assertEquals(estado, asistencia.getEstado());
        }
    }

    @Nested
    @DisplayName("Pruebas con mocks")
    class MockTest {

        @Test
        @DisplayName("Asistencia debe trabajar correctamente con estudiante mock")
        void testAsistenciaConEstudianteMock() {
            asistencia.setEstudiante(estudianteMock);
            asistencia.setTipo("ESTUDIANTE");

            assertEquals(estudianteMock, asistencia.getEstudiante());
            assertEquals("ESTUDIANTE", asistencia.getTipo());

            // Verificar que se llama al mock
            verify(estudianteMock, times(0)).getId(); // No se llama automáticamente
            asistencia.getEstudiante().getId(); // Llamada explícita
            verify(estudianteMock, times(1)).getId();
        }

        @Test
        @DisplayName("Asistencia debe trabajar correctamente con profesor mock")
        void testAsistenciaConProfesorMock() {
            asistencia.setProfesor(profesorMock);
            asistencia.setTipo("PROFESOR");

            assertEquals(profesorMock, asistencia.getProfesor());
            assertEquals("PROFESOR", asistencia.getTipo());

            // Verificar que se llama al mock
            verify(profesorMock, times(0)).getId(); // No se llama automáticamente
            asistencia.getProfesor().getId(); // Llamada explícita
            verify(profesorMock, times(1)).getId();
        }

        @Test
        @DisplayName("Asistencia debe permitir cambiar entre estudiante y profesor")
        void testCambioEntreEstudianteYProfesor() {
            // Inicialmente configurado para estudiante
            asistencia.setEstudiante(estudianteMock);
            asistencia.setTipo("ESTUDIANTE");
            assertEquals(estudianteMock, asistencia.getEstudiante());
            assertNull(asistencia.getProfesor());

            // Cambiar a profesor
            asistencia.setEstudiante(null);
            asistencia.setProfesor(profesorMock);
            asistencia.setTipo("PROFESOR");
            assertEquals(profesorMock, asistencia.getProfesor());
            assertNull(asistencia.getEstudiante());
        }
    }

    @Nested
    @DisplayName("Pruebas de fechas")
    class FechasTest {

        @Test
        @DisplayName("Asistencia debe manejar diferentes fechas correctamente")
        void testDiferentesFechas() {
            Calendar cal = Calendar.getInstance();
            
            // Fecha actual
            Date fechaActual = cal.getTime();
            asistencia.setFecha(fechaActual);
            assertEquals(fechaActual, asistencia.getFecha());

            // Fecha pasada
            cal.add(Calendar.DAY_OF_MONTH, -1);
            Date fechaPasada = cal.getTime();
            asistencia.setFecha(fechaPasada);
            assertEquals(fechaPasada, asistencia.getFecha());

            // Fecha futura
            cal.add(Calendar.DAY_OF_MONTH, 2);
            Date fechaFutura = cal.getTime();
            asistencia.setFecha(fechaFutura);
            assertEquals(fechaFutura, asistencia.getFecha());
        }

        @Test
        @DisplayName("Asistencia debe permitir fecha nula")
        void testFechaNula() {
            asistencia.setFecha(null);
            assertNull(asistencia.getFecha());
        }
    }

    @Nested
    @DisplayName("Pruebas de casos límite")
    class CasosLimiteTest {

        @Test
        @DisplayName("Asistencia debe manejar strings vacíos")
        void testStringsVacios() {
            asistencia.setHoraEntrada("");
            asistencia.setHoraSalida("");
            asistencia.setEstado("");
            asistencia.setObservaciones("");
            asistencia.setTipo("");

            assertEquals("", asistencia.getHoraEntrada());
            assertEquals("", asistencia.getHoraSalida());
            assertEquals("", asistencia.getEstado());
            assertEquals("", asistencia.getObservaciones());
            assertEquals("", asistencia.getTipo());
        }

        @Test
        @DisplayName("Asistencia debe manejar strings largos")
        void testStringsLargos() {
            String observacionLarga = "Esta es una observación muy larga que contiene mucha información " +
                                     "sobre la asistencia del estudiante y puede incluir detalles importantes " +
                                     "sobre el comportamiento, la puntualidad y otros aspectos relevantes " +
                                     "para el seguimiento académico del alumno.";

            asistencia.setObservaciones(observacionLarga);
            assertEquals(observacionLarga, asistencia.getObservaciones());
        }

        @Test
        @DisplayName("Asistencia debe manejar valores extremos de ID")
        void testValoresExtremosId() {
            asistencia.setId(0L);
            assertEquals(0L, asistencia.getId());

            asistencia.setId(Long.MAX_VALUE);
            assertEquals(Long.MAX_VALUE, asistencia.getId());

            asistencia.setId(Long.MIN_VALUE);
            assertEquals(Long.MIN_VALUE, asistencia.getId());
        }
    }

    @Nested
    @DisplayName("Pruebas de integración")
    class IntegracionTest {

        @Test
        @DisplayName("Asistencia completa debe integrar todos los componentes")
        void testIntegracionCompleta() {
            // Configurar asistencia completa
            asistencia.setId(1L);
            asistencia.setFecha(fechaActual);
            asistencia.setEstudiante(estudianteMock);
            asistencia.setTipo("ESTUDIANTE");
            asistencia.setEstado("PRESENTE");
            asistencia.setHoraEntrada("08:00");
            asistencia.setHoraSalida("16:00");
            asistencia.setObservaciones("Asistencia normal");

            // Verificar integración
            assertNotNull(asistencia);
            assertEquals(1L, asistencia.getId());
            assertEquals(fechaActual, asistencia.getFecha());
            assertEquals(estudianteMock, asistencia.getEstudiante());
            assertEquals("ESTUDIANTE", asistencia.getTipo());
            assertEquals("PRESENTE", asistencia.getEstado());
            assertEquals("08:00", asistencia.getHoraEntrada());
            assertEquals("16:00", asistencia.getHoraSalida());
            assertEquals("Asistencia normal", asistencia.getObservaciones());
            assertNull(asistencia.getProfesor()); // No debe tener profesor si es estudiante
        }

        @Test
        @DisplayName("Asistencia debe mantener consistencia entre tipo y entidad")
        void testConsistenciaTipoEntidad() {
            // Configurar como estudiante
            asistencia.setEstudiante(estudianteMock);
            asistencia.setTipo("ESTUDIANTE");
            asistencia.setProfesor(null);

            assertEquals("ESTUDIANTE", asistencia.getTipo());
            assertNotNull(asistencia.getEstudiante());
            assertNull(asistencia.getProfesor());

            // Cambiar a profesor
            asistencia.setProfesor(profesorMock);
            asistencia.setTipo("PROFESOR");
            asistencia.setEstudiante(null);

            assertEquals("PROFESOR", asistencia.getTipo());
            assertNotNull(asistencia.getProfesor());
            assertNull(asistencia.getEstudiante());
        }
    }

    @Test
    @DisplayName("Asistencia debe manejar fechas correctamente")
    void testInmutabilidadReferencias() {
        Date fechaOriginal = new Date();
        asistencia.setFecha(fechaOriginal);

        // Verificar que la fecha se estableció correctamente
        assertEquals(fechaOriginal, asistencia.getFecha());
        
        // Crear una nueva fecha diferente
        Date nuevaFecha = new Date(fechaOriginal.getTime() + 86400000); // +1 día
        asistencia.setFecha(nuevaFecha);
        
        // Verificar que la fecha cambió
        assertEquals(nuevaFecha, asistencia.getFecha());
        assertNotEquals(fechaOriginal, asistencia.getFecha());
    }
} 