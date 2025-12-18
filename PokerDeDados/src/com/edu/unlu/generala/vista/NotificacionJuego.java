package com.edu.unlu.generala.vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.border.Border;

public class NotificacionJuego extends JDialog {

    // Tipos de notificación para variar el icono y color
    public enum Tipo { EXITO, ERROR, INFO }

    public NotificacionJuego(JFrame parent, String mensaje, Tipo tipo) {
        super(parent);

        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0));
        setAlwaysOnTop(true);
        setFocusableWindowState(false);

        PanelFondo panel = new PanelFondo("src/com/edu/unlu/generala/utils/fondoMesa.jpg");
        panel.setLayout(new BorderLayout(15, 0)); // Espacio entre icono y texto

        Border bordeMadera = BorderFactory.createLineBorder(new Color(80, 50, 20), 4);
        Border bordeDorado = BorderFactory.createLineBorder(new Color(218, 165, 32), 2);
        panel.setBorder(BorderFactory.createCompoundBorder(bordeMadera, bordeDorado));

        JLabel lblIcono = new JLabel();
        lblIcono.setFont(new Font("Segoe UI Emoji", Font.BOLD, 30));

        switch (tipo) {
            case EXITO -> {
                lblIcono.setText("✔");
                lblIcono.setForeground(Color.GREEN);
            }
            case ERROR -> {
                lblIcono.setText("❌");
                lblIcono.setForeground(new Color(255, 80, 80));
            }
            case INFO -> {
                lblIcono.setText("🎲");
                lblIcono.setForeground(Color.WHITE);
            }
        }
        lblIcono.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));

        JLabel lblMensaje = new JLabel("<html>" + mensaje + "</html>"); // HTML para salto de linea si es largo
        lblMensaje.setFont(new Font("Arial", Font.BOLD, 16));
        lblMensaje.setForeground(Color.WHITE);
        lblMensaje.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 15)); // Margen derecho

        panel.add(lblIcono, BorderLayout.WEST);
        panel.add(lblMensaje, BorderLayout.CENTER);

        setContentPane(panel);
        pack();

        if (parent != null) {
            int x = parent.getX() + (parent.getWidth() - getWidth()) / 2;
            // Lo ponemos un poco más abajo del centro (estética Toast)
            int y = parent.getY() + (parent.getHeight() - getHeight()) - 100;
            setLocation(x, y);
        } else {
            setLocationRelativeTo(null);
        }

        iniciarCierreSuave();
    }

    private void iniciarCierreSuave() {
        Timer timerInicio = new Timer(2000, e -> {

            Timer timerFade = new Timer(50, new ActionListener() {
                float opacidad = 1.0f;

                @Override
                public void actionPerformed(ActionEvent evt) {
                    opacidad -= 0.1f;
                    if (opacidad <= 0.0f) {
                        ((Timer)evt.getSource()).stop();
                        dispose();
                    } else {
                        setOpacity(opacidad);
                    }
                }
            });
            timerFade.start();
        });

        timerInicio.setRepeats(false);
        timerInicio.start();
    }

    // Métodos estáticos para usarlo fácil

    public static void exito(JFrame parent, String msg) {
        mostrar(parent, msg, Tipo.EXITO);
    }

    public static void error(JFrame parent, String msg) {
        mostrar(parent, msg, Tipo.ERROR);
    }

    public static void info(JFrame parent, String msg) {
        mostrar(parent, msg, Tipo.INFO);
    }

    private static void mostrar(JFrame parent, String msg, Tipo tipo) {
        try {
            NotificacionJuego noti = new NotificacionJuego(parent, msg, tipo);
            noti.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}