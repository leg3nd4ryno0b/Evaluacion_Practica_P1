package ec.edu.taller;

import javax.swing.*;

public class InterfazIncidentes {

    private JPanel panelPrincipal;
    private JTabbedPane pestañasPrincipal;
    private JPanel panelIngreso;
    private JPanel panelGestion;
    private JTextField txtCodigo;
    private JComboBox cboTipo;
    private JTextArea txtDescripcion;
    private JSpinner spiPrioridad;
    private JLabel lblEstadoInicial;
    private JButton btnRegistrar;
    private JLabel lblPendientes;
    private JLabel lblDisponibles;
    private JLabel lblCapacidad;
    private JLabel lblCodigoActual;
    private JLabel lblEstadoActual;
    private JLabel lblTipoActual;
    private JTextArea txtDescripcionActual;
    private JLabel lblPrioridadActual;
    private JButton btnSiguiente;
    private JButton btnAtender;
    private JButton btnListar;
    private JLabel lblTituloListado;
    private JTextArea txtListado;

    private final GestorIncidentes gestor = new GestorIncidentes(10, true);

    public InterfazIncidentes() {

        cboTipo.setModel(new DefaultComboBoxModel<>(
                new String[]{
                        "PHISHING",
                        "MALWARE",
                        "ACCESO_NO_AUTORIZADO",
                        "FUGA_DATOS",
                        "OTRO"
                }
        ));

        spiPrioridad.setModel(new SpinnerNumberModel(
                Integer.valueOf(1),
                null,
                null,
                Integer.valueOf(1)
        ));

        txtDescripcion.setText("");
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);

        txtDescripcionActual.setEditable(false);
        txtDescripcionActual.setLineWrap(true);
        txtDescripcionActual.setWrapStyleWord(true);

        txtListado.setEditable(false);
        txtListado.setLineWrap(true);
        txtListado.setWrapStyleWord(true);

        lblEstadoInicial.setText("Estado inicial: PENDIENTE");

        // Registrar un incidente.
        btnRegistrar.addActionListener(e -> registrarIncidente());

        // Consultar el siguiente sin retirarlo.
        btnSiguiente.addActionListener(e -> {

            IncidenteSeguridad siguiente =
                    gestor.consultarSiguienteIncidente();

            mostrarIncidente(siguiente);
        });

        // Retirar el siguiente y cambiar su estado a ATENDIDO.
        btnAtender.addActionListener(e -> {

            IncidenteSeguridad atendido =
                    gestor.atenderSiguienteIncidente();

            mostrarIncidente(atendido);

            actualizarContadores();

            if (atendido != null) {
                indicarListadoPendiente();
            }
        });

        // Mostrar los pendientes únicamente al pulsar Listar.
        btnListar.addActionListener(e -> {

            actualizarListado();
            actualizarContadores();
        });

        actualizarContadores();
        indicarListadoPendiente();
        mostrarIncidente(null);
    }

    private void registrarIncidente() {

        try {
            spiPrioridad.commitEdit();

        } catch (java.text.ParseException ex) {

            JOptionPane.showMessageDialog(
                    panelPrincipal,
                    "La prioridad debe ser un número entero.",
                    "Prioridad inválida",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        String codigo = txtCodigo.getText();

        if (codigo == null || codigo.trim().isEmpty()) {

            JOptionPane.showMessageDialog(
                    panelPrincipal,
                    "Debes ingresar un código."
            );

            txtCodigo.requestFocusInWindow();
            return;
        }

        if (gestor.existeIncidente(codigo)) {

            JOptionPane.showMessageDialog(
                    panelPrincipal,
                    "Ya existe un incidente con ese código."
            );

            return;
        }

        if (gestor.consultarEspaciosDisponibles() == 0) {

            JOptionPane.showMessageDialog(
                    panelPrincipal,
                    "La cola está llena. Atiende un incidente para liberar espacio."
            );

            return;
        }

        Object tipoSeleccionado = cboTipo.getSelectedItem();

        if (tipoSeleccionado == null) {

            JOptionPane.showMessageDialog(
                    panelPrincipal,
                    "Selecciona un tipo de incidente."
            );

            return;
        }

        String tipo = tipoSeleccionado.toString();
        String descripcion = txtDescripcion.getText();

        int prioridad =
                ((Number) spiPrioridad.getValue()).intValue();

        IncidenteSeguridad nuevo = new IncidenteSeguridad(
                codigo,
                tipo,
                descripcion,
                prioridad
        );

        boolean registrado = gestor.registrarIncidente(nuevo);

        if (registrado) {

            actualizarContadores();
            indicarListadoPendiente();

            JOptionPane.showMessageDialog(
                    panelPrincipal,
                    "Incidente registrado correctamente."
            );

            txtCodigo.setText("");
            txtDescripcion.setText("");
            cboTipo.setSelectedIndex(0);
            spiPrioridad.setValue(1);

            txtCodigo.requestFocusInWindow();

        } else {

            JOptionPane.showMessageDialog(
                    panelPrincipal,
                    "No se pudo registrar el incidente."
            );
        }
    }

    private void mostrarIncidente(IncidenteSeguridad incidente) {

        if (incidente == null) {

            lblCodigoActual.setText("Código: —");
            lblTipoActual.setText("Tipo: —");
            lblPrioridadActual.setText("Prioridad: —");
            lblEstadoActual.setText("Estado: —");

            txtDescripcionActual.setText(
                    "No hay incidentes pendientes."
            );

            return;
        }

        lblCodigoActual.setText(
                "Código: " + incidente.getCodigo()
        );

        lblTipoActual.setText(
                "Tipo: " + incidente.getTipo()
        );

        lblPrioridadActual.setText(
                "Prioridad: " + incidente.getPrioridad()
        );

        lblEstadoActual.setText(
                "Estado: " + incidente.getEstado()
        );

        txtDescripcionActual.setText(
                "Descripción: " + incidente.getDescripcion()
        );

        txtDescripcionActual.setCaretPosition(0);
    }

    private void actualizarContadores() {

        int pendientes = gestor.contarIncidentesPendientes();
        int disponibles = gestor.consultarEspaciosDisponibles();

        lblPendientes.setText("Pendientes: " + pendientes);
        lblDisponibles.setText("Disponibles: " + disponibles);

        lblCapacidad.setText(
                "Capacidad: " + (pendientes + disponibles)
        );
    }

    private void indicarListadoPendiente() {

        txtListado.setText("Pulsa Listar para consultar los pendientes actualizados.");

        txtListado.setCaretPosition(0);
    }

    private void actualizarListado() {

        StringBuilder texto = new StringBuilder();

        for (IncidenteSeguridad incidente : gestor.listarIncidentes()) {

            texto.append(incidente.toString());
            texto.append("\n\n");
        }

        if (texto.length() == 0) {

            txtListado.setText("No hay incidentes pendientes.");

        } else {

            txtListado.setText(texto.toString());
        }

        txtListado.setCaretPosition(0);
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            JFrame ventana = new JFrame("Gestión de incidentes de seguridad");

            ventana.setContentPane(new InterfazIncidentes().panelPrincipal);

            ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            ventana.setSize(1000, 700);
            ventana.setLocationRelativeTo(null);
            ventana.setVisible(true);
        });
    }
}