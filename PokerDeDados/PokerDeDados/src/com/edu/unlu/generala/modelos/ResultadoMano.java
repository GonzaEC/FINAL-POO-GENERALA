package com.edu.unlu.generala.modelos;

import java.io.Serializable;

public class ResultadoMano implements Serializable, Comparable<ResultadoMano> {
    private String nombre;
    private int jerarquia;
    private int sumaDesempate;

    public ResultadoMano(String nombre, int jerarquia, int sumaDesempate) {
        this.nombre = nombre;
        this.jerarquia = jerarquia;
        this.sumaDesempate = sumaDesempate;
    }

    public String getNombre() { return nombre; }
    public int getJerarquia() { return jerarquia; }

    @Override
    public int compareTo(ResultadoMano otro) {
        int comparacion = Integer.compare(this.jerarquia, otro.jerarquia);

        if (comparacion == 0) {
            return Integer.compare(this.sumaDesempate, otro.sumaDesempate);
        }
        return comparacion;
    }

    @Override
    public String toString() {
        return nombre + " (Puntos: " + jerarquia + ")";
    }
}