package ec.edu.taller;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GestorIncidentesRegistrarTest {
    @Test
    void registrarIncidenteValidoDebeRetornarTrue() {
        GestorIncidentes gestor = new GestorIncidentes(10);
        IncidenteSeguridad incidente = new IncidenteSeguridad("INC-001", "PHISHING", "Correo falso", 3);
        assertTrue(gestor.registrarIncidente(incidente));
        assertEquals(1, gestor.contarIncidentesPendientes());
        assertEquals("INC-001", gestor.consultarSiguienteIncidente().getCodigo());
    }

    @Test
    void registrarNullNoDebeModificarUnaColaConIncidentes() {
        GestorIncidentes gestor = new GestorIncidentes(10);
        assertTrue(gestor.registrarIncidente(new IncidenteSeguridad("INC-001", "MALWARE", "Virus", 2)));
        assertFalse(gestor.registrarIncidente(null));
        assertEquals(1, gestor.contarIncidentesPendientes());
        assertEquals(9, gestor.consultarEspaciosDisponibles());
        assertEquals("INC-001", gestor.consultarSiguienteIncidente().getCodigo());
    }

    @Test
    void registrarCodigoNuloVacioOSoloEspaciosDebeRetornarFalse() {
        GestorIncidentes gestor = new GestorIncidentes(10);
        for (String codigo : new String[] {null, "", "   "}) {
            assertFalse(gestor.registrarIncidente(new IncidenteSeguridad(codigo, "OTRO", "Evento", 1)));
        }
        assertEquals(0, gestor.contarIncidentesPendientes());
        assertEquals(10, gestor.consultarEspaciosDisponibles());
        assertNull(gestor.consultarSiguienteIncidente());
    }

    @Test
    void registrarDuplicadoPorCodigoDebeRechazarloAunqueCambienLosDatos() {
        GestorIncidentes gestor = new GestorIncidentes(10);
        assertTrue(gestor.registrarIncidente(new IncidenteSeguridad("INC-001", "PHISHING", "Original", 5)));
        assertFalse(gestor.registrarIncidente(new IncidenteSeguridad("INC-001", "MALWARE", "Otro evento", 1)));
        assertEquals(1, gestor.contarIncidentesPendientes());
        assertEquals(9, gestor.consultarEspaciosDisponibles());
        assertEquals(5, gestor.consultarSiguienteIncidente().getPrioridad());
    }

    @Test
    void registrarDiezIncidentesDebeAceptarDiezYRechazarElOnce() {
        GestorIncidentes gestor = new GestorIncidentes(10);
        for (int i = 1; i <= 10; i++) {
            assertTrue(gestor.registrarIncidente(new IncidenteSeguridad("INC-" + i, "OTRO", "Evento", i)));
        }
        assertFalse(gestor.registrarIncidente(new IncidenteSeguridad("INC-11", "MALWARE", "Urgente", 1)));
        assertEquals(10, gestor.contarIncidentesPendientes());
        assertEquals(0, gestor.consultarEspaciosDisponibles());
        assertFalse(gestor.existeIncidente("INC-11"));
    }

    @Test
    void registrarDebeRespetarLaCapacidadIndicadaEnElConstructor() {
        GestorIncidentes gestor = new GestorIncidentes(2);
        assertTrue(gestor.registrarIncidente(new IncidenteSeguridad("INC-001", "OTRO", "Uno", 3)));
        assertTrue(gestor.registrarIncidente(new IncidenteSeguridad("INC-002", "OTRO", "Dos", 2)));
        assertFalse(gestor.registrarIncidente(new IncidenteSeguridad("INC-003", "OTRO", "Tres", 1)));
        assertEquals(2, gestor.contarIncidentesPendientes());
        assertEquals(0, gestor.consultarEspaciosDisponibles());
    }

    @Test
    void registrarDespuesDeAtenderDebeReutilizarElEspacio() {
        GestorIncidentes gestor = new GestorIncidentes(1);
        assertTrue(gestor.registrarIncidente(new IncidenteSeguridad("INC-001", "OTRO", "Uno", 3)));
        assertNotNull(gestor.atenderSiguienteIncidente());
        assertTrue(gestor.registrarIncidente(new IncidenteSeguridad("INC-002", "OTRO", "Dos", 1)));
        assertEquals(1, gestor.contarIncidentesPendientes());
        assertEquals(0, gestor.consultarEspaciosDisponibles());
        assertEquals("INC-002", gestor.consultarSiguienteIncidente().getCodigo());
    }
}
