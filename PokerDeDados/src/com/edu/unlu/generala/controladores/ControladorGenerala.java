package com.edu.unlu.generala.controladores;

import ar.edu.unlu.rmimvc.cliente.IControladorRemoto;
import ar.edu.unlu.rmimvc.observer.IObservableRemoto;
import com.edu.unlu.generala.modelos.*;
import com.edu.unlu.generala.vista.IVista;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;


public class ControladorGenerala implements IControladorRemoto {
    IVista vista;
    IPartida partida;
    private String nombreJugadorLocal;

    public ControladorGenerala(){

    }
    public ControladorGenerala(IVista vista) {
        this.vista = vista;
    }

    public void setVista(IVista vista) {
        this.vista = vista;
    }

    public void iniciar() throws RemoteException {
        try {
            if (partida.getJugadores().size() < 2) {
                vista.mostrarMensaje("Debe haber al menos 2 jugadores para comenzar la partida.");
                return;
            }

            partida.iniciarPartida();

            Jugador jugadorActual = partida.getJugadorActual();

            vista.mostrarMensaje("¡Comienza la partida!");
            vista.mostrarMensaje("Turno de " + jugadorActual.getNombre());

        } catch (RemoteException e) {
            vista.mostrarMensaje("Error al iniciar la partida.");
            e.printStackTrace();
        }
    }

    public void setNombreJugadorLocal(String nombre) {
        this.nombreJugadorLocal = nombre;
    }

    public String getNombreJugadorLocal() {
        return nombreJugadorLocal;
    }

