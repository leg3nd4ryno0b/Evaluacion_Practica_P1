package ec.edu.taller;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GestorIncidentesEspaciosTest {
    @Test
    void espaciosInicialesDebenCoincidirConCapacidadConfigurada() {
        assertEquals(10, new GestorIncidentes(10).consultarEspaciosDisponibles());
        assertEquals(3, new GestorIncidentes(3).consultarEspaciosDisponibles());
    }

    @Test
    void espaciosDebenDisminuirHastaCeroYNoVolverseNegativos() {
        GestorIncidentes gestor = new GestorIncidentes(10);
        for (int i = 1; i <= 10; i++) {
            assertTrue(gestor.registrarIncidente(new IncidenteSeguridad("INC-" + i, "OTRO", "Evento", i)));
            assertEquals(10 - i, gestor.consultarEspaciosDisponibles());
        }
        assertFalse(gestor.registrarIncidente(new IncidenteSeguridad("INC-11", "OTRO", "Sin espacio", 1)));
        assertEquals(0, gestor.consultarEspaciosDisponibles());
    }

    @Test
    void espaciosDebenRecuperarseTrasAtenderYReutilizarse() {
        GestorIncidentes gestor = new GestorIncidentes(2);
        assertTrue(gestor.registrarIncidente(new IncidenteSeguridad("INC-001", "OTRO", "Uno", 2)));
        assertTrue(gestor.registrarIncidente(new IncidenteSeguridad("INC-002", "OTRO", "Dos", 1)));
        assertEquals(0, gestor.consultarEspaciosDisponibles());
        assertNotNull(gestor.atenderSiguienteIncidente());
        assertEquals(1, gestor.consultarEspaciosDisponibles());
        assertTrue(gestor.registrarIncidente(new IncidenteSeguridad("INC-003", "OTRO", "Tres", 3)));
        assertEquals(0, gestor.consultarEspaciosDisponibles());
        assertNotNull(gestor.atenderSiguienteIncidente());
        assertNotNull(gestor.atenderSiguienteIncidente());
        assertEquals(2, gestor.consultarEspaciosDisponibles());
        assertNull(gestor.atenderSiguienteIncidente());
        assertEquals(2, gestor.consultarEspaciosDisponibles());
    }

    @Test
    void dosPendientesDeDiezDebenDejarOchoEspacios() {
        GestorIncidentes gestor = new GestorIncidentes(10);
        assertTrue(gestor.registrarIncidente(new IncidenteSeguridad("INC-001", "OTRO", "Uno", 4)));
        assertTrue(gestor.registrarIncidente(new IncidenteSeguridad("INC-002", "OTRO", "Dos", 1)));
        assertEquals(8, gestor.consultarEspaciosDisponibles());
        assertNotNull(gestor.consultarSiguienteIncidente());
        assertEquals(2, gestor.listarIncidentes().size());
        assertEquals(8, gestor.consultarEspaciosDisponibles());
    }
}
