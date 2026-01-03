package com.edu.unlu.generala.vista;

import javax.swing.*;
import java.awt.*;

public class DialogoApostar extends JDialog {

    private JTextField txtMonto;
    private int montoConfirmado = -1; // -1 significa que canceló
    private JLabel lblError;

    public DialogoApostar(Window parent, int saldoDisponible) {
        super(parent, "Realizar Apuesta", ModalityType.APPLICATION_MODAL);
        setSize(400, 300);
        setLocationRelativeTo(parent);
        setResizable(false);
        setUndecorated(true);

        // Fondo
        PanelFondo panelFondo = new PanelFondo("src/com/edu/unlu/generala/utils/fondoMesa.jpg");
        panelFondo.setLayout(new BoxLayout(panelFondo, BoxLayout.Y_AXIS));
        // Borde decorativo
        panelFondo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(107, 76, 59), 3),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        // Titulo
        JLabel lblTitulo = new JLabel("SUBIR APUESTA");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Saldo
        JLabel lblSaldo = new JLabel("Tu saldo disponible: $" + saldoDisponible);
        lblSaldo.setFont(new Font("Arial", Font.PLAIN, 16));
        lblSaldo.setForeground(new Color(255, 215, 0));
        lblSaldo.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Input
        txtMonto = new JTextField();
        txtMonto.setFont(new Font("Arial", Font.BOLD, 30));
        txtMonto.setHorizontalAlignment(JTextField.CENTER);
        txtMonto.setMaximumSize(new Dimension(200, 50));
        // Borde simple para el input
        txtMonto.setBorder(BorderFactory.createLineBorder(new Color(107, 76, 59), 2));


        lblError = new JLabel(" ");
        lblError.setForeground(Color.RED);
        lblError.setFont(new Font("Arial", Font.BOLD, 14));
        lblError.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Botones
        JPanel panelBotones = new JPanel();
        panelBotones.setOpaque(false);
        panelBotones.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 0));

        JButton btnConfirmar = new BotonRedondeado("Apostar", 40);
        estilizarBoton(btnConfirmar, new Color(34, 139, 34));

        JButton btnCancelar = new BotonRedondeado("Cancelar", 40);
        estilizarBoton(btnCancelar, new Color(178, 34, 34));

        panelBotones.add(btnCancelar);
        panelBotones.add(btnConfirmar);

        // --- Acciones ---
        btnConfirmar.addActionListener(e -> validarYConfirmar(saldoDisponible));
        btnCancelar.addActionListener(e -> dispose());

        // --- Armado ---
        panelFondo.add(lblTitulo);
        panelFondo.add(Box.createVerticalStrut(10));
        panelFondo.add(lblSaldo);
        panelFondo.add(Box.createVerticalStrut(30));
        panelFondo.add(txtMonto);
        panelFondo.add(Box.createVerticalStrut(10));
        panelFondo.add(lblError);
        panelFondo.add(Box.createVerticalStrut(20));
        panelFondo.add(panelBotones);

        add(panelFondo);
    }

    private void validarYConfirmar(int saldoMax) {
        String texto = txtMonto.getText().trim();
        try {
            int monto = Integer.parseInt(texto);
            if (monto <= 0) {
                lblError.setText("El monto debe ser mayor a 0.");
            } else if (monto > saldoMax) {
                lblError.setText("No tienes suficiente saldo.");
            } else {
                // Todo OK
                this.montoConfirmado = monto;
                dispose();
            }
        } catch (NumberFormatException e) {
            lblError.setText("Ingresa un número válido.");
        }
    }

    public int getMontoSeleccionado() {
        return montoConfirmado;
    }

    private void estilizarBoton(JButton btn, Color colorFondo) {
        btn.setBackground(colorFondo);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(120, 40));
    }
}