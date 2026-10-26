package ec.edu.taller;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class GestorIncidentesAtenderTest {
    @Test
    void atenderDebeRespetarPrioridadNumericaYActualizarEstado() {
        GestorIncidentes gestor = new GestorIncidentes(10);
        assertTrue(gestor.registrarIncidente(new IncidenteSeguridad("INC-001", "PHISHING", "Correo", 5)));
        assertTrue(gestor.registrarIncidente(new IncidenteSeguridad("INC-002", "MALWARE", "Virus", 1)));
        assertTrue(gestor.registrarIncidente(new IncidenteSeguridad("INC-003", "FUGA_DATOS", "Datos", 3)));
        String[] codigos = {"INC-002", "INC-003", "INC-001"};
        int[] prioridades = {1, 3, 5};
        for (int i = 0; i < codigos.length; i++) {
            IncidenteSeguridad atendido = gestor.atenderSiguienteIncidente();
            assertNotNull(atendido);
            assertEquals(codigos[i], atendido.getCodigo());
            assertEquals(prioridades[i], atendido.getPrioridad());
            assertEquals("ATENDIDO", atendido.getEstado());
            assertFalse(gestor.existeIncidente(codigos[i]));
            assertEquals(2 - i, gestor.contarIncidentesPendientes());
            assertEquals(8 + i, gestor.consultarEspaciosDisponibles());
        }
        assertNull(gestor.atenderSiguienteIncidente());
        assertNull(gestor.consultarSiguienteIncidente());
    }

    @Test
    void atenderEmpatesDebeConservarTodosSinExigirOrdenEntreIguales() {
        GestorIncidentes gestor = new GestorIncidentes(10);
        assertTrue(gestor.registrarIncidente(new IncidenteSeguridad("INC-001", "OTRO", "Uno", 2)));
        assertTrue(gestor.registrarIncidente(new IncidenteSeguridad("INC-002", "OTRO", "Dos", 1)));
        assertTrue(gestor.registrarIncidente(new IncidenteSeguridad("INC-003", "OTRO", "Tres", 1)));
        assertTrue(gestor.registrarIncidente(new IncidenteSeguridad("INC-004", "OTRO", "Cuatro", 3)));
        Set<String> empatados = new HashSet<>();
        for (int i = 0; i < 2; i++) {
            IncidenteSeguridad atendido = gestor.atenderSiguienteIncidente();
            assertNotNull(atendido);
            assertEquals(1, atendido.getPrioridad());
            assertEquals("ATENDIDO", atendido.getEstado());
            assertTrue(empatados.add(atendido.getCodigo()), "No se debe atender dos veces el mismo codigo");
        }
        assertEquals(new HashSet<>(Arrays.asList("INC-002", "INC-003")), empatados);
        assertEquals("INC-001", gestor.atenderSiguienteIncidente().getCodigo());
        assertEquals("INC-004", gestor.atenderSiguienteIncidente().getCodigo());
        assertEquals(0, gestor.contarIncidentesPendientes());
    }

    @Test
    void atenderNoDebeCambiarElEstadoDeOtrosPendientes() {
        GestorIncidentes gestor = new GestorIncidentes(10);
        IncidenteSeguridad pendiente = new IncidenteSeguridad("INC-001", "OTRO", "Uno", 5);
        assertTrue(gestor.registrarIncidente(pendiente));
        String estadoAnterior = pendiente.getEstado();
        assertTrue(gestor.registrarIncidente(new IncidenteSeguridad("INC-002", "OTRO", "Dos", 1)));
        assertEquals("INC-002", gestor.atenderSiguienteIncidente().getCodigo());
        assertEquals(estadoAnterior, pendiente.getEstado());
        assertEquals(estadoAnterior, gestor.consultarSiguienteIncidente().getEstado());
        assertEquals("INC-001", gestor.consultarSiguienteIncidente().getCodigo());
    }

    @Test
    void atenderConColaVaciaDebeRetornarNullSinAlterarContadores() {
        GestorIncidentes gestor = new GestorIncidentes(10);
        assertNull(gestor.atenderSiguienteIncidente());
        assertNull(gestor.atenderSiguienteIncidente());
        assertEquals(0, gestor.contarIncidentesPendientes());
        assertEquals(10, gestor.consultarEspaciosDisponibles());
    }
}
