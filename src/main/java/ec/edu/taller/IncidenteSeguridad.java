package ec.edu.taller;

public class IncidenteSeguridad {

    private String codigo;
    private String tipo;
    private String descripcion;
    private int prioridad;
    private String estado;

    public IncidenteSeguridad(String codigo, String tipo, String descripcion, int prioridad) {
        this.codigo = codigo;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.prioridad = prioridad;
        this.estado = "PENDIENTE";
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(int prioridad) {
        this.prioridad = prioridad;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return codigo + " | " + tipo + " | " + descripcion + " | Prioridad: " + prioridad + " | Estado: " + estado;
    }
}