    public boolean esMiTurno() {
        try {
            Jugador actual = partida.getJugadorActual();
            if (actual == null || nombreJugadorLocal == null) return false;
            return actual.getNombre().equals(nombreJugadorLocal);
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void agregarJugador(String jugador) {
        try {
            partida.agregarJugador(jugador);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public int cantidadJugadores(){
        try {
            return partida.cantidaJugadores();
        }catch (Exception e){
            e.printStackTrace();
        }

        return 0;
    }

    //!chequear
    public String evaluarJugada() {
        try {
            return partida.evaluarJugada();
        } catch (RemoteException e) {
            e.printStackTrace();
            return "Error al evaluar la jugada.";
        }
    }

    public Jugador determinarGanador() {
        try {
            return partida.determinarGanador();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


    public Jugador getJugadorActual() {
        try {
            return partida.getJugadorActual();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    //!chequear
    public void cambiarTurno() {
        try {
            partida.cambiarTurno();
        } catch (RemoteException e) {
            vista.mostrarMensaje("Error al cambiar de turno.");
        }
    }


    public List<Jugador> getJugadores() {
        try {
            return partida.getJugadores();
        }catch (Exception e){
            e.printStackTrace();
        }

        return List.of();
    }




    public int getTiradasRestantes() {
        try {
            return partida.getTiradasRestantes();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }


    public boolean existeJugador(String nombreJugador) {
        boolean resultado = false;
        List<Jugador> jugadores = new ArrayList<>();
        try {
            jugadores = partida.getJugadores();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        for (Jugador gamer : jugadores) {
            if (gamer.getNombre().equals(nombreJugador)) {
                resultado = true;
                break;
            }
        }
        return resultado;
    }

    // ! chequear creo que es al pedo
    public void depositar(String deposito) {
        try {
            int monto = Integer.parseInt(deposito);

            if (monto <= 0) {
                vista.mostrarMensaje("El monto debe ser mayor a 0.");
                return;
            }

            partida.depositar(monto);

            vista.mostrarMensaje("Se depositaron $" + monto + " al jugador actual.");

        } catch (NumberFormatException e) {
            vista.mostrarMensaje("Entrada inválida, ingrese un número válido.");
        } catch (RemoteException e) {
            vista.mostrarMensaje("Error de conexión al realizar el depósito.");
            e.printStackTrace();
        }
    }

    public void tirarTodosDados() throws RemoteException {
        try {
            if (partida.getRondaActual() != EventoPartida.RONDA_TIRADAS) {
                vista.mostrarMensaje("No se pueden tirar dados en esta ronda.");
                return;
            }

            Jugador jugador = partida.getJugadorActual();
            if (jugador.isPlantado()) {
                vista.mostrarMensaje("Ya te has plantado. No podés tirar más dados.");
                return;
            }

            if (partida.getTiradasRestantes() <= 0) {
                vista.mostrarMensaje("No quedan más tiradas.");
                return;
            }

            partida.tirarTodosDados();

        } catch (RemoteException e) {
            e.printStackTrace();
            vista.mostrarMensaje("Error de conexión al tirar los dados.");
        }
    }

    public void tirarDadosSeleccionados(List<Integer> indices) {
        try {
            if (partida.getRondaActual() != EventoPartida.RONDA_TIRADAS) {
                vista.mostrarMensaje("No se pueden tirar dados en esta ronda.");
                return;
            }

            Jugador jugador = partida.getJugadorActual();

            if (jugador.isPlantado()) {
                vista.mostrarMensaje("Ya te has plantado. No podés tirar más dados.");
                return;
            }

            int tiradas = partida.getTiradasRestantes();
            if (tiradas <= 0) {
                vista.mostrarMensaje("No quedan más tiradas.");
                return;
            }

            if (tiradas != 1) {
                vista.mostrarMensaje("Solo podés tirar dados seleccionados en la segunda tirada.");
                return;
            }

            partida.tirarDadosSeleccionados(indices);

        } catch (RemoteException e) {
            e.printStackTrace();
            vista.mostrarMensaje("Error de conexión al tirar dados seleccionados.");
        }
    }

    public void plantarse() throws RemoteException {
        try {
            if (partida.getRondaActual() != EventoPartida.RONDA_TIRADAS) {
                vista.mostrarMensaje("No podés plantarte en esta ronda.");
                return;
            }

            Jugador jugador = partida.getJugadorActual();
            vista.mostrarMensaje(jugador.getNombre() + " se ha plantado.");

            partida.plantarse();

        } catch (RemoteException e) {
            e.printStackTrace();
            vista.mostrarMensaje("Error de conexión al plantarse.");
        }
    }

    public boolean todosPlantados(){
        try {
            return partida.todosPlantados();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public int getPozo() throws RemoteException {
        return partida.getBote();
    }

    public int getApuestaMaxima() {
        try {
            return partida.getApuestaMaxima();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void igualarApuesta() throws RemoteException {
        try {
            partida.igualarApuesta();
        } catch (RemoteException e) {
            vista.mostrarMensaje("Error al igualar la apuesta.");
            e.printStackTrace();
        }

    }

    public void plantarseApuesta() throws RemoteException {
        try {
            partida.plantarseApuesta();
        } catch (RemoteException e) {
            vista.mostrarMensaje("Error de conexión al plantarte en la apuesta.");
        }
    }


    public Jugador getJugadorLocalObjeto() {
        try {
            List<Jugador> jugadores = partida.getJugadores();

            for (Jugador j : jugadores) {
                if (j.getNombre().equals(this.nombreJugadorLocal)) {
                    return j;
                }
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void subirApuesta(String entrada) throws RemoteException {
        try {
            int nuevoMonto = Integer.parseInt(entrada);

            boolean ok = partida.subirApuesta(nuevoMonto);

            if (!ok) {
                vista.mostrarMensaje("No fue posible subir la apuesta.");
                partida.notificarObservadores(EventoPartida.RONDA_APUESTAS_INICIADA);
            }

        } catch (NumberFormatException e) {
            vista.mostrarMensaje("Entrada inválida. Debe ser un número.");
            try {
                partida.notificarObservadores(EventoPartida.RONDA_APUESTAS_INICIADA);
            } catch (RemoteException ex) {
                ex.printStackTrace();
            }
        } catch (RemoteException e) {
            vista.mostrarMensaje("Error de conexión al subir la apuesta.");
            e.printStackTrace();
        }
    }

    public List<RegistroPartida> getHistorial() {
        try {
            return partida.getHistorial();
        } catch (RemoteException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }


    @Override
    public <T extends IObservableRemoto> void setModeloRemoto(T modeloRemoto) throws RemoteException {
        this.partida = (IPartida) modeloRemoto;
    }


    @Override
    public void actualizar(IObservableRemoto modelo, Object evento) throws RemoteException {
        if (!(evento instanceof EventoPartida)) {
            return;
        }

        EventoPartida eventoGenerala = (EventoPartida) evento;

        javax.swing.SwingUtilities.invokeLater(() -> {
            try {
                System.out.println(" [CLIENTE] Procesando evento: " + eventoGenerala);

                switch (eventoGenerala) {
                    case JUGADOR_AGREGADO -> {
                        vista.mostrarMensaje("Jugador agregado correctamente.");
                    }

                    case JUEGO_INICIADO -> {
                        vista.mostrarMensaje("¡La partida ha comenzado!");
                        vista.mostrarMenuJugadorDados();
                        vista.actualizarVista();
                    }

                    // --- FASE DE DADOS ---
                    case DADOS_TIRADOS -> {
                        if (esMiTurno()) {
                            if (partida.getJugadorActual() != null) {
                                int[] valores = partida.getJugadorActual().getVasoJugador().getValores();
                                vista.mostrarTirada(valores);
                            }
                        } else {
                            vista.limpiarMesa();
                            vista.actualizarVista();
                        }
                    }

                    case CAMBIO_TURNO, ACTUALIZAR_MENU_DADOS -> {
                        vista.limpiarMesa();
                        vista.actualizarVista();
                    }
                    // --- FASE DE APUESTAS ---
                    case TODOS_PLANTADOS -> {
                        vista.mostrarMensaje("Todos los jugadores se plantaron. ¡Comienzan las apuestas!");
                        vista.mostrarMenuApuestas();
                    }

                    case RONDA_APUESTAS_INICIADA, SIGUIENTE_APOSTADOR, APUESTA_IGUALADA, APUESTA_SUBIDA,
                         APOSTADOR_PLANTADO -> {
                        vista.mostrarMenuApuestas();
                    }

                    // --- FIN DEL JUEGO ---
                    case GANADOR_DETERMINADO -> {
                        vista.mostrarGanador();
                    }

                    default -> System.out.println("[CLIENTE] Evento sin accion visual definida: " + eventoGenerala);
                }

            } catch (RemoteException e) {
                vista.mostrarMensaje("Error de conexion con el servidor al actualizar.");
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
