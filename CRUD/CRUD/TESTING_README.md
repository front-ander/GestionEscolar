# Pruebas Unitarias - Clase Asistencia

Este documento describe las pruebas unitarias implementadas para la clase `Asistencia` del sistema CRUD.

## Estructura de Pruebas

### Archivos de Pruebas

1. **`AsistenciaTest.java`** - Pruebas unitarias básicas
   - Pruebas de constructores
   - Pruebas de getters y setters
   - Pruebas de funcionalidad básica
   - Pruebas de casos de uso comunes

2. **`AsistenciaAdvancedTest.java`** - Pruebas unitarias avanzadas
   - Pruebas con Mockito
   - Pruebas parametrizadas
   - Pruebas de casos límite
   - Pruebas de integración
   - Pruebas de validación de datos

## Cómo Ejecutar las Pruebas

### Opción 1: Usando Maven desde la línea de comandos

```bash
# Navegar al directorio del proyecto
cd CRUD/CRUD

# Ejecutar todas las pruebas
mvn test

# Ejecutar solo las pruebas de la clase Asistencia
mvn test -Dtest=AsistenciaTest
mvn test -Dtest=AsistenciaAdvancedTest

# Ejecutar una prueba específica
mvn test -Dtest=AsistenciaTest#testConstructorPorDefecto

# Ejecutar pruebas con reporte detallado
mvn test -Dtest=AsistenciaTest,AsistenciaAdvancedTest
```

### Opción 2: Usando IDE (IntelliJ IDEA, Eclipse, VS Code)

1. **IntelliJ IDEA:**
   - Click derecho en el archivo de prueba
   - Seleccionar "Run 'AsistenciaTest'"
   - O usar Ctrl+Shift+F10

2. **Eclipse:**
   - Click derecho en el archivo de prueba
   - Seleccionar "Run As" > "JUnit Test"

3. **VS Code:**
   - Instalar extensión "Extension Pack for Java"
   - Click en los iconos de "Run Test" junto a cada método

### Opción 3: Ejecutar desde el IDE con Maven

```bash
# Ejecutar con reporte de cobertura
mvn clean test jacoco:report

# Ejecutar con debug
mvn test -X
```

## Cobertura de Pruebas

### Pruebas Básicas (`AsistenciaTest.java`)

- ✅ Constructor por defecto
- ✅ Constructor con parámetros
- ✅ Getters y setters para todos los campos
- ✅ Configuración para estudiantes
- ✅ Configuración para profesores
- ✅ Estados de asistencia (PRESENTE, AUSENTE, TARDANZA)
- ✅ Manejo de valores nulos
- ✅ Cambio de valores

### Pruebas Avanzadas (`AsistenciaAdvancedTest.java`)

- ✅ Validación de datos con pruebas parametrizadas
- ✅ Pruebas con Mockito para dependencias
- ✅ Manejo de fechas (pasado, presente, futuro)
- ✅ Casos límite (strings vacíos, valores extremos)
- ✅ Pruebas de integración
- ✅ Inmutabilidad de referencias

## Dependencias Utilizadas

El proyecto ya incluye todas las dependencias necesarias en `pom.xml`:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
```

Esta dependencia incluye:
- JUnit 5 (Jupiter)
- Mockito
- AssertJ
- Spring Test
- JSONassert
- JSONPath

## Anotaciones de JUnit 5 Utilizadas

- `@Test` - Marca un método como prueba
- `@DisplayName` - Proporciona nombres descriptivos para las pruebas
- `@BeforeEach` - Ejecuta antes de cada prueba
- `@Nested` - Agrupa pruebas relacionadas
- `@ParameterizedTest` - Pruebas con múltiples valores de entrada
- `@ValueSource` - Proporciona valores para pruebas parametrizadas
- `@CsvSource` - Proporciona datos CSV para pruebas parametrizadas

## Assertions Utilizadas

- `assertNotNull()` - Verifica que un objeto no sea nulo
- `assertNull()` - Verifica que un objeto sea nulo
- `assertEquals()` - Verifica igualdad de valores
- `assertNotEquals()` - Verifica que valores sean diferentes
- `assertTrue()` - Verifica que una condición sea verdadera
- `assertFalse()` - Verifica que una condición sea falsa

## Mockito Utilizado

- `mock()` - Crea un mock de una clase
- `when()` - Define el comportamiento del mock
- `verify()` - Verifica que se llamen métodos del mock
- `times()` - Especifica el número de veces que se debe llamar un método

## Ejemplos de Uso

### Ejecutar una prueba específica

```bash
# Ejecutar solo la prueba del constructor
mvn test -Dtest=AsistenciaTest#testConstructorPorDefecto

# Ejecutar todas las pruebas de validación
mvn test -Dtest=AsistenciaAdvancedTest$ValidacionDatosTest
```

### Ver el reporte de pruebas

Después de ejecutar `mvn test`, puedes encontrar el reporte en:
```
target/surefire-reports/
```

### Ejecutar con cobertura de código

```bash
mvn clean test jacoco:report
```

El reporte de cobertura estará en:
```
target/site/jacoco/
```

## Consejos para Desarrollo

1. **Ejecuta las pruebas frecuentemente** durante el desarrollo
2. **Mantén las pruebas actualizadas** cuando cambies la clase Asistencia
3. **Usa nombres descriptivos** para las pruebas
4. **Agrupa pruebas relacionadas** usando `@Nested`
5. **Usa pruebas parametrizadas** para casos similares
6. **Mockea dependencias externas** para aislar las pruebas

## Troubleshooting

### Error: "No tests found"

```bash
# Asegúrate de estar en el directorio correcto
cd CRUD/CRUD

# Limpia y recompila
mvn clean compile test-compile
```

### Error: "Mockito not found"

```bash
# Verifica que spring-boot-starter-test esté en el pom.xml
# Si no está, agrega la dependencia
```

### Error: "JUnit 5 not found"

```bash
# Verifica que estés usando JUnit 5 (Jupiter)
# No JUnit 4
```

## Próximos Pasos

Para expandir las pruebas, considera:

1. **Pruebas de persistencia** - Probar con `@DataJpaTest`
2. **Pruebas de servicio** - Probar `AsistenciaService`
3. **Pruebas de controlador** - Probar `AsistenciaController`
4. **Pruebas de integración** - Probar flujos completos
5. **Pruebas de rendimiento** - Probar con grandes volúmenes de datos 