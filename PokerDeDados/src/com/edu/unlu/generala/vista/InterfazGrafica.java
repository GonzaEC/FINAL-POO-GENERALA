package com.edu.unlu.generala.vista;

import com.edu.unlu.generala.controladores.ControladorGenerala;
import com.edu.unlu.generala.modelos.Dado;
import com.edu.unlu.generala.modelos.Jugador;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.rmi.RemoteException;
import java.util.List;


public class InterfazGrafica extends JFrame implements IVista {
    private JButton btnAgregarJugador;
    private JButton btnVerJugadores;
    private JButton btnIniciarJuego;
    private JButton btnSalir;
    private JPanel contenedorPrincipal;
    private CardLayout cardLayout;
    private ControladorGenerala controlador;
    private String nombreJugador;
    private DefaultListModel<String> modeloListaJugadores;
    private JList<String> listaJugadoresVisual;

    private PanelMenuDados panelMenuDados;
    private PanelMenuApuestas panelMenuApuestas;

    public InterfazGrafica(ControladorGenerala controlador) {
        setTitle("Juego de Dados");
        setSize(800, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        this.controlador = controlador;
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        cardLayout = new CardLayout();
        contenedorPrincipal = new JPanel(cardLayout);

        JPanel panelMenu = crearPanelMenu();
        JPanel panelAgregarJugador = crearPanelAgregarJugador();
        JPanel panelVerJugadores = crearPanelVerJugadores();

        panelMenuDados = new PanelMenuDados(controlador);
        panelMenuApuestas = new PanelMenuApuestas(controlador);

        contenedorPrincipal.add(panelMenu, "menu");
        contenedorPrincipal.add(panelAgregarJugador, "agregarJugador");
        contenedorPrincipal.add(panelVerJugadores, "verJugadores");
        contenedorPrincipal.add(panelMenuDados, "dados");
        contenedorPrincipal.add(panelMenuApuestas, "apuestas");

        setContentPane(contenedorPrincipal);
    }


    private JPanel crearPanelMenu() {
        PanelFondo panelFondo = new PanelFondo("src/com/edu/unlu/generala/utils/fondoMesa.jpg");
        panelFondo.setLayout(new GridBagLayout());

        JPanel panelBotones = new JPanel();
        panelBotones.setOpaque(false);
        panelBotones.setLayout(new BoxLayout(panelBotones, BoxLayout.Y_AXIS));

        btnAgregarJugador = new BotonRedondeado("Agregar Jugador", 50);
        estilizarBoton(btnAgregarJugador);
        configurarBoton(btnAgregarJugador);
        btnAgregarJugador.addActionListener(e -> cardLayout.show(contenedorPrincipal, "agregarJugador"));

        btnVerJugadores = new BotonRedondeado("Ver Jugadores", 50);
        estilizarBoton(btnVerJugadores);
        configurarBoton(btnVerJugadores);
        btnVerJugadores.addActionListener(e -> {
            actualizarAreaJugadores();
            cardLayout.show(contenedorPrincipal, "verJugadores");
        });

        JButton btnHistorial = new BotonRedondeado("Salon de la Fama", 50);
        estilizarBoton(btnHistorial);
        configurarBoton(btnHistorial);
        btnHistorial.addActionListener(e -> {
            List<com.edu.unlu.generala.modelos.RegistroPartida> lista = controlador.getHistorial();
            VistaHistorial ventana = new VistaHistorial(this, lista);
            ventana.setVisible(true);
        });

        btnIniciarJuego = new BotonRedondeado("Iniciar Juego", 50);
        estilizarBoton(btnIniciarJuego);
        configurarBoton(btnIniciarJuego);
        btnIniciarJuego.addActionListener(e -> iniciarPartidaDirecto());

        btnSalir = new BotonRedondeado("Salir", 50);
        estilizarBoton(btnSalir);
        configurarBoton(btnSalir);
        btnSalir.addActionListener(e -> System.exit(0));

        panelBotones.add(btnAgregarJugador);
        panelBotones.add(Box.createRigidArea(new Dimension(0, 25)));

        panelBotones.add(btnVerJugadores);
        panelBotones.add(Box.createRigidArea(new Dimension(0, 25)));

        panelBotones.add(btnHistorial); // <--- Aquí aparece el nuevo botón
        panelBotones.add(Box.createRigidArea(new Dimension(0, 25)));

        panelBotones.add(btnIniciarJuego);
        panelBotones.add(Box.createRigidArea(new Dimension(0, 25)));

        panelBotones.add(btnSalir);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        panelFondo.add(panelBotones, gbc);

        return panelFondo;
    }

    private void actualizarAreaJugadores() {
        modeloListaJugadores.clear();
        List<Jugador> jugadores = controlador.getJugadores();

        if (jugadores.isEmpty()) {
            modeloListaJugadores.addElement("No hay jugadores todavia...");
        } else {
            for (Jugador j : jugadores) {
                modeloListaJugadores.addElement(j.getNombre());
            }
        }
    }


    private JPanel crearPanelAgregarJugador() {
        PanelFondo panelFondo = new PanelFondo("src/com/edu/unlu/generala/utils/fondoMesa.jpg");
        panelFondo.setLayout(new GridBagLayout());

        JPanel panelTarjeta = new JPanel();
        panelTarjeta.setLayout(new BoxLayout(panelTarjeta, BoxLayout.Y_AXIS));
        panelTarjeta.setBackground(new Color(0, 0, 0, 150));
        panelTarjeta.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        JLabel lblTitulo = new JLabel("NUEVO JUGADOR");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitulo.setForeground(new Color(255, 215, 0));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField txtNombre = new JTextField(15);
        txtNombre.setMaximumSize(new Dimension(300, 40));
        txtNombre.setFont(new Font("Arial", Font.PLAIN, 16));
        txtNombre.setHorizontalAlignment(JTextField.CENTER);
        txtNombre.setBorder(BorderFactory.createLineBorder(new Color(255, 215, 0), 2));
        txtNombre.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        panelBotones.setOpaque(false);
        panelBotones.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnConfirmar = new BotonRedondeado("Unirse", 40);
        estilizarBoton(btnConfirmar);

        JButton btnVolver = new BotonRedondeado("Cancelar", 40);
        estilizarBoton(btnVolver);
        btnVolver.setBackground(new Color(150, 50, 50));

        panelBotones.add(btnVolver);
        panelBotones.add(btnConfirmar);

        btnConfirmar.addActionListener(e -> {
            String nombre = txtNombre.getText().trim();
            if (!nombre.isEmpty()) {
                agregarJugador(nombre);
                txtNombre.setText("");
                cardLayout.show(contenedorPrincipal, "menu");
            } else {
                JOptionPane.showMessageDialog(this, "¡El nombre no puede estar vacio!", "Atencion", JOptionPane.WARNING_MESSAGE);
            }
        });

        btnVolver.addActionListener(e -> {
            txtNombre.setText("");
            cardLayout.show(contenedorPrincipal, "menu");
        });

        panelTarjeta.add(lblTitulo);
        panelTarjeta.add(Box.createRigidArea(new Dimension(0, 20)));
        panelTarjeta.add(txtNombre);
        panelTarjeta.add(Box.createRigidArea(new Dimension(0, 25)));
        panelTarjeta.add(panelBotones);

        panelFondo.add(panelTarjeta);

        return panelFondo;
    }

    private void agregarJugador(String nombreJugador) {
        if (!nombreJugador.isEmpty()) {
            if (controlador.existeJugador(nombreJugador)) {
                mostrarMensaje("El nombre del jugador ya existe. Por favor, ingrese un nombre diferente.");
            } else {
                controlador.agregarJugador(nombreJugador);
                controlador.setNombreJugadorLocal(nombreJugador);
                mostrarMensaje("Jugador " + nombreJugador + " agregado.");
                this.nombreJugador = nombreJugador;
            }
        } else {
            mostrarMensaje("Por favor, ingrese un nombre válido.");
        }
    }

    private JPanel crearPanelVerJugadores() {
        PanelFondo panelFondo = new PanelFondo("src/com/edu/unlu/generala/utils/fondoMesa.jpg");
        panelFondo.setLayout(new GridBagLayout());

        JPanel panelTarjeta = new JPanel();
        panelTarjeta.setLayout(new BoxLayout(panelTarjeta, BoxLayout.Y_AXIS));
        panelTarjeta.setBackground(new Color(0, 0, 0, 180));
        panelTarjeta.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblTitulo = new JLabel("JUGADORES EN LA MESA");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        modeloListaJugadores = new DefaultListModel<>();
        listaJugadoresVisual = new JList<>(modeloListaJugadores);
        listaJugadoresVisual.setOpaque(false);
        listaJugadoresVisual.setBackground(new Color(0,0,0,0));
        listaJugadoresVisual.setForeground(Color.WHITE);
        listaJugadoresVisual.setFont(new Font("Arial", Font.PLAIN, 18));
        listaJugadoresVisual.setCellRenderer(new JugadorListRenderer());

        JScrollPane scrollPane = new JScrollPane(listaJugadoresVisual);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(107, 76, 59), 1));
        scrollPane.setPreferredSize(new Dimension(300, 250));

