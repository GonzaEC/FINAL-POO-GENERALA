package com.edu.unlu.generala.vista;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Mensaje extends JDialog {

    private JPanel contentPane;
    private JButton buttonOK;
    private JLabel lblMensaje;

    public Mensaje(String mensaje) {
        contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());

        lblMensaje = new JLabel(mensaje);
        lblMensaje.setHorizontalAlignment(SwingConstants.CENTER);
        contentPane.add(lblMensaje, BorderLayout.CENTER);

        buttonOK = new JButton("OK");
        JPanel panelBotones = new JPanel();
        panelBotones.add(buttonOK);
        contentPane.add(panelBotones, BorderLayout.SOUTH);

        setContentPane(contentPane);
        setModal(true);
        pack();
        setLocationRelativeTo(null);

        buttonOK.addActionListener(e -> dispose());
    }


    private void onOK() {
        dispose();
    }

}
