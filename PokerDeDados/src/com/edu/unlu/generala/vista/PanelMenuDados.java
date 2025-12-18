package com.edu.unlu.generala.vista;

import com.edu.unlu.generala.controladores.ControladorGenerala;
import com.edu.unlu.generala.modelos.Jugador;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.rmi.RemoteException;

public class PanelMenuDados extends JPanel {
    private ControladorGenerala controlador;
    private JLabel lblJugador;
    private JLabel lblTiradasRestantes;
    private JButton btnTirarTodos;
    private JButton btnApartarDados;
    private JButton btnPlantarse;

    private JPanel panelDados;
    private JToggleButton[] botonesDados;
    private static final int CANT_DADOS = 5;

    // Constantes de diseño originales
    private static final int DADO_SIZE = 80;
    private static final Dimension TAM_BOTON = new Dimension(280, 50);
    private static final Font FUENTE_LABEL = new Font("Arial", Font.BOLD, 20);
    private static final Color COLOR_FUENTE_LABEL = Color.WHITE;

    public PanelMenuDados(ControladorGenerala controlador) {
        this.controlador = controlador;

        setLayout(new BorderLayout());

        PanelFondo panelFondo = new PanelFondo("src/com/edu/unlu/generala/utils/fondoMesa.jpg");
        panelFondo.setLayout(new BoxLayout(panelFondo, BoxLayout.Y_AXIS));
        panelFondo.setBorder(BorderFactory.createEmptyBorder(100, 100, 100, 100));

        lblJugador = new JLabel("Esperando inicio de partida...");
        lblJugador.setFont(FUENTE_LABEL);
        lblJugador.setForeground(COLOR_FUENTE_LABEL);
        lblJugador.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblTiradasRestantes = new JLabel("Tiradas restantes: -");
        lblTiradasRestantes.setFont(new Font("Arial", Font.PLAIN, 18));
        lblTiradasRestantes.setForeground(COLOR_FUENTE_LABEL);
        lblTiradasRestantes.setAlignmentX(Component.CENTER_ALIGNMENT);

        // PANEL DE DADOS
        panelDados = new JPanel();
        panelDados.setOpaque(false);
        panelDados.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panelDados.setMaximumSize(new Dimension(800, 130));

        botonesDados = new JToggleButton[CANT_DADOS];
        for (int i = 0; i < CANT_DADOS; i++) {
            botonesDados[i] = new JToggleButton();
            botonesDados[i].setPreferredSize(new Dimension(DADO_SIZE, DADO_SIZE));
            botonesDados[i].setMinimumSize(new Dimension(DADO_SIZE, DADO_SIZE));
            botonesDados[i].setMaximumSize(new Dimension(DADO_SIZE, DADO_SIZE));

            botonesDados[i].setContentAreaFilled(false);
            botonesDados[i].setFocusPainted(false);
            botonesDados[i].setBorderPainted(false);
            panelDados.add(botonesDados[i]);
        }

        btnTirarTodos = crearBoton("1. Tirar todos los dados");
        btnApartarDados = crearBoton("2. Apartar dados y tirar restantes");
        btnPlantarse = crearBoton("3. Plantarse (no tirar más)");

        panelFondo.add(lblJugador);
        panelFondo.add(Box.createRigidArea(new Dimension(0, 20)));
        panelFondo.add(lblTiradasRestantes);
        panelFondo.add(Box.createRigidArea(new Dimension(0, 40)));
        panelFondo.add(panelDados);
        panelFondo.add(Box.createRigidArea(new Dimension(0, 40)));
        panelFondo.add(btnTirarTodos);
        panelFondo.add(Box.createRigidArea(new Dimension(0, 20)));
        panelFondo.add(btnApartarDados);
        panelFondo.add(Box.createRigidArea(new Dimension(0, 20)));
        panelFondo.add(btnPlantarse);

        add(panelFondo, BorderLayout.CENTER);

        inicializarDadosVisualmente();

        btnTirarTodos.setEnabled(false);
        btnApartarDados.setEnabled(false);
        btnPlantarse.setEnabled(false);

        configurarListeners();
    }


    private void inicializarDadosVisualmente() {
        int[] valoresIniciales = {1, 1, 1, 1, 1};
        actualizarDados(valoresIniciales);
    }

    private void configurarListeners() {
        btnTirarTodos.addActionListener(e -> {
            try {
                controlador.tirarTodosDados();
            } catch (RemoteException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error de conexión al tirar dados.");
            }
        });

        btnApartarDados.addActionListener(e -> {
            java.util.List<Integer> indicesDadosATirar = new java.util.ArrayList<>();
            for (int i = 0; i < CANT_DADOS; i++) {
                if (!botonesDados[i].isSelected()) {
                    indicesDadosATirar.add(i);
                }
            }
            controlador.tirarDadosSeleccionados(indicesDadosATirar);
        });

        btnPlantarse.addActionListener(e -> {
            try {
                controlador.plantarse();
            } catch (RemoteException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error de conexión al plantarse.");
            }
        });
    }

    public void actualizarDados(int[] valoresDados) {
        for (int i = 0; i < CANT_DADOS; i++) {
            botonesDados[i].setIcon(cargarIconoDado(valoresDados[i]));
            botonesDados[i].setSelected(false);
        }
    }

    private ImageIcon cargarIconoDado(int numero) {
        String rutaRelativa = "/com/edu/unlu/generala/utils/dado" + numero + ".png";
        URL url = getClass().getResource(rutaRelativa);

        if (url == null) {
            System.err.println("❌ No se encontró la imagen: " + rutaRelativa);
            return new ImageIcon();
        }

        ImageIcon icono = new ImageIcon(url);
        Image img = icono.getImage().getScaledInstance(DADO_SIZE, DADO_SIZE, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }

    public void actualizarEstadoYVista() {
        Jugador j = controlador.getJugadorActual();

        if (j == null) {
            lblJugador.setText("Turno de: -");
            lblTiradasRestantes.setText("Esperando...");
            btnTirarTodos.setEnabled(false);
            btnApartarDados.setEnabled(false);
            btnPlantarse.setEnabled(false);
            return;
        }

        lblJugador.setText("Turno de: " + j.getNombre());

        boolean miTurno = controlador.esMiTurno();
        if (miTurno) {
            lblTiradasRestantes.setText("Tiradas restantes: " + controlador.getTiradasRestantes());
            actualizarBotones();
        } else {
            btnTirarTodos.setEnabled(false);
            btnApartarDados.setEnabled(false);
            btnPlantarse.setEnabled(false);
            lblTiradasRestantes.setText("Juega " + j.getNombre() + "...");
        }
    }

    public void actualizarBotones() {
        int tiradas = controlador.getTiradasRestantes();
        btnTirarTodos.setEnabled(tiradas == 2);
        btnApartarDados.setEnabled(tiradas == 1);
        btnPlantarse.setEnabled(true);
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

    public void reiniciarVisual() {
        int[] dadosEnUno = {1, 1, 1, 1, 1};
        actualizarDados(dadosEnUno);

        for (JToggleButton btn : botonesDados) {
            btn.setSelected(false);
        }
    }
}