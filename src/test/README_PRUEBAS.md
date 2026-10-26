# Pruebas actualizadas de Programación III

## Criterios aplicados

El enunciado conserva referencias a FIFO, pero también solicita una cola con
prioridad y que el listado mantenga la prioridad. Para esta actualización, el
docente confirmó que **un menor número representa una mayor prioridad**.
Ejemplo: si llegan incidentes con prioridades 5, 1 y 3, se consultan, atienden
y listan en el orden 1, 3 y 5.

- Las prioridades del constructor son enteros, en correspondencia con el modelo actual.
- Los empates no exigen FIFO ni otro criterio de desempate; sí deben conservarse
  todos los incidentes y atenderse antes que los de menor prioridad.
- Se utiliza la capacidad recibida por el constructor. Se comprueba expresamente
  el caso de 10 elementos: aceptar el décimo y rechazar el undécimo.
- Los códigos nulos, vacíos y compuestos solo por espacios se rechazan, como en
  las pruebas anteriores. No se agregan reglas de mayúsculas o normalización.
- `consultarSiguienteIncidente()` y `listarIncidentes()` deben preservar los
  pendientes, sus estados y la disponibilidad de espacios.
- `atenderSiguienteIncidente()` debe retirar el incidente y devolverlo con estado
  `ATENDIDO`.
- Una lista devuelta puede ser una copia modificable o una lista inmodificable;
  ninguna debe permitir vaciar la cola original a través de `clear()`.
- No se impone `PENDIENTE` como estado inicial porque el enunciado lo recomienda,
  pero no lo establece como obligatorio. Tampoco se inventan rangos de prioridad,
  reglas para capacidades inválidas o validaciones adicionales de tipo y descripción.

## Casos de prueba

| Clase conservada para GitHub Classroom | Casos | Comprobaciones |
| --- | ---: | --- |
| `GestorIncidentesRegistrarTest` | 7 | Registro válido; incidente nulo sin alterar pendientes; código nulo/vacío/espacios; duplicado con datos diferentes; límite de 10; capacidad configurada de 2; reutilización de espacio tras atender. |
| `GestorIncidentesExisteTest` | 5 | Encontrar códigos con distintas prioridades sin alterar la cola; cola vacía; código inexistente; consultas nulas/vacías/espacios; exclusión del atendido. |
| `GestorIncidentesConsultarTest` | 4 | Elegir prioridad 1 aunque llegue después; consultas repetidas sin eliminar ni cambiar estado; cola vacía; actualización del siguiente tras registro y atención. |
| `GestorIncidentesAtenderTest` | 4 | Atención completa en orden 1, 3, 5 con estado y contadores; empates sin imponer desempate; preservar el estado de otros pendientes; cola vacía sin alterar contadores. |
| `GestorIncidentesContarTest` | 4 | Cero inicial; incremento hasta 10; estabilidad ante consultas/listados/rechazos; decremento hasta cero y atención adicional en vacío. |
| `GestorIncidentesEspaciosTest` | 4 | Capacidad inicial configurable; disminución hasta cero y rechazo sin valores negativos; recuperación y reutilización; ejemplo de 2 pendientes en capacidad 10. |
| `GestorIncidentesListarTest` | 6 | Lista vacía no nula; listado completo ordenado y repetible sin alterar la cola; empates; independencia estructural de la lista; conservación de estados; exclusión de atendidos. |
| **Total** | **34** | |

El caso de listado con prioridades de entrada `5, 1, 4, 2, 3` detecta que iterar
directamente una `PriorityQueue` o convertirla directamente en lista no garantiza
el orden de prioridad de todos sus elementos.

## Ejecución

Con JDK 17 y Maven, desde la raíz del proyecto:

```bash
mvn test
```

Para ejecutar solo una categoría, se mantienen los nombres que utiliza el flujo
original de GitHub Classroom, por ejemplo:

```bash
mvn -Dtest=GestorIncidentesListarTest test
```

## Estado del proyecto recibido y verificación

**Las pruebas no pueden compilar contra el código base recibido**: faltan los
métodos `getCodigo()`, `getPrioridad()` y `getEstado()` de `IncidenteSeguridad`,
requeridos por el contrato de getters del enunciado. Además, los métodos de
`GestorIncidentes` son plantillas pendientes de implementación y su cola se
inicializa en `null`. Una vez completados los accesores, todavía será necesario
implementar la lógica para que las pruebas pasen. No se modificó ese código.

Se comprobó la compilación con Java 17 y compatibilidad de fuentes Java 8, como
configura actualmente el `pom.xml`. Las prioridades de texto de las pruebas
anteriores se sustituyeron por enteros. La compilación del proyecto recibido
ahora se detiene por los accesores faltantes, no por el tipo del constructor.

Para verificar la propia batería, se ejecutaron sus 34 casos con JUnit Platform
1.10.2 / JUnit Jupiter 5.10.2 sobre una implementación de referencia temporal,
separada del proyecto y excluida del ZIP: **34 aprobados, 0 fallidos**. También
pasaron los 34 con una lista devuelta inmodificable. Esto valida la batería;
no significa que la plantilla recibida esté implementada o que apruebe los tests.

Se introdujeron por separado siete defectos en esa referencia y la batería
detectó todos: FIFO, prioridad invertida, listado sin ordenar, listado que vacía
la cola, ausencia de límite de capacidad, falta del cambio a `ATENDIDO` y
aceptación de códigos duplicados. Maven no está instalado en el entorno de
revisión; la ejecución de verificación utilizó el compilador de Java y JUnit
Console, sin cambiar las dependencias del proyecto.

## Alcance de los cambios

Solo se modificaron las siete clases existentes dentro de `src/test/java` y
se añadió este documento en `src/test`. Los archivos de desarrollo, el
enunciado, `pom.xml` y la configuración de GitHub Classroom se conservaron
idénticos al ZIP original. La interfaz Swing requiere revisión manual y no se
incluye en estas pruebas unitarias de lógica.
