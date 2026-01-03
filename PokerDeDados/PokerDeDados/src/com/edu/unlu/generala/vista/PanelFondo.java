package com.edu.unlu.generala.vista;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class PanelFondo extends JPanel {

    private Image imagen;

    public PanelFondo(String rutaImagen) {
        this.imagen = new ImageIcon(rutaImagen).getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(imagen, 0, 0, getWidth(), getHeight(), this);
    }
}
