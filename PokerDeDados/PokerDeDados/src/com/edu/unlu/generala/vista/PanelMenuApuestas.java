package com.edu.unlu.generala.vista;

import com.edu.unlu.generala.controladores.ControladorGenerala;
import com.edu.unlu.generala.modelos.Jugador;

import javax.swing.*;
import java.awt.*;
import java.rmi.RemoteException;

public class PanelMenuApuestas extends JPanel {

    private ControladorGenerala controlador;

    private JLabel lblJugador;
    private JLabel lblPozo;
    private JLabel lblSaldo;
    private JLabel lblApuestaJugador;
    private JLabel lblApuestaMaxima;

    private JButton btnIgualar;
    private JButton btnSubir;
    private JButton btnPlantarse;

    private static final Dimension TAM_BOTON = new Dimension(280, 50);
    private static final Font FUENTE_LABEL = new Font("Arial", Font.BOLD, 20);
    private static final Color COLOR_FUENTE_LABEL = Color.WHITE;

    public PanelMenuApuestas(ControladorGenerala controlador) {
        this.controlador = controlador;

        setLayout(new BorderLayout());

        PanelFondo panelFondo = new PanelFondo("src/com/edu/unlu/generala/utils/fondoMesa.jpg");
        panelFondo.setLayout(new BoxLayout(panelFondo, BoxLayout.Y_AXIS));
        panelFondo.setBorder(BorderFactory.createEmptyBorder(80, 100, 100, 100));

        lblJugador = new JLabel("Esperando turno de apuestas...");
        estilizarLabel(lblJugador);

        lblSaldo = new JLabel("Tu Saldo: $ -");
        estilizarLabel(lblSaldo);
        lblSaldo.setForeground(new Color(255, 215, 0));
        lblSaldo.setFont(new Font("Arial", Font.BOLD, 24));

        lblPozo = new JLabel("Pozo actual: -");
        estilizarLabel(lblPozo);

        lblApuestaJugador = new JLabel("Tu apuesta actual: -");
        estilizarLabel(lblApuestaJugador);

        lblApuestaMaxima = new JLabel("Apuesta máxima: -");
        estilizarLabel(lblApuestaMaxima);

        btnIgualar = crearBoton("1. Igualar (Call)");
        btnSubir = crearBoton("2. Subir (Raise)");
        btnPlantarse = crearBoton("3. Plantarse (Fold)");

        panelFondo.add(lblJugador);
        panelFondo.add(Box.createVerticalStrut(20));

        panelFondo.add(lblSaldo);
        panelFondo.add(Box.createVerticalStrut(20));

        panelFondo.add(lblPozo);
        panelFondo.add(lblApuestaJugador);
        panelFondo.add(lblApuestaMaxima);

        panelFondo.add(Box.createVerticalStrut(30));

        panelFondo.add(btnIgualar);
        panelFondo.add(Box.createVerticalStrut(15));
        panelFondo.add(btnSubir);
        panelFondo.add(Box.createVerticalStrut(15));
        panelFondo.add(btnPlantarse);

        add(panelFondo, BorderLayout.CENTER);

        configurarAcciones();
        deshabilitarBotones();
    }

    private void configurarAcciones() {
        btnIgualar.addActionListener(e -> {
            try {
                controlador.igualarApuesta();
            } catch (Exception ex) {
                ex.printStackTrace();
                mostrarError("Error al igualar apuesta.");
            }
        });

        btnSubir.addActionListener(e -> {
            try {
                Jugador jugadorLocal = controlador.getJugadorLocalObjeto();
                int saldoActual = (jugadorLocal != null) ? jugadorLocal.getSaldo() : 0;

                Window ventanaPadre = SwingUtilities.getWindowAncestor(this);

                DialogoApostar dialogo = new DialogoApostar(ventanaPadre, saldoActual);
                dialogo.setVisible(true);

                int monto = dialogo.getMontoSeleccionado();

                if (monto > 0) {
                    controlador.subirApuesta(String.valueOf(monto));
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        btnPlantarse.addActionListener(e -> {
            try {
                controlador.plantarseApuesta();
            } catch (Exception ex) {
                ex.printStackTrace();
                mostrarError("Error al plantarse.");
            }
        });
    }


    public void actualizar() {
        try {

            Jugador jugadorTurno = controlador.getJugadorActual();

            Jugador jugadorLocal = controlador.getJugadorLocalObjeto();

            if (jugadorTurno == null || jugadorLocal == null) {
                lblJugador.setText("Cargando datos...");
                deshabilitarBotones();
                return;
            }

            boolean esMiTurno = controlador.esMiTurno();


            lblPozo.setText("Pozo actual: $" + controlador.getPozo());
            lblApuestaMaxima.setText("Apuesta a igualar: $" + controlador.getApuestaMaxima());


            lblSaldo.setText("Tu Saldo: $" + jugadorLocal.getSaldo());
            lblApuestaJugador.setText("Tu apuesta en mesa: $" + jugadorLocal.getApostado());



            if (esMiTurno) {
                lblJugador.setText("¡ES TU TURNO! (Apuesta: " + jugadorTurno.getNombre() + ")");
                lblJugador.setForeground(Color.GREEN);

                btnIgualar.setEnabled(true);
                btnSubir.setEnabled(true);
                btnPlantarse.setEnabled(true);

                if (jugadorLocal.getSaldo() <= 0) {
                    btnSubir.setEnabled(false);
                }

            } else {
                lblJugador.setText("Esperando a que apueste " + jugadorTurno.getNombre() + "...");
                lblJugador.setForeground(Color.WHITE);

                deshabilitarBotones();
            }

        } catch (RemoteException e) {
            e.printStackTrace();
            lblJugador.setText("Error de conexión...");
            deshabilitarBotones();
        }
    }

    private void deshabilitarBotones() {
        btnIgualar.setEnabled(false);
        btnSubir.setEnabled(false);
        btnPlantarse.setEnabled(false);
    }

    private void estilizarLabel(JLabel label) {
        label.setFont(FUENTE_LABEL);
        label.setForeground(COLOR_FUENTE_LABEL);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    private JButton crearBoton(String texto) {
        JButton btn = new BotonRedondeado(texto, 50);
        Color marronMedio = new Color(107, 76, 59);
        btn.setBackground(marronMedio);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 16));
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(200, 50));
        btn.setMaximumSize(TAM_BOTON);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        return btn;
    }

    private void mostrarError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }
}