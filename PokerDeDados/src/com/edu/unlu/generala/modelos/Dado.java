package com.edu.unlu.generala.modelos;

import java.io.Serializable;
import java.util.Random;

public class Dado implements Serializable {
    private int cara;
    static private final Random numeroAleatorio = new Random();

    public Dado(){
        tirar();
    }

    public void tirar() {
        cara = numeroAleatorio.nextInt(6)+1;
    }

    public int getCara(){
        return this.cara;
    }

}