        JButton btnVolver = new BotonRedondeado("Volver al Menú", 40);
        estilizarBoton(btnVolver);
        btnVolver.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnVolver.addActionListener(e -> cardLayout.show(contenedorPrincipal, "menu"));

        panelTarjeta.add(lblTitulo);
        panelTarjeta.add(Box.createRigidArea(new Dimension(0, 15)));
        panelTarjeta.add(scrollPane);
        panelTarjeta.add(Box.createRigidArea(new Dimension(0, 20)));
        panelTarjeta.add(btnVolver);

        panelFondo.add(panelTarjeta);
        return panelFondo;
    }

    private void iniciarPartidaDirecto() {
        if (controlador.cantidadJugadores() < 2) {
            JOptionPane.showMessageDialog(this, "Necesitas que haya dos jugadores para iniciar la partida.");
            return;
        }

        try {
            controlador.iniciar();
        } catch (RemoteException e) {
            mostrarMensaje("Error al iniciar la partida.");
            return;
        }

        cardLayout.show(contenedorPrincipal, "dados");
        panelMenuDados.actualizarEstadoYVista();
    }


    private void estilizarBoton(JButton boton) {
        Color marronMedio = new Color(107, 76, 59);
        boton.setBackground(marronMedio);
        boton.setForeground(Color.WHITE);
        boton.setFont(new Font("Arial", Font.BOLD, 16));
        boton.setFocusPainted(false);
    }

    private void configurarBoton(JButton boton) {
        boton.setPreferredSize(new Dimension(200, 50));
        boton.setMaximumSize(new Dimension(200, 50));
        boton.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    @Override
    public void setControlador(ControladorGenerala controlador) {
        this.controlador= controlador;
    }

    @Override
    public void mostrarMensaje(String mensaje) {
        SwingUtilities.invokeLater(() -> {
            NotificacionJuego.info(this, mensaje);
        });

    }
    public void iniciar() {
        setVisible(true);
    }

    @Override
    public void mostrarMenuJugadorDados() {
        SwingUtilities.invokeLater(() -> {
            cardLayout.show(contenedorPrincipal, "dados");
            if (panelMenuDados != null) {
                panelMenuDados.actualizarEstadoYVista();
            }
        });
    }

    @Override
    public void mostrarMenuApuestas() {
        SwingUtilities.invokeLater(() -> {
            cardLayout.show(contenedorPrincipal, "apuestas");

            if (panelMenuApuestas != null) {
                panelMenuApuestas.actualizar();
            }
        });
    }


    public void mostrarGanador() {
        SwingUtilities.invokeLater(() -> {
            Jugador ganador = controlador.determinarGanador();
            int pozoGanado = 0;
            try {
                pozoGanado = controlador.getPozo();
            } catch (Exception e) { e.printStackTrace(); }

            if (ganador != null) {
                PanelGanador panelGanador = new PanelGanador(this, ganador, pozoGanado);
                panelGanador.setVisible(true);
            }
            volverAlMenuPrincipal();
        });
    }

    private void volverAlMenuPrincipal() {
        cardLayout.show(contenedorPrincipal, "menu");
    }

    @Override
    public void mostrarTirada(int[] valoresDados) {
        SwingUtilities.invokeLater(() -> {
            if (panelMenuDados != null) {
                panelMenuDados.actualizarDados(valoresDados);
                panelMenuDados.actualizarEstadoYVista();
            }
        });
    }
    @Override
    public void irAVentanaMenuApuestas() {
        mostrarMenuApuestas();
    }

    @Override
    public void actualizarVista() {
        SwingUtilities.invokeLater(() -> {
            if (panelMenuDados != null) {
                panelMenuDados.actualizarEstadoYVista();
            }
        });
    }

    public void notificarMensaje(String string) {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(null, string);
        });
    }

    @Override
    public void limpiarMesa() {
        SwingUtilities.invokeLater(() -> {
            if (panelMenuDados != null) {
                panelMenuDados.reiniciarVisual();
            }
        });
    }



    // Clase interna para estilizar cada fila de la lista
    private static class JugadorListRenderer extends DefaultListCellRenderer {
        private static final Icon ICONO_JUGADOR;

        static {
            ImageIcon tempIcon = null;
            try {
                java.net.URL imgURL = InterfazGrafica.class.getResource("/com/edu/unlu/generala/utils/iconoJugador.png");
                if (imgURL != null) {
                    ImageIcon original = new ImageIcon(imgURL);
                    Image scaled = original.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
                    tempIcon = new ImageIcon(scaled);
                } else {
                    System.err.println("No se encontró el ícono del jugador.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            ICONO_JUGADOR = tempIcon;
        }


        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            label.setOpaque(isSelected);
            if (isSelected) {
                label.setBackground(new Color(255, 215, 0, 100));
                label.setForeground(Color.WHITE);
            } else {
                label.setForeground(Color.LIGHT_GRAY);
            }

            label.setFont(new Font("Arial", Font.BOLD, 16));
            label.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

            if (ICONO_JUGADOR != null) {
                label.setIcon(ICONO_JUGADOR);
                label.setIconTextGap(15);
            }

            label.setText(value.toString());

            return label;
        }
    }


}
