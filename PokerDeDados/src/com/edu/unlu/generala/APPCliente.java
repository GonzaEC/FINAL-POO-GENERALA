package com.edu.unlu.generala;

import com.edu.unlu.generala.vista.InterfazGrafica;
import com.edu.unlu.generala.vista.InterfazConsola;
import com.edu.unlu.generala.controladores.ControladorGenerala;
import com.edu.unlu.generala.modelos.Partida;



import ar.edu.unlu.rmimvc.RMIMVCException;
import ar.edu.unlu.rmimvc.Util;
import ar.edu.unlu.rmimvc.cliente.Cliente;

import javax.swing.*;
import java.rmi.RemoteException;
import java.util.ArrayList;
public class APPCliente {
    public static void main(String[] args) {
        String[] opciones = {"Vista Gráfica", "Vista Consola"};
        int seleccion = JOptionPane.showOptionDialog(
                null,
                "¿Qué tipo de vista desea usar?",
                "Seleccionar Vista",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]
        );

        ArrayList<String> ips = Util.getIpDisponibles();
        String ip = (String) JOptionPane.showInputDialog(
                null,
                "Seleccione la IP en la que escuchará peticiones el cliente", "IP del cliente",
                JOptionPane.QUESTION_MESSAGE,
                null,
                ips.toArray(),
                null
        );
        String port = (String) JOptionPane.showInputDialog(
                null,
                "Seleccione el puerto en el que escuchará peticiones el cliente", "Puerto del cliente",
                JOptionPane.QUESTION_MESSAGE,
                null,
                null,
                9999
        );
        String ipServidor = (String) JOptionPane.showInputDialog(
                null,
                "Seleccione la IP en la que corre el servidor", "IP del servidor",
                JOptionPane.QUESTION_MESSAGE,
                null,
                null,
                null
        );
        String portServidor = (String) JOptionPane.showInputDialog(
                null,   
                "Seleccione el puerto en el que corre el servidor", "Puerto del servidor",
                JOptionPane.QUESTION_MESSAGE,
                null,
                null,
                8888
        );

        // Crear controlador vacío
        ControladorGenerala controlador = new ControladorGenerala();

        // Conectar al servidor y setear el modelo
        Cliente cliente = new Cliente(ip, Integer.parseInt(port), ipServidor, Integer.parseInt(portServidor));
        try {
            cliente.iniciar(controlador);  // 👉 setModeloRemoto(partida) se hace acá
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al conectarse con el servidor.");
            return;
        }

        // Recién ahora se crea y muestra la vista
        if (seleccion == 0) {
            InterfazGrafica vista = new InterfazGrafica(controlador);
            controlador.setVista(vista);
            vista.setVisible(true);
        } else {
            InterfazConsola vista = new InterfazConsola(controlador);
            controlador.setVista(vista);
            vista.setVisible(true);
        }
    }
}

