package com.edu.unlu.generala.vista;

import com.edu.unlu.generala.modelos.Jugador;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class PanelGanador extends JDialog {

    private static final int DADO_SIZE = 60;

    public PanelGanador(JFrame parent, Jugador ganador, int montoGanado) {
        super(parent, "¡Tenemos un Ganador!", true);
        setSize(500, 450);
        setLocationRelativeTo(parent);
        setResizable(false);

        // Fondo de madera
        PanelFondo panelFondo = new PanelFondo("src/com/edu/unlu/generala/utils/fondoMesa.jpg");
        panelFondo.setLayout(new BoxLayout(panelFondo, BoxLayout.Y_AXIS));
        panelFondo.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        // Título
        JLabel lblTitulo = new JLabel("¡GANADOR DE LA RONDA!");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Nombre del Ganador
        JLabel lblNombre = new JLabel(ganador.getNombre().toUpperCase());
        lblNombre.setFont(new Font("Arial", Font.BOLD, 40));
        lblNombre.setForeground(new Color(255, 215, 0)); // Dorado
        lblNombre.setAlignmentX(Component.CENTER_ALIGNMENT);


        // Panel para mostrar los dados ganadores
        JPanel panelDados = new JPanel();
        panelDados.setOpaque(false);
        panelDados.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        // Cargamos los dados del vaso del ganador
        int[] dados = ganador.getVasoJugador().getValores();
        for (int valor : dados) {
            JLabel lblDado = new JLabel(cargarIconoDado(valor));
            panelDados.add(lblDado);
        }

        // Premio
        JLabel lblPremio = new JLabel("Se lleva el pozo de: $" + montoGanado);
        lblPremio.setFont(new Font("Arial", Font.BOLD, 20));
        lblPremio.setForeground(Color.WHITE);
        lblPremio.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Continuar
        JButton btnContinuar = new BotonRedondeado("Continuar", 40);
        btnContinuar.setBackground(new Color(107, 76, 59));
        btnContinuar.setForeground(Color.WHITE);
        btnContinuar.setFont(new Font("Arial", Font.BOLD, 16));
        btnContinuar.setPreferredSize(new Dimension(150, 40));
        btnContinuar.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnContinuar.addActionListener(e -> dispose());

        panelFondo.add(lblTitulo);
        panelFondo.add(Box.createVerticalStrut(20));
        panelFondo.add(lblNombre);
        panelFondo.add(Box.createVerticalStrut(30));
        panelFondo.add(panelDados);
        panelFondo.add(Box.createVerticalStrut(30));
        panelFondo.add(lblPremio);
        panelFondo.add(Box.createVerticalGlue());
        panelFondo.add(btnContinuar);

        add(panelFondo);
    }

    private ImageIcon cargarIconoDado(int numero) {
        String rutaRelativa = "/com/edu/unlu/generala/utils/dado" + numero + ".png";
        URL url = getClass().getResource(rutaRelativa);
        if (url == null) return new ImageIcon();
        ImageIcon icono = new ImageIcon(url);
        Image img = icono.getImage().getScaledInstance(DADO_SIZE, DADO_SIZE, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }
}