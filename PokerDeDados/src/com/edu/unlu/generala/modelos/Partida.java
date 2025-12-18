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
        int mejorPuntaje = -1;
        int[] mejoresDados = null;

        List<Jugador> jugadoresEnCompetencia = new ArrayList<>();

        for (Jugador j : jugadores) {
            if (j.getApostado() == apuestaMaxima) {
                jugadoresEnCompetencia.add(j);
            }
        }

        if (jugadoresEnCompetencia.size() == 1) {
            return jugadoresEnCompetencia.get(0);
        }

        if (jugadoresEnCompetencia.isEmpty()) {
            return getJugadorActual();
        }

        for (Jugador player : jugadoresEnCompetencia) {
            int[] valoresDados = player.getVasoJugador().getValores();
            int puntajeActual = player.getMano().verificarMano(valoresDados);

            if (puntajeActual > mejorPuntaje) {
                mejorPuntaje = puntajeActual;
                ganador = player;
                mejoresDados = valoresDados;

            } else if (puntajeActual == mejorPuntaje && mejoresDados != null) {
                int desempate = player.getMano().desempatar(valoresDados, mejoresDados);
                if (desempate > 0) {
                    ganador = player;
                    mejoresDados = valoresDados;
                }
            }
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

    //!creo que no se va a necesesitar mas
    public void prepararSiguienteRonda() {
        jugadorQueEmpieza = (jugadorQueEmpieza + 1) % jugadores.size();
        indiceJugadorActual = jugadorQueEmpieza;

        for (Jugador j : jugadores) {
            j.setPlantado(false);
            j.setPlantoApuesta(false);
            j.setHaApostado(false);
            j.setApostado(new Apuesta(j, 0));
        }

        apuestaMaxima = 0;
        apuestas.clear();
        bote = 0;

        rondaActual = EventoPartida.RONDA_TIRADAS;
        reiniciarTiradas();
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
            jugador.setManoPoker(jugador.getVasoJugador());
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
        jugador.setManoPoker(jugador.getVasoJugador());
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
            j.setManoPoker(null);


            j.setApostado(new Apuesta(j, 0));
        }

        notificarObservadores(EventoPartida.JUEGO_INICIADO);
    }
    @Override
    public String evaluarJugada() throws RemoteException {
        Jugador jugadorActual = getJugadorActual();
        int[] valoresDados = jugadorActual.getVasoJugador().getValores();
        int resultado = jugadorActual.getMano().verificarMano(valoresDados);

        return switch (resultado) {
            case 7 -> "Poker real";
            case 6 -> "Poker cuadruple";
            case 5 -> "Full";
            case 4 -> "Escalera mayor";
            case 3 -> "Escalera menor";
            case 2 -> "Piernas";
            case 1 -> "Pares dobles";
            case 0 -> "Pares";
            default -> "Sin valor";
        };
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

            //guardado de historial
            String jugadaTxt = evaluarJugadaTexto(ganador);
            RegistroPartida reg = new RegistroPartida(ganador.getNombre(), this.bote, jugadaTxt);
            historial.add(0, reg);
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


