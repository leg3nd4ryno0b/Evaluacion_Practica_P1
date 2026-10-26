package ec.edu.taller;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GestorIncidentesExisteTest {
    @Test
    void existeDebeEncontrarCodigosConDistintasPrioridades() {
        GestorIncidentes gestor = new GestorIncidentes(10);
        assertTrue(gestor.registrarIncidente(new IncidenteSeguridad("INC-001", "OTRO", "Uno", 5)));
        assertTrue(gestor.registrarIncidente(new IncidenteSeguridad("INC-002", "OTRO", "Dos", 1)));
        assertTrue(gestor.registrarIncidente(new IncidenteSeguridad("INC-003", "OTRO", "Tres", 3)));
        assertTrue(gestor.existeIncidente("INC-001"));
        assertTrue(gestor.existeIncidente("INC-002"));
        assertTrue(gestor.existeIncidente("INC-003"));
        assertEquals(3, gestor.contarIncidentesPendientes());
        assertEquals("INC-002", gestor.consultarSiguienteIncidente().getCodigo());
    }

    @Test
    void existeDebeRetornarFalseEnColaVacia() {
        assertFalse(new GestorIncidentes(10).existeIncidente("INC-001"));
    }

    @Test
    void existeDebeRetornarFalseSiCodigoNoEstaRegistrado() {
        GestorIncidentes gestor = new GestorIncidentes(10);
        assertTrue(gestor.registrarIncidente(new IncidenteSeguridad("INC-001", "MALWARE", "Virus", 1)));
        assertFalse(gestor.existeIncidente("INC-999"));
        assertEquals(1, gestor.contarIncidentesPendientes());
    }

    @Test
    void existeDebeRechazarCodigoNuloVacioOSoloEspacios() {
        GestorIncidentes gestor = new GestorIncidentes(10);
        assertTrue(gestor.registrarIncidente(new IncidenteSeguridad("INC-001", "OTRO", "Evento", 1)));
        assertFalse(gestor.existeIncidente(null));
        assertFalse(gestor.existeIncidente(""));
        assertFalse(gestor.existeIncidente("   "));
        assertEquals(1, gestor.contarIncidentesPendientes());
    }

    @Test
    void existeDebeDejarDeEncontrarUnIncidenteAtendido() {
        GestorIncidentes gestor = new GestorIncidentes(10);
        assertTrue(gestor.registrarIncidente(new IncidenteSeguridad("INC-001", "OTRO", "Uno", 5)));
        assertTrue(gestor.registrarIncidente(new IncidenteSeguridad("INC-002", "OTRO", "Dos", 1)));
        assertEquals("INC-002", gestor.atenderSiguienteIncidente().getCodigo());
        assertFalse(gestor.existeIncidente("INC-002"));
        assertTrue(gestor.existeIncidente("INC-001"));
    }
}
