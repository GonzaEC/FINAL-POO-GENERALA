package com.edu.unlu.generala;

import com.edu.unlu.generala.vista.InterfazGrafica;
import com.edu.unlu.generala.vista.InterfazConsola;
import com.edu.unlu.generala.controladores.ControladorGenerala;
import ar.edu.unlu.rmimvc.cliente.Cliente;

import javax.swing.*;
import java.util.Random;

public class APPCliente {
    public static void main(String[] args) {
        String[] opciones = {"Vista Gráfica", "Vista Consola"};
        int seleccion = JOptionPane.showOptionDialog(
                null, "Seleccione la interfaz visual:", "Inicio Cliente",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);

        if (seleccion == -1) return; // Si cierra la ventana

        // --- VALORES POR DEFECTO ---
        // Generamos el puerto aleatorio para sugerirlo
        int puertoRandom = new Random().nextInt(1000) + 9000;

        String ipCliente = (String) JOptionPane.showInputDialog(
                null, "IP para este Cliente:", "Config Cliente",
                JOptionPane.QUESTION_MESSAGE, null, null,
                "127.0.0.1" // <--- DEFAULT
        );
        if (ipCliente == null) return;

        String portClienteStr = (String) JOptionPane.showInputDialog(
                null, "Puerto para este Cliente:", "Config Cliente",
                JOptionPane.QUESTION_MESSAGE, null, null,
                String.valueOf(puertoRandom) // <--- DEFAULT ALEATORIO (El usuario solo da Enter)
        );
        if (portClienteStr == null) return;

        String ipServidor = (String) JOptionPane.showInputDialog(
                null, "IP del Servidor:", "Conexión",
                JOptionPane.QUESTION_MESSAGE, null, null,
                "127.0.0.1" // <--- DEFAULT
        );
        if (ipServidor == null) return;

        String portServidor = (String) JOptionPane.showInputDialog(
                null, "Puerto del Servidor:", "Conexión",
                JOptionPane.QUESTION_MESSAGE, null, null,
                "8888" // <--- DEFAULT
        );
        if (portServidor == null) return;


        // Lógica de conexión
        ControladorGenerala controlador = new ControladorGenerala();
        Cliente cliente = new Cliente(ipCliente, Integer.parseInt(portClienteStr), ipServidor, Integer.parseInt(portServidor));

        try {
            cliente.iniciar(controlador);

            if (seleccion == 0) {
                InterfazGrafica vista = new InterfazGrafica(controlador);
                controlador.setVista(vista);
                vista.setVisible(true);
            } else {
                InterfazConsola vista = new InterfazConsola(controlador);
                controlador.setVista(vista);
                vista.setVisible(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "No se pudo conectar al servidor.");
        }
    }
}