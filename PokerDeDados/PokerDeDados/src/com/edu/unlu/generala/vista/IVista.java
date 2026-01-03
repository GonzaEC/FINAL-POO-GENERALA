package com.edu.unlu.generala.vista;

import com.edu.unlu.generala.controladores.ControladorGenerala;
import com.edu.unlu.generala.modelos.Apuesta;
import com.edu.unlu.generala.modelos.Dado;

import java.rmi.RemoteException;
import java.util.List;

public interface IVista {
    void setControlador(ControladorGenerala controlador);
    void mostrarMensaje(String mensaje);
    void iniciar();

    void mostrarMenuJugadorDados();

    void mostrarMenuApuestas();

    void mostrarGanador() throws RemoteException;

    void  mostrarTirada(int[] valoresDados);


    void irAVentanaMenuApuestas();

    void actualizarVista();
    void limpiarMesa();
}
