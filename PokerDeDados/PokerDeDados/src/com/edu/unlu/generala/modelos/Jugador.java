package com.edu.unlu.generala.modelos;

import java.io.Serializable;
import java.util.List;

public class Jugador extends Persona implements Serializable {
    private String nombre;
    private int saldo;
    private Apuesta apostado;
    private Vaso vasoJugador;
    private boolean haApostado = false;
    private boolean plantado = false;
    List<Dado> dadosGuardados;
    private boolean sePlantoApuesta = false;

    private ResultadoMano mejorJugada;

    public Jugador(String nombre, int saldoInicial) {
        this.nombre = nombre;

        this.vasoJugador =new Vaso();
        this.saldo = saldoInicial;
    }

    public int getSaldo() {
        return saldo;
    }

    public String getNombre() {
        return nombre;
    }
    public ResultadoMano getMejorJugada() {
        return mejorJugada;
    }

    public void setMejorJugada(ResultadoMano mejorJugada) {
        this.mejorJugada = mejorJugada;
    }

    public int getApostado() {
        return apostado != null ? apostado.getCantidad() : 0;
    }

    public Vaso getVasoJugador(){return this.vasoJugador;}

    public void setApostado(Apuesta apostado) {
        this.apostado = apostado;
    }

    public void agregarSaldo(int saldo){
        this.saldo += saldo;
    }

    public boolean retirarSaldo(int monto) {
        if(saldo > monto){
            saldo -= monto;
            return true;
        }
        else {
            return false;
        }
    }
    public void apostar(int monto){
        this.apostado = new Apuesta(this, monto);
        this.haApostado = true;
    }
    public Apuesta getApuesta(){
        return this.apostado;
    }
    public boolean haApostado() {
        return haApostado;
    }
    public void setHaApostado(boolean haApostado) {
        this.haApostado = haApostado;
    }


    public List<Dado> getDadosGuardados() {
        return dadosGuardados;
    }
    public void setDadosGuardados(List<Dado> dadosGuardados) {
        this.dadosGuardados = dadosGuardados;
    }

    public boolean isPlantado() {
        return this.plantado;
    }

    public void setPlantado(boolean b) {
        this.plantado = b;
    }

    public void aumentarApuesta(int cantidad) {
        if (saldo >= cantidad) {
            saldo -= cantidad;

            if (apostado == null) {
                apostado = new Apuesta(this, cantidad);
            } else {
                // Sumás a la apuesta ya existente
                apostado.setApuesta(apostado.getCantidad() + cantidad);
            }
            haApostado = true;
        } else {
            throw new IllegalArgumentException("Saldo insuficiente para aumentar apuesta");
        }
    }
    public boolean isPlantoApuesta() {
        return sePlantoApuesta;
    }

    public void setPlantoApuesta(boolean b) {
        this.sePlantoApuesta = b;
    }

}
