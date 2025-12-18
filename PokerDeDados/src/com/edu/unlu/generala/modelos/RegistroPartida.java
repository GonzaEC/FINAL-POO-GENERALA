package com.edu.unlu.generala.modelos;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RegistroPartida implements Serializable {
    private static final long serialVersionUID = 1L;

    private String nombre;
    private int premio;
    private String jugada;
    private String fecha;

    public RegistroPartida(String nombre, int premio, String jugada) {
        this.nombre = nombre;
        this.premio = premio;
        this.jugada = jugada;
        this.fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }

    public String getNombre() { return nombre; }
    public int getPremio() { return premio; }
    public String getJugada() { return jugada; }
    public String getFecha() { return fecha; }
}