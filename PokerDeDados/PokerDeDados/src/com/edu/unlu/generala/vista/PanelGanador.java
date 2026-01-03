package com.edu.unlu.generala.vista;

import com.edu.unlu.generala.modelos.Jugador;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class PanelGanador extends JDialog {

    private static final int DADO_SIZE = 60;

    public PanelGanador(JFrame parent, Jugador ganador, int montoGanado) {
        super(parent, "¡Tenemos un Ganador!", true);
        setSize(500, 520);
        setLocationRelativeTo(parent);
        setResizable(false);

        // 1. FONDO CON EFECTO CONFETI
        PanelFondo panelFondo = new PanelFondo("src/com/edu/unlu/generala/utils/fondoMesa.jpg") {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                // Dibujar Confeti (Círculos aleatorios)
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                for(int i = 0; i < 60; i++) {
                    int x = (int)(Math.random() * getWidth());
                    int y = (int)(Math.random() * getHeight());
                    int size = (int)(Math.random() * 10) + 5;
                    g2d.setColor(new Color((int)(Math.random() * 0x1000000)));
                    g2d.fillOval(x, y, size, size);
                }
            }
        };

        panelFondo.setLayout(new BoxLayout(panelFondo, BoxLayout.Y_AXIS));
        panelFondo.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        // 2. TÍTULO
        JLabel lblTitulo = new JLabel("¡GANADOR DE LA RONDA!");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER); // Refuerzo de centrado

        // 3. NOMBRE DEL GANADOR
        JLabel lblNombre = new JLabel(ganador.getNombre().toUpperCase());
        lblNombre.setFont(new Font("Arial", Font.BOLD, 45));
        lblNombre.setForeground(new Color(255, 215, 0)); // Dorado
        lblNombre.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblNombre.setHorizontalAlignment(SwingConstants.CENTER);

        // 4. NOMBRE DE LA JUGADA
        String nombreJugada = "Jugada Ganadora";
        if (ganador.getMejorJugada() != null) {
            nombreJugada = ganador.getMejorJugada().getNombre();
        }

        JLabel lblJugada = new JLabel("\u2605 " + nombreJugada + " \u2605");
        lblJugada.setFont(new Font("SansSerif", Font.ITALIC, 22));
        lblJugada.setForeground(new Color(220, 220, 220));
        lblJugada.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 5. DADOS
        JPanel panelDados = new JPanel();
        panelDados.setOpaque(false);
        panelDados.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));

        int[] dados = ganador.getVasoJugador().getValores();
        for (int valor : dados) {
            JLabel lblDado = new JLabel(cargarIconoDado(valor));
            panelDados.add(lblDado);
        }

        String textoPremio = "<html><div style='text-align: center;'>Se lleva el pozo de: <font color='#FFD700' size='6'>$" + montoGanado + "</font></div></html>";
        JLabel lblPremio = new JLabel(textoPremio);
        lblPremio.setFont(new Font("Arial", Font.BOLD, 24));
        lblPremio.setForeground(Color.WHITE);
        lblPremio.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblPremio.setHorizontalAlignment(SwingConstants.CENTER); // Refuerzo

        // 7. BOTÓN
        JButton btnContinuar = new BotonRedondeado("Continuar", 40);
        btnContinuar.setBackground(new Color(107, 76, 59));
        btnContinuar.setForeground(Color.WHITE);
        btnContinuar.setFont(new Font("Arial", Font.BOLD, 18));
        btnContinuar.setPreferredSize(new Dimension(180, 45));
        btnContinuar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnContinuar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnContinuar.addActionListener(e -> dispose());

        // --- ARMADO ---
        panelFondo.add(Box.createVerticalStrut(10));
        panelFondo.add(lblTitulo);
        panelFondo.add(Box.createVerticalStrut(15));
        panelFondo.add(lblNombre);
        panelFondo.add(Box.createVerticalStrut(5));
        panelFondo.add(lblJugada);
        panelFondo.add(Box.createVerticalStrut(20));
        panelFondo.add(panelDados);
        panelFondo.add(Box.createVerticalStrut(20));
        panelFondo.add(lblPremio);
        panelFondo.add(Box.createVerticalGlue());
        panelFondo.add(btnContinuar);
        panelFondo.add(Box.createVerticalStrut(10));

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