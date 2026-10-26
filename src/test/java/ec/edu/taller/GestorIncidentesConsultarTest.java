package ec.edu.taller;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GestorIncidentesConsultarTest {
    @Test
    void consultarDebeElegirElMenorNumeroDePrioridadAunqueLlegueDespues() {
        GestorIncidentes gestor = new GestorIncidentes(10);
        assertTrue(gestor.registrarIncidente(new IncidenteSeguridad("INC-001", "PHISHING", "Correo falso", 5)));
        assertTrue(gestor.registrarIncidente(new IncidenteSeguridad("INC-002", "MALWARE", "Virus", 1)));
        assertTrue(gestor.registrarIncidente(new IncidenteSeguridad("INC-003", "FUGA_DATOS", "Datos", 3)));
        IncidenteSeguridad siguiente = gestor.consultarSiguienteIncidente();
        assertNotNull(siguiente);
        assertEquals("INC-002", siguiente.getCodigo());
        assertEquals(1, siguiente.getPrioridad());
        assertEquals(3, gestor.contarIncidentesPendientes());
        assertEquals(7, gestor.consultarEspaciosDisponibles());
    }

    @Test
    void consultarRepetidamenteNoDebeEliminarNiAtender() {
        GestorIncidentes gestor = new GestorIncidentes(10);
        IncidenteSeguridad incidente = new IncidenteSeguridad("INC-001", "MALWARE", "Virus", 1);
        assertTrue(gestor.registrarIncidente(incidente));
        String estadoAnterior = incidente.getEstado();
        assertEquals("INC-001", gestor.consultarSiguienteIncidente().getCodigo());
        assertEquals("INC-001", gestor.consultarSiguienteIncidente().getCodigo());
        assertEquals(estadoAnterior, incidente.getEstado());
        assertEquals(estadoAnterior, gestor.consultarSiguienteIncidente().getEstado());
        assertEquals(1, gestor.contarIncidentesPendientes());
        assertTrue(gestor.existeIncidente("INC-001"));
    }

    @Test
    void consultarConColaVaciaDebeRetornarNullSinCambiarEspacios() {
        GestorIncidentes gestor = new GestorIncidentes(10);
        assertNull(gestor.consultarSiguienteIncidente());
        assertNull(gestor.consultarSiguienteIncidente());
        assertEquals(0, gestor.contarIncidentesPendientes());
        assertEquals(10, gestor.consultarEspaciosDisponibles());
    }

    @Test
    void consultarDebeActualizarElSiguienteTrasRegistroYAtencion() {
        GestorIncidentes gestor = new GestorIncidentes(10);
        assertTrue(gestor.registrarIncidente(new IncidenteSeguridad("INC-001", "OTRO", "Uno", 4)));
        assertEquals("INC-001", gestor.consultarSiguienteIncidente().getCodigo());
        assertTrue(gestor.registrarIncidente(new IncidenteSeguridad("INC-002", "OTRO", "Dos", 1)));
        assertEquals("INC-002", gestor.consultarSiguienteIncidente().getCodigo());
        assertEquals("INC-002", gestor.atenderSiguienteIncidente().getCodigo());
        assertEquals("INC-001", gestor.consultarSiguienteIncidente().getCodigo());
    }
}
