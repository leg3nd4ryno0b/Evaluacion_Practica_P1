package ec.edu.taller;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GestorIncidentesListarTest {
    @Test
    void listarConColaVaciaDebeRetornarListaVaciaNoNula() {
        GestorIncidentes gestor = new GestorIncidentes(10);
        assertNotNull(gestor.listarIncidentes());
        assertTrue(gestor.listarIncidentes().isEmpty());
    }

    @Test
    void listarDebeOrdenarTodaLaListaPorPrioridadSinModificarLaCola() {
        GestorIncidentes gestor = new GestorIncidentes(10);
        // Detecta tambien la iteracion directa (no ordenada) de PriorityQueue.
        int[] prioridades = {5, 1, 4, 2, 3};
        for (int prioridad : prioridades) {
            assertTrue(gestor.registrarIncidente(new IncidenteSeguridad("INC-" + prioridad, "OTRO", "Evento", prioridad)));
        }
        for (int consulta = 0; consulta < 2; consulta++) {
            List<IncidenteSeguridad> lista = gestor.listarIncidentes();
            assertNotNull(lista);
            assertEquals(5, lista.size());
            for (int i = 0; i < lista.size(); i++) {
                assertEquals(i + 1, lista.get(i).getPrioridad());
                assertEquals("INC-" + (i + 1), lista.get(i).getCodigo());
            }
            assertEquals(5, gestor.contarIncidentesPendientes());
            assertEquals(5, gestor.consultarEspaciosDisponibles());
            assertEquals("INC-1", gestor.consultarSiguienteIncidente().getCodigo());
        }
        for (int prioridad = 1; prioridad <= 5; prioridad++) {
            assertEquals("INC-" + prioridad, gestor.atenderSiguienteIncidente().getCodigo());
        }
    }

    @Test
    void listarEmpatesDebeIncluirTodosEnPrioridadSinImponerDesempate() {
        GestorIncidentes gestor = new GestorIncidentes(10);
        assertTrue(gestor.registrarIncidente(new IncidenteSeguridad("INC-001", "OTRO", "Uno", 2)));
        assertTrue(gestor.registrarIncidente(new IncidenteSeguridad("INC-002", "OTRO", "Dos", 1)));
        assertTrue(gestor.registrarIncidente(new IncidenteSeguridad("INC-003", "OTRO", "Tres", 1)));
        List<IncidenteSeguridad> lista = gestor.listarIncidentes();
        assertNotNull(lista);
        assertEquals(3, lista.size());
        assertEquals(1, lista.get(0).getPrioridad());
        assertEquals(1, lista.get(1).getPrioridad());
        Set<String> empatados = new HashSet<>(Arrays.asList(lista.get(0).getCodigo(), lista.get(1).getCodigo()));
        assertEquals(new HashSet<>(Arrays.asList("INC-002", "INC-003")), empatados);
        assertEquals("INC-001", lista.get(2).getCodigo());
        assertEquals(2, lista.get(2).getPrioridad());
        assertEquals(3, gestor.contarIncidentesPendientes());
    }

    @Test
    void modificarLaListaDevueltaNoDebeVaciarLaCola() {
        GestorIncidentes gestor = new GestorIncidentes(10);
        assertTrue(gestor.registrarIncidente(new IncidenteSeguridad("INC-001", "OTRO", "Uno", 2)));
        assertTrue(gestor.registrarIncidente(new IncidenteSeguridad("INC-002", "OTRO", "Dos", 1)));
        List<IncidenteSeguridad> lista = gestor.listarIncidentes();
        assertNotNull(lista);
        assertEquals(2, lista.size());
        // Una copia modificable y una lista inmodificable protegen la cola.
        try {
            lista.clear();
        } catch (UnsupportedOperationException aceptable) {
            // No se exige que la lista devuelta permita cambios.
        }
        assertEquals(2, gestor.contarIncidentesPendientes());
        assertEquals(8, gestor.consultarEspaciosDisponibles());
        assertTrue(gestor.existeIncidente("INC-001"));
        assertTrue(gestor.existeIncidente("INC-002"));
        assertEquals("INC-002", gestor.atenderSiguienteIncidente().getCodigo());
        assertEquals("INC-001", gestor.atenderSiguienteIncidente().getCodigo());
    }

    @Test
    void listarNoDebeCambiarElEstadoDeLosIncidentes() {
        GestorIncidentes gestor = new GestorIncidentes(10);
        IncidenteSeguridad incidente = new IncidenteSeguridad("INC-001", "OTRO", "Evento", 1);
        assertTrue(gestor.registrarIncidente(incidente));
        String estadoAnterior = incidente.getEstado();
        List<IncidenteSeguridad> lista = gestor.listarIncidentes();
        assertNotNull(lista);
        assertEquals(1, lista.size());
        assertEquals(estadoAnterior, lista.get(0).getEstado());
        assertEquals(estadoAnterior, incidente.getEstado());
    }

    @Test
    void listarTrasAtenderDebeExcluirAlAtendidoYConservarLosPendientes() {
        GestorIncidentes gestor = new GestorIncidentes(10);
        assertTrue(gestor.registrarIncidente(new IncidenteSeguridad("INC-001", "OTRO", "Uno", 3)));
        assertTrue(gestor.registrarIncidente(new IncidenteSeguridad("INC-002", "OTRO", "Dos", 1)));
        assertTrue(gestor.registrarIncidente(new IncidenteSeguridad("INC-003", "OTRO", "Tres", 2)));
        assertEquals("INC-002", gestor.atenderSiguienteIncidente().getCodigo());
        List<IncidenteSeguridad> lista = gestor.listarIncidentes();
        assertNotNull(lista);
        assertEquals(2, lista.size());
        assertEquals("INC-003", lista.get(0).getCodigo());
        assertEquals("INC-001", lista.get(1).getCodigo());
        assertEquals(2, gestor.contarIncidentesPendientes());
    }
}
