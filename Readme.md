# Taller Evaluación: Estructuras de Datos - Cola de Incidentes de Seguridad

## Contexto del problema

Una empresa ha detectado varios intentos de acceso sospechosos en sus sistemas internos.  
Para organizar la atención de estos eventos, el equipo de seguridad informática necesita un sistema que administre una **cola de incidentes**.

La cola debe funcionar bajo el principio **FIFO**: el primer incidente registrado debe ser el primero en ser atendido.

Cada incidente tendrá:

- Código único del incidente.
- Tipo de incidente.
- Descripción.
- Nivel de prioridad.
- Estado.

Ejemplos de tipos de incidente:

- `PHISHING`
- `MALWARE`
- `ACCESO_NO_AUTORIZADO`
- `FUGA_DATOS`
- `OTRO`

## Objetivo del taller

Desarrollar una solución en Java que utilice una estructura de datos tipo **cola con prioridad** para gestionar incidentes de seguridad informática.

Además, el estudiante debe crear una **interfaz gráfica de usuario en Swing** que permita interactuar con la solución.

## Requisitos técnicos

El proyecto debe usar:

- Java 17.
- Maven.
- JUnit 5.
- Interfaz gráfica con Swing.

## Clase principal de lógica

Debe implementar la clase:

```java
ec.edu.taller.GestorIncidentes
```

Esta clase debe manejar internamente una cola de objetos `IncidenteSeguridad`.

## Métodos obligatorios

### 1. `registrarIncidente(IncidenteSeguridad incidente)`

Registra un incidente en la cola.

Reglas:

- No debe aceptar incidentes `null`.
- No debe aceptar códigos vacíos o nulos.
- No debe aceptar incidentes duplicados por código.
- No debe registrar más incidentes si la cola ya está llena 10 elementos.
- Si el registro es correcto, debe retornar `true`.
- Si no se puede registrar, debe retornar `false`.

---

### 2. `existeIncidente(String codigo)`

Verifica si ya existe un incidente registrado con ese código.

Reglas:

- Debe retornar `true` si el código existe en la cola.
- Debe retornar `false` si el código no existe.
- Debe retornar `false` si el código es nulo o vacío.

---

### 3. `consultarSiguienteIncidente()`

Consulta el siguiente incidente a ser atendido, sin eliminarlo de la cola.

Reglas:

- Debe retornar el primer incidente de la cola.
- Si no hay incidentes, debe retornar `null`.
- No debe modificar la cantidad de incidentes pendientes.

---

### 4. `atenderSiguienteIncidente()`

Atiende el siguiente incidente de la cola.

Reglas:

- Debe eliminar y retornar el primer incidente en la cola.
- Si no hay incidentes, debe retornar `null`.
- El estado del incidente atendido debe cambiar a `ATENDIDO`.

---

### 5. `contarIncidentesPendientes()`

Retorna la cantidad de incidentes pendientes en la cola.

---

### 6. `consultarEspaciosDisponibles()`

Retorna cuántos espacios quedan disponibles en la cola.

Reglas:

- La capacidad máxima debe definirse al crear el gestor.
- Si la capacidad máxima es 10 y hay 2 incidentes pendientes, debe retornar 8.

---

### 7. `listarIncidentes()`

Retorna una lista con todos los incidentes pendientes.

Reglas:

- Debe mantener prioridad.
- No debe vaciar ni modificar la cola original.

## Clase `IncidenteSeguridad`

Debe tener los siguientes atributos:

```java
private String codigo;
private String tipo;
private String descripcion;
private int prioridad;
private String estado;
```

Debe incluir:

- Constructor.
- Getters y setters.
- Método `toString()`.

El estado inicial recomendado para un incidente nuevo es:

```java
"PENDIENTE"
```

## Requisito de interfaz gráfica Swing

El estudiante debe crear una clase:

```java
ec.edu.taller.InterfazIncidentes
```

La interfaz debe realizarla en swing de manera manual permitir registrar, consultar, listar y atender incidentes.

Debe incluir obligatoriamente los siguientes componentes vistos en clase:

- `JLabel`
- `JTextField`
- `JComboBox`
- `JTextArea`
- `JSpinner`
- `JButton`



## Calificación

CLASES IMPLEMENTADAS CON CONDICIONES 4 PUNTOS
INTERFAZ GRAFICA 5 INTEGRADA A CLASES 4 PUNTOS
SUBIDA DEL EJERCICIO AL GITHUB, ENTREGA DEL ENLACE EN EL AULA VIRTUAL 2 PUNTOS. 
LLAMAR AL DOCENTE PARA LA REVISION. 



