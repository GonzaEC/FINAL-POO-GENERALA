package com.edu.unlu.generala;

import com.edu.unlu.generala.controladores.ControladorGenerala;
import com.edu.unlu.generala.vista.InterfazGrafica;

import javax.swing.*;

public class pruebaG {
    public static void main(String[] args) {
        ControladorGenerala controlador = new ControladorGenerala();
        SwingUtilities.invokeLater(() -> {
            InterfazGrafica ventana = new InterfazGrafica(controlador);
            ventana.setVisible(true);
        });
    }
}
