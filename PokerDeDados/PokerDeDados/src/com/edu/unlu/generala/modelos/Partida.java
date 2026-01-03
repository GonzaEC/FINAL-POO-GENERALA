package com.edu.unlu.generala.modelos;


import ar.edu.unlu.rmimvc.observer.ObservableRemoto;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class Partida extends ObservableRemoto implements IPartida {
    private final List<Jugador> jugadores;
    private final List<Apuesta> apuestas;
    private Vaso vaso;
    private int indiceJugadorActual;
    private int bote;
    private Jugador jugadorActual;
    private int tiradasRestantes;
    private static final int MAX_TIRADAS = 2;
    private List<RegistroPartida> historial;
    private EventoPartida rondaActual;
    private int apuestaMaxima;
    private boolean partidaEnCurso = false;
    private int jugadorQueEmpieza = 0;
    private ManoPoker arbitro = new ManoPoker();

    public Partida() {
        this.jugadores = new ArrayList<>();
        this.apuestas = new ArrayList<>();
        this.vaso = new Vaso();
        this.indiceJugadorActual = 0;
        this.bote = 0;
        jugadorActual = null;
        this.tiradasRestantes = MAX_TIRADAS;
        this.rondaActual = EventoPartida.RONDA_TIRADAS;
        this.apuestaMaxima = 0;
        this.historial = com.edu.unlu.generala.utils.Serializador.cargar();
    }

    public void setIndiceJugadorActual(int indiceJugadorActual) {
        this.indiceJugadorActual = indiceJugadorActual;
    }

    public void setBote(int bote) {
        this.bote = bote;
    }

    public int getTiradasRestantes() {
        return tiradasRestantes;
    }

    @Override
    public List<Jugador> getJugadores() {
        return jugadores;
    }

    @Override
    public int getBote() {
        return bote;
    }

    @Override
    public Jugador getJugadorActual() {
        return jugadores.get(indiceJugadorActual);
    }

    @Override
    public List<RegistroPartida> getHistorial() throws RemoteException {
        return historial;
    }

    @Override
    public int cantidaJugadores() {
        return jugadores.size();
    }

    @Override
    public void agregarJugador(String nombre) throws RemoteException {
        jugadores.add(new Jugador(nombre, 100));
        //notificarObservadores(EventoPartida.JUGADOR_AGREGADO);
    }

    public void usarTirada() {
        if (tiradasRestantes > 0) {
            tiradasRestantes--;
        }
    }

    public void reiniciarTiradas() {
        this.tiradasRestantes = MAX_TIRADAS;
    }

    public Jugador determinarGanador() {
        Jugador ganador = null;
        List<Jugador> activos = new ArrayList<>();

        for (Jugador j : jugadores) {
            if (!j.isPlantoApuesta()) {
                ResultadoMano resultado = arbitro.evaluar(j.getVasoJugador());
                j.setMejorJugada(resultado);

                activos.add(j);
            }
        }

        if (!activos.isEmpty()) {
            ganador = activos.get(0);

            for (int i = 1; i < activos.size(); i++) {
                Jugador retador = activos.get(i);

                if (retador.getMejorJugada().compareTo(ganador.getMejorJugada()) > 0) {
                    ganador = retador;
                }
            }
        } else {
        }

        return ganador;
    }

    public void avanzarTurno() throws RemoteException {
        indiceJugadorActual = (indiceJugadorActual + 1) % jugadores.size();
    }

    public int getApuestaMaxima() {
        return apuestaMaxima;
    }

    public EventoPartida getRondaActual() {
        return rondaActual;
    }

    public void setRondaActual(EventoPartida rondaActual) {
        this.rondaActual = rondaActual;
    }

    public boolean todosPlantados() throws RemoteException {
        for (Jugador j : jugadores) {
            if (!j.isPlantado()) {
                return false;
            }
        }
        return true;

    }

    public void reiniciarTurnoParaApuestas() {
        this.indiceJugadorActual = jugadorQueEmpieza;

        for (Jugador j : jugadores) {
            j.setHaApostado(false);
            j.setPlantoApuesta(false);
        }
    }
    public void agregarAlPozo(int diferencia) {
        this.bote = bote+=diferencia;
    }

    public boolean siguienteApostador() {
        int totalJugadores = jugadores.size();
        int intentos = 0;

        do {
            indiceJugadorActual = (indiceJugadorActual + 1) % totalJugadores;
            intentos++;
        } while ((jugadores.get(indiceJugadorActual).isPlantoApuesta() ||
                jugadores.get(indiceJugadorActual).getApostado() == apuestaMaxima)
                && intentos <= totalJugadores);

        boolean rondaFinalizada = jugadores.stream().allMatch(j ->
                j.isPlantoApuesta() || j.getApostado() == apuestaMaxima
        );

        if (rondaFinalizada) {
            try {
                avanzarTurno();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }

        return rondaFinalizada;
    }

    public void distribuirGanancias(Jugador ganador) {
        ganador.agregarSaldo(bote);
    }


    public void setApuestaMaxima(int n){
        this.apuestaMaxima = n;
    }

    public void tirarTodosDados() throws RemoteException {
        if (rondaActual != EventoPartida.RONDA_TIRADAS) {
            return;
        }

        Jugador jugador = getJugadorActual();

        if (jugador.isPlantado()) {
            return;
        }

        if (tiradasRestantes <= 0) {
            return;
        }

        if (tiradasRestantes == MAX_TIRADAS) {
            jugador.getVasoJugador().lanzarDados();
            usarTirada();

            notificarObservadores(EventoPartida.DADOS_TIRADOS);
        } else {
            return;
        }
    }

    @Override
    public void tirarDadosSeleccionados(List<Integer> indices) throws RemoteException {

        if (rondaActual != EventoPartida.RONDA_TIRADAS) {
            return;
        }

        Jugador jugador = getJugadorActual();

        if (jugador.isPlantado()) {
            return;
        }

        if (tiradasRestantes <= 0) {
            return;
        }

        if (tiradasRestantes != 1) {
            return;
        }

        jugador.getVasoJugador().lanzarSeleccionados(indices);
        usarTirada();

        notificarObservadores(EventoPartida.DADOS_TIRADOS);
    }

    @Override
    public void plantarse() throws RemoteException {
        if (rondaActual != EventoPartida.RONDA_TIRADAS) {
            return;
        }

        Jugador jugador = getJugadorActual();
        jugador.setPlantado(true);
        reiniciarTiradas();

        if (todosPlantados()) {

            this.rondaActual = EventoPartida.RONDA_APUESTAS;
            reiniciarTurnoParaApuestas();

            notificarObservadores(EventoPartida.TODOS_PLANTADOS);

        } else {
            avanzarTurno();
            notificarObservadores(EventoPartida.ACTUALIZAR_MENU_DADOS);
        }
    }

    @Override
    public void cambiarTurno() throws RemoteException {
        reiniciarTiradas();
        avanzarTurno();
        notificarObservadores(EventoPartida.CAMBIO_TURNO);
    }
    @Override
    public void iniciarPartida() throws RemoteException {
        rondaActual = EventoPartida.RONDA_TIRADAS;
        reiniciarTiradas();

        if (!partidaEnCurso) {
            //! Prinera ronda
            this.jugadorQueEmpieza = 0;
            partidaEnCurso = true;
        }
        else {
            //! Ronda 2 o mas
            this.jugadorQueEmpieza = (this.jugadorQueEmpieza + 1) % jugadores.size();
        }

        this.indiceJugadorActual = this.jugadorQueEmpieza;

        this.bote = 0;
        this.apuestaMaxima = 0;
        this.apuestas.clear();

        for (Jugador j : jugadores) {
            j.setPlantado(false);
            j.setPlantoApuesta(false);
            j.setHaApostado(false);


            j.setApostado(new Apuesta(j, 0));
        }

        notificarObservadores(EventoPartida.JUEGO_INICIADO);
    }

    @Override
    public String evaluarJugada() throws RemoteException {
        return "";
    }

    @Override
    public void igualarApuesta() throws RemoteException {
        Jugador jugador = getJugadorActual();

        int diferencia = apuestaMaxima - jugador.getApostado();
        if (diferencia <= 0) {
            return;
        }

        jugador.aumentarApuesta(diferencia);
        agregarAlPozo(diferencia);

        evaluarFinDeRondaApuestasInterno();
    }

    private void evaluarFinDeRondaApuestasInterno() throws RemoteException {
        boolean termino = siguienteApostador();
        if (termino) {
            Jugador ganador = determinarGanador();
            distribuirGanancias(ganador);

            String nombreJugada = ganador.getMejorJugada().getNombre();
            int pozoGanado = this.bote;

            RegistroPartida registro = new RegistroPartida(ganador.getNombre(), pozoGanado, nombreJugada);
            historial.add(0, registro);
            com.edu.unlu.generala.utils.Serializador.guardar(historial);

            notificarObservadores(EventoPartida.GANADOR_DETERMINADO);
        } else {
            notificarObservadores(EventoPartida.RONDA_APUESTAS_INICIADA);
        }
    }

    @Override
    public void plantarseApuesta() throws RemoteException {
        Jugador jugador = getJugadorActual();
        jugador.setPlantoApuesta(true);
        evaluarFinDeRondaApuestasInterno();
    }

    @Override
    public boolean subirApuesta(int nuevoMonto) throws RemoteException {
        Jugador jugador = getJugadorActual();
        int yaApostado = jugador.getApostado();

        if (nuevoMonto <= 0) {
            return false;
        }

        if (nuevoMonto <= yaApostado) {
            return false;
        }

        int diferencia = nuevoMonto - yaApostado;

        if (jugador.getSaldo() < diferencia) {
            return false;
        }

        jugador.retirarSaldo(diferencia);
        jugador.apostar(diferencia);
        setApuestaMaxima(jugador.getApostado());
        agregarAlPozo(diferencia);
        jugador.setPlantoApuesta(false);

        evaluarFinDeRondaApuestasInterno();

        return true;
    }
    @Override
    public void depositar(int monto) throws RemoteException {
        Jugador jugador = getJugadorActual();
        jugador.agregarSaldo(monto);
    }

    private String evaluarJugadaTexto(Jugador j) {
        return "Ganador";
    }

}


