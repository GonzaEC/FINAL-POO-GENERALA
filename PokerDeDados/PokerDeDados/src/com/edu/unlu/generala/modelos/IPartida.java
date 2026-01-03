package com.edu.unlu.generala.modelos;

import ar.edu.unlu.rmimvc.observer.IObservableRemoto;

import java.rmi.RemoteException;
import java.util.List;

public interface IPartida extends IObservableRemoto {
    void agregarJugador(String jugador) throws RemoteException;


    void avanzarTurno() throws RemoteException;

    int getBote() throws RemoteException;

    int cantidaJugadores() throws RemoteException;

    Jugador determinarGanador() throws RemoteException;

    List<Jugador> getJugadores() throws RemoteException;

    Jugador getJugadorActual() throws RemoteException;

    int getTiradasRestantes() throws RemoteException;

    void usarTirada() throws RemoteException;

    void reiniciarTiradas() throws RemoteException;

    void agregarAlPozo(int diferencia) throws RemoteException;

    void setApuestaMaxima(int apostado) throws RemoteException;

    void distribuirGanancias(Jugador ganador) throws RemoteException;


    boolean siguienteApostador() throws RemoteException;

    int getApuestaMaxima() throws RemoteException;

    boolean todosPlantados() throws RemoteException;

    void reiniciarTurnoParaApuestas() throws RemoteException;

    void setRondaActual(EventoPartida eventoPartida) throws RemoteException;

    EventoPartida getRondaActual() throws RemoteException;

    void tirarTodosDados() throws RemoteException;
    void tirarDadosSeleccionados(List<Integer> indices) throws RemoteException;
    void plantarse() throws RemoteException;
    void cambiarTurno() throws RemoteException;
    void iniciarPartida() throws RemoteException;
    String evaluarJugada() throws RemoteException;
    void igualarApuesta() throws RemoteException;
    void plantarseApuesta() throws RemoteException;
    boolean subirApuesta(int nuevoMonto) throws RemoteException;
    void depositar(int monto) throws RemoteException;
    List<RegistroPartida> getHistorial() throws RemoteException;

}
