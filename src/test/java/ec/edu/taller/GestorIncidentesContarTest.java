package ec.edu.taller;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GestorIncidentesContarTest {
    @Test
    void contarEnGestorNuevoDebeRetornarCero() {
        assertEquals(0, new GestorIncidentes(10).contarIncidentesPendientes());
    }

    @Test
    void contarDebeIncrementarsePorCadaRegistroAceptado() {
        GestorIncidentes gestor = new GestorIncidentes(10);
        for (int i = 1; i <= 10; i++) {
            assertTrue(gestor.registrarIncidente(new IncidenteSeguridad("INC-" + i, "OTRO", "Evento", 11 - i)));
            assertEquals(i, gestor.contarIncidentesPendientes());
        }
    }

    @Test
    void contarNoDebeCambiarPorConsultasListadosORegistrosRechazados() {
        GestorIncidentes gestor = new GestorIncidentes(1);
        assertTrue(gestor.registrarIncidente(new IncidenteSeguridad("INC-001", "OTRO", "Uno", 1)));
        assertNotNull(gestor.consultarSiguienteIncidente());
        assertEquals(1, gestor.listarIncidentes().size());
        assertTrue(gestor.existeIncidente("INC-001"));
        assertFalse(gestor.registrarIncidente(null));
        assertFalse(gestor.registrarIncidente(new IncidenteSeguridad("INC-001", "OTRO", "Duplicado", 2)));
        assertFalse(gestor.registrarIncidente(new IncidenteSeguridad("INC-002", "OTRO", "Sin espacio", 1)));
        assertEquals(1, gestor.contarIncidentesPendientes());
    }

    @Test
    void contarDebeDecrementarseHastaCeroAlAtender() {
        GestorIncidentes gestor = new GestorIncidentes(3);
        for (int i = 1; i <= 3; i++) {
            assertTrue(gestor.registrarIncidente(new IncidenteSeguridad("INC-" + i, "OTRO", "Evento", i)));
        }
        for (int restantes = 2; restantes >= 0; restantes--) {
            assertNotNull(gestor.atenderSiguienteIncidente());
            assertEquals(restantes, gestor.contarIncidentesPendientes());
        }
        assertNull(gestor.atenderSiguienteIncidente());
        assertEquals(0, gestor.contarIncidentesPendientes());
    }
}
