package ec.edu.taller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

public class GestorIncidentes {

    private Queue<IncidenteSeguridad> colaIncidentes;
    private int capacidadMaxima;

    public GestorIncidentes(int capacidadMaxima) {
        this.capacidadMaxima = capacidadMaxima;

        this.colaIncidentes = new PriorityQueue<>(Comparator.comparingInt(IncidenteSeguridad::getPrioridad));
    }

    public boolean registrarIncidente(IncidenteSeguridad incidente) {

        if (incidente == null) {
            return false;
        }

        String codigo = incidente.getCodigo();

        if (codigo == null || codigo.trim().isEmpty()) {
            return false;
        }

        if (colaIncidentes.size() >= capacidadMaxima) {
            return false;
        }

        if (existeIncidente(codigo)) {
            return false;
        }

        return colaIncidentes.offer(incidente);
    }

    public boolean existeIncidente(String codigo) {

        if (codigo == null || codigo.trim().isEmpty()) {
            return false;
        }

        for (IncidenteSeguridad incidente : colaIncidentes) {

            if (codigo.equals(incidente.getCodigo())) {
                return true;
            }
        }

        return false;
    }

    public IncidenteSeguridad consultarSiguienteIncidente() {
        return colaIncidentes.peek();
    }

    public IncidenteSeguridad atenderSiguienteIncidente() {

        IncidenteSeguridad atendido = colaIncidentes.poll();

        if (atendido != null) {
            atendido.setEstado("ATENDIDO");
        }
        return atendido;
    }

    public int contarIncidentesPendientes() {
        return colaIncidentes.size();
    }

    public int consultarEspaciosDisponibles() {
        return capacidadMaxima - colaIncidentes.size();
    }

    public List<IncidenteSeguridad> listarIncidentes() {

        PriorityQueue<IncidenteSeguridad> copia = new PriorityQueue<>(Comparator.comparingInt(IncidenteSeguridad::getPrioridad));

        copia.addAll(colaIncidentes);

        List<IncidenteSeguridad> listado = new ArrayList<>();

        while (!copia.isEmpty()) {
            listado.add(copia.poll());
        }

        return listado;
    }

    private void predefinirIncidentes() {

        colaIncidentes.add(new IncidenteSeguridad("A1", "FUGA_DATOS", "Datos importantes", 1));
        colaIncidentes.add(new IncidenteSeguridad("A2", "MALWARE", "Virus detectado en una computadora", 2));
        colaIncidentes.add(new IncidenteSeguridad("A3", "PHISHING", "Correo sospechoso ", 3));
        colaIncidentes.add(new IncidenteSeguridad("A4", "ACCESO_NO_AUTORIZADO", "Ingreso desconocido a una cuenta", 1));
        colaIncidentes.add(new IncidenteSeguridad("A5", "OTRO", "Actividad sospechosa en internet", 5));
        colaIncidentes.add(new IncidenteSeguridad("A6", "MALWARE", "Archivo malicioso ", 4));
        colaIncidentes.add(new IncidenteSeguridad("A7", "PHISHING", "Enlace falso ", 3));
        colaIncidentes.add(new IncidenteSeguridad("A8", "FUGA_DATOS", "Documentos privados filtrados", 2));
    }

    public GestorIncidentes(int capacidadMaxima, boolean cargarEjemplos) {

        this(capacidadMaxima);

        if (cargarEjemplos) {

            if (capacidadMaxima < 8) {
                throw new IllegalArgumentException(
                        "Se necesitan al menos 8 espacios para cargar los ejemplos."
                );
            }

            predefinirIncidentes();
        }
    }
}